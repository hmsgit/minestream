/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrapper;

import java.util.Random;
import moa.core.InstancesHeader;
import moa.core.ObjectRepository;
import moa.options.AbstractOptionHandler;
import moa.options.IntOption;
import moa.streams.InstanceStream;
import moa.tasks.TaskMonitor;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author mahmud
 */
public class StackedRBFStream extends AbstractOptionHandler implements
        InstanceStream  {

    private MyRandomRBFGeneratorDrift[] generators;
    
    public IntOption modelRandomSeedOption = new IntOption("modelRandomSeed",
            'x', "Seed for random generation of model.", (int) System.currentTimeMillis());
    public IntOption numClassesOption = new IntOption("numClasses", 'c',
        "The number of classes to generate.", 2, 2, Integer.MAX_VALUE);
    
    private Random modelRand;
    protected InstancesHeader streamHeader;
    
    public StackedRBFStream(int stackSize) {
        generators = new MyRandomRBFGeneratorDrift[stackSize];
        for (int i = 0; i < generators.length; i++) {
            generators[i] = new MyRandomRBFGeneratorDrift();
            //generators[i].prepareForUse();
        }
        
        modelRand = new Random(this.modelRandomSeedOption.getValue());
    }
    
    public void setNumClassesOption(int value) {
        for (MyRandomRBFGeneratorDrift generator : generators) {
            generator.numClassesOption.setValue(value);
        }
    }
    public void setNumClassesOption(int generatorNumber, int value) {
        if (generatorNumber > generators.length - 1) return;
        generators[generatorNumber].numClassesOption.setValue(value);
    }
    public void setNumCentroidOpiton(int value) {
        for (MyRandomRBFGeneratorDrift generator : generators) {
            generator.numCentroidsOption.setValue(value);
        }
    }
    public void setClassStartIndexOption(int value) {
        for (MyRandomRBFGeneratorDrift generator : generators) {
            generator.classStartIndexOption.setValue(value);
        }
    }
    public void setClassStartIndexOption(int generatorNumber, int value) {
        if (generatorNumber > generators.length - 1) return;
        generators[generatorNumber].classStartIndexOption.setValue(value);
    }
    public void setNumCentroidOpiton(int generatorNumber, int value) {
        if (generatorNumber > generators.length - 1) return;
        generators[generatorNumber].numCentroidsOption.setValue(value);
    }
    public void setNumDriftCentroidOpiton(int value) {
        for (MyRandomRBFGeneratorDrift generator : generators) {
            generator.numDriftCentroidsOption.setValue(value);
        }
    }
    public void setNumDriftCentroidOpiton(int generatorNumber, int value) {
        if (generatorNumber > generators.length - 1) return;
        generators[generatorNumber].numDriftCentroidsOption.setValue(value);
    }
    public void setSpeedChangeOpiton(double value) {
        for (MyRandomRBFGeneratorDrift generator : generators) {
            generator.speedChangeOption.setValue(value);
        }
    }
    public void setSpeedChangeOpiton(int generatorNumber, double value) {
        if (generatorNumber > generators.length - 1) return;
        generators[generatorNumber].speedChangeOption.setValue(value);
    }
    
    @Override
    public String getPurposeString() {
        return "Generates a random radial basis function stream with drift.";
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void prepareForUseImpl(TaskMonitor monitor,
            ObjectRepository repository) {
        monitor.setCurrentActivity("Preparing random RBF...", -1.0);
        
        for (MyRandomRBFGeneratorDrift generator : generators) {
            generator.generateHeader();
            generator.generateCentroids();
            generator.restart();
        }
    }

    @Override
    public InstancesHeader getHeader() {
        FastVector attributes = new FastVector();
        for (int i = 0; i < generators[0].numAttsOption.getValue(); i++) {
            attributes.addElement(new Attribute("att" + (i + 1)));
        }

        FastVector classLabels = new FastVector();
        for (int i = 0; i < numClassesOption.getValue(); i++) {
            classLabels.addElement("class" + (i + 1));
        }
        attributes.addElement(new Attribute("class", classLabels));
        streamHeader = new InstancesHeader(new Instances(
                getCLICreationString(InstanceStream.class), attributes, 0));
        streamHeader.setClassIndex(generators[0].streamHeader.numAttributes() - 1);
        
        return streamHeader;
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
        for (MyRandomRBFGeneratorDrift generator : generators) {
            generator.instanceRandom = new Random(generator.instanceRandomSeedOption.getValue());
        }
    }

    protected void generateHeader() {
        for (MyRandomRBFGeneratorDrift generator : generators) {
            generator.generateHeader();
        }
    }

    protected void generateCentroids() {
        for (MyRandomRBFGeneratorDrift generator : generators) {
            generator.generateCentroids();
        }
    }

    @Override
    public Instance nextInstance() {
        int ind = modelRand.nextInt(generators.length);
        return generators[ind].nextInstance();
    }
    
    @Override
    public void prepareForUse() {
        for (MyRandomRBFGeneratorDrift generator : generators) {
            generator.prepareForUse();
        }
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) {
        // TODO Auto-generated method stub
    }
}
