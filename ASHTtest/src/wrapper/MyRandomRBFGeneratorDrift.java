/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrapper;

import java.io.Serializable;
import java.util.Random;
import moa.core.InstancesHeader;
import moa.core.MiscUtils;
import moa.core.ObjectRepository;
import moa.options.AbstractOptionHandler;
import moa.options.FloatOption;
import moa.options.IntOption;
import moa.streams.InstanceStream;
import moa.tasks.TaskMonitor;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author mahmud
 */
public class MyRandomRBFGeneratorDrift extends AbstractOptionHandler implements
        InstanceStream  {

    @Override
    public String getPurposeString() {
        return "Generates a random radial basis function stream with drift.";
    }

    private static final long serialVersionUID = 1L;

    public IntOption modelRandomSeedOption = new IntOption("modelRandomSeed",
            'r', "Seed for random generation of model.", (int) System.currentTimeMillis());

    public IntOption instanceRandomSeedOption = new IntOption(
            "instanceRandomSeed", 'i',
            "Seed for random generation of instances.", (int) System.currentTimeMillis());

    public IntOption numClassesOption = new IntOption("numClasses", 'c',
            "The number of classes to generate.", 2, 2, Integer.MAX_VALUE);

    public IntOption numAttsOption = new IntOption("numAtts", 'a',
            "The number of attributes to generate.", 10, 0, Integer.MAX_VALUE);

    public IntOption numCentroidsOption = new IntOption("numCentroids", 'n',
            "The number of centroids in the model.", 50, 1, Integer.MAX_VALUE);

    protected static class Centroid implements Serializable {

        private static final long serialVersionUID = 1L;

        public double[] centre;

        public int classLabel;

        public double stdDev;
    }

    public FloatOption speedChangeOption = new FloatOption("speedChange", 's',
            "Speed of change of centroids in the model.", 0, 0, Float.MAX_VALUE);

    public IntOption numDriftCentroidsOption = new IntOption("numDriftCentroids", 'k',
            "The number of centroids with drift.", 50, 0, Integer.MAX_VALUE);
    
    public IntOption classStartIndexOption = new IntOption("classLabelStartIndex",
            'z', "Class label starts from.", 1);
    
    protected double[][] speedCentroids;
    
    protected InstancesHeader streamHeader;

    protected Centroid[] centroids;

    protected double[] centroidWeights;

    protected Random instanceRandom;

    @Override
    public void prepareForUseImpl(TaskMonitor monitor,
            ObjectRepository repository) {
        monitor.setCurrentActivity("Preparing random RBF...", -1.0);
        generateHeader();
        generateCentroids();
        restart();
    }

    @Override
    public InstancesHeader getHeader() {
        return this.streamHeader;
    }

    @Override
    public long estimatedRemainingInstances() {
        return -1;
    }

    @Override
    public boolean hasMoreInstances() {
        return true;
    }

    @Override
    public boolean isRestartable() {
        return true;
    }

    @Override
    public void restart() {
        this.instanceRandom = new Random(this.instanceRandomSeedOption.getValue());
    }

    private Instance _nextInstance() {
        Centroid centroid = this.centroids[MiscUtils.chooseRandomIndexBasedOnWeights(this.centroidWeights,
                this.instanceRandom)];
        int numAtts = this.numAttsOption.getValue();
        double[] attVals = new double[numAtts + 1];
        for (int i = 0; i < numAtts; i++) {
            attVals[i] = (this.instanceRandom.nextDouble() * 2.0) - 1.0;
        }
        double magnitude = 0.0;
        for (int i = 0; i < numAtts; i++) {
            magnitude += attVals[i] * attVals[i];
        }
        magnitude = Math.sqrt(magnitude);
        double desiredMag = this.instanceRandom.nextGaussian()
                * centroid.stdDev;
        double scale = desiredMag / magnitude;
        for (int i = 0; i < numAtts; i++) {
            attVals[i] = centroid.centre[i] + attVals[i] * scale;
        }
        Instance inst = new DenseInstance(1.0, attVals);
        inst.setDataset(getHeader());
        inst.setClassValue(centroid.classLabel);
        return inst;
    }

    protected void generateHeader() {
        FastVector attributes = new FastVector();
        for (int i = 0; i < this.numAttsOption.getValue(); i++) {
            attributes.addElement(new Attribute("att" + (i + 1)));
        }
        
        int classoffset = classStartIndexOption.getValue()-1;
        FastVector classLabels = new FastVector();
        //for (int i = 0; i < classoffset; i++) {
        //    classLabels.addElement("classx"+ i);
        //}
        for (int i = classoffset; i < this.numClassesOption.getValue()+classoffset; i++) {
            classLabels.addElement("class" + (i + 1));
        }
        attributes.addElement(new Attribute("class", classLabels));
        this.streamHeader = new InstancesHeader(new Instances(
                getCLICreationString(InstanceStream.class), attributes, 0));
        this.streamHeader.setClassIndex(this.streamHeader.numAttributes() - 1);
    }

    private void _generateCentroids() {
        Random modelRand = new Random(this.modelRandomSeedOption.getValue());
        this.centroids = new Centroid[this.numCentroidsOption.getValue()];
        this.centroidWeights = new double[this.centroids.length];
        for (int i = 0; i < this.centroids.length; i++) {
            this.centroids[i] = new Centroid();
            double[] randCentre = new double[this.numAttsOption.getValue()];
            for (int j = 0; j < randCentre.length; j++) {
                randCentre[j] = modelRand.nextDouble();
            }
            this.centroids[i].centre = randCentre;
            
            int classoffset = classStartIndexOption.getValue()-1;
            this.centroids[i].classLabel = classoffset + modelRand.nextInt(this.numClassesOption.getValue());
            
            this.centroids[i].stdDev = modelRand.nextDouble();
            this.centroidWeights[i] = modelRand.nextDouble();
        }
    }

    @Override
    public Instance nextInstance() {
        //Update Centroids with drift
        int len = this.numDriftCentroidsOption.getValue();
        if (len > this.centroids.length) {
            len = this.centroids.length;
        }
        for (int j = 0; j < len; j++) {
            for (int i = 0; i < this.numAttsOption.getValue(); i++) {
                this.centroids[j].centre[i] += this.speedCentroids[j][i] * this.speedChangeOption.getValue();
                if (this.centroids[j].centre[i] > 1) {
                    this.centroids[j].centre[i] = 1;
                    this.speedCentroids[j][i] = -this.speedCentroids[j][i];
                }
                if (this.centroids[j].centre[i] < 0) {
                    this.centroids[j].centre[i] = 0;
                    this.speedCentroids[j][i] = -this.speedCentroids[j][i];
                }
            }
        }
        return _nextInstance();
    }

    protected void generateCentroids() {
        _generateCentroids();
        Random modelRand = new Random(this.modelRandomSeedOption.getValue());
        int len = this.numDriftCentroidsOption.getValue();
        if (len > this.centroids.length) {
            len = this.centroids.length;
        }
        this.speedCentroids = new double[len][this.numAttsOption.getValue()];
        for (int i = 0; i < len; i++) {
            double[] randSpeed = new double[this.numAttsOption.getValue()];
            double normSpeed = 0.0;
            for (int j = 0; j < randSpeed.length; j++) {
                randSpeed[j] = modelRand.nextDouble();
                normSpeed += randSpeed[j] * randSpeed[j];
            }
            normSpeed = Math.sqrt(normSpeed);
            for (int j = 0; j < randSpeed.length; j++) {
                randSpeed[j] /= normSpeed;
            }
            this.speedCentroids[i] = randSpeed;
        }
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) {
        // TODO Auto-generated method stub
    }
}
