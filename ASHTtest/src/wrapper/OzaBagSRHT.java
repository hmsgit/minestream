/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import moa.classifiers.Classifier;
import moa.core.DoubleVector;
import moa.core.MiscUtils;
import moa.options.FlagOption;
import moa.options.IntOption;
import weka.core.Instance;
import weka.core.Utils;



/**
 *
 * @author mahmud
 */

public class OzaBagSRHT extends MyOzaBag {
    private static final long serialVersionUID = 1L;

    @Override
    public String getPurposeString() {
        return "Bagging using trees of different size.";
    }
    
    public IntOption firstClassifierSizeOption = new IntOption("firstClassifierSize", 'f',
            "The size of first classifier in the bag.", 1, 1, Integer.MAX_VALUE);

    public FlagOption useWeightOption = new FlagOption("useWeight",
            'u', "Enable weight classifiers.");

    public FlagOption resetTreesOption = new FlagOption("resetTrees",
            'e', "Reset trees when size is higher than the max.");

    protected double[] error;

    protected double alpha = 0.01;

    public int resetCount;
    ArrayList<Classifier> dyingClassifiers = new ArrayList<>();
    
    
    @Override
    public void resetLearningImpl() {
        this.ensemble = new Classifier[this.ensembleSizeOption.getValue()];
        this.error = new double[this.ensembleSizeOption.getValue()];
        this.resetCount = 0;
        Classifier baseLearner = (Classifier) getPreparedClassOption(this.baseLearnerOption);
        baseLearner.resetLearning();
        int pow = this.firstClassifierSizeOption.getValue(); //EXTENSION TO ASHT
        for (int i = 0; i < this.ensemble.length; i++) {
            this.ensemble[i] = baseLearner.copy();
            this.error[i] = 0.0;
            ((SizeRestrictedHT) this.ensemble[i]).setMaxSize(pow); //EXTENSION TO ASHT
            if ((this.resetTreesOption != null)
                    && this.resetTreesOption.isSet()) {
                ((SizeRestrictedHT) this.ensemble[i]).setResetTree();
            }
            pow *= 2; //EXTENSION TO ASHT
        }
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
        int trueClass = (int) inst.classValue();
        for (Classifier cl : dyingClassifiers) {
            SizeRestrictedHT cx = (SizeRestrictedHT) cl;
            cx.trainOnInstance(inst);
            if (cx.getToleranceRemain() == 0)
                dyingClassifiers.remove(cl);
        }
        for (int i = 0; i < this.ensemble.length; i++) {
            int k = MiscUtils.poisson(1.0, this.classifierRandom);
            if (k > 0) {
                Instance weightedInst = (Instance) inst.copy();
                weightedInst.setWeight(inst.weight() * k);
                if (Utils.maxIndex(this.ensemble[i].getVotesForInstance(inst)) == trueClass) {
                    this.error[i] += alpha * (0.0 - this.error[i]); //EWMA
                } else {
                    this.error[i] += alpha * (1.0 - this.error[i]); //EWMA
                }
                this.ensemble[i].trainOnInstance(weightedInst);
                
                SizeRestrictedHT ens = (SizeRestrictedHT) ensemble[i];
                if (ens.getDecisionNodeCount() >= ens.maxSize) {
                    if (i >= this.ensembleSizeOption.getValue()/2 
                            //&& dyingClassifiers.size() <= this.ensembleSizeOption.getValue()
                            )
                        dyingClassifiers.add(ens);
                    if (dyingClassifiers.size() > this.ensembleSizeOption.getValue()) {
                        dyingClassifiers.remove(0);
                        resetCount++;
                    }
                    
                    Classifier baseLearner = (Classifier) getPreparedClassOption(this.baseLearnerOption);
                    baseLearner.resetLearning();
                    this.ensemble[i] = baseLearner.copy();
                    this.error[i] = 0.0;
                    ((SizeRestrictedHT) this.ensemble[i]).setMaxSize(ens.maxSize);
                    if ((this.resetTreesOption != null)
                            && this.resetTreesOption.isSet()) {
                        ((SizeRestrictedHT) this.ensemble[i]).setResetTree();
                    }
                    this.ensemble[i].trainOnInstance(weightedInst);
                }
            }
        }
    }

    @Override
    public double[] getVotesForInstance(Instance inst) {
        DoubleVector combinedVote = new DoubleVector();
        
        for (Classifier cl : dyingClassifiers) {
            SizeRestrictedHT cx = (SizeRestrictedHT) cl;
            DoubleVector vote = new DoubleVector(cx.getVotesForInstance(inst));
            if (vote.sumOfValues() > 0.0) {
                vote.normalize();
                combinedVote.addValues(vote);
            }
        }
        for (int i = 0; i < this.ensemble.length; i++) {
            DoubleVector vote = new DoubleVector(this.ensemble[i].getVotesForInstance(inst));
            if (vote.sumOfValues() > 0.0) {
                vote.normalize();
                if ((this.useWeightOption != null)
                        && this.useWeightOption.isSet()) {
                    vote.scaleValues(1.0 / (this.error[i] * this.error[i]));
                }
                combinedVote.addValues(vote);
            }
        }
        return combinedVote.getArrayRef();
    }

    @Override
    public void getModelDescription(StringBuilder out, int indent) {        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.SizeRestrictedHT
                    && ((SizeRestrictedHT)this.ensemble[i]).measureTreeDepth() != 0) {
                
                StringBuilder outx = new StringBuilder();
                out.append("--------TREE--------: "+ i +":\n");
                ((SizeRestrictedHT)this.ensemble[i]).getModelDescription(outx, indent);
                out.append(outx.toString());
            }
        }
    }
    
    @Override
    public int [] measureTreeDepths() {
        int [] toRet = new int[this.ensemble.length];
        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.SizeRestrictedHT)
                toRet[i] = ((SizeRestrictedHT)this.ensemble[i]).measureTreeDepth();
            else 
                return null;
        }
        return toRet;
    }
    @Override
    public int maxDepth() {
        int ret[] = measureTreeDepths();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[ret.length-1];
    }
    @Override
    public int minDepth() {
        int ret[] = measureTreeDepths();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[0];
    }
    @Override
    public int [] getResetCounts() {
        int [] toRet = new int[this.ensemble.length];
        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.SizeRestrictedHT)
                toRet[i] = ((SizeRestrictedHT)this.ensemble[i]).getResetCount();
            else 
                return null;
        }
        return toRet;
    }
    @Override
    public int maxReset() {
        int ret[] = getResetCounts();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[ret.length-1];
    }
    @Override
    public int minReset() {
        int ret[] = getResetCounts();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[0];
    }
    @Override
    public int [] getPrunedCounts() {
        int [] toRet = new int[this.ensemble.length];
        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.SizeRestrictedHT)
                toRet[i] = ((SizeRestrictedHT)this.ensemble[i]).getPrunedAlternateTrees();
            else 
                return null;
        }
        return toRet;
    }
    @Override
    public int maxPruned() {
        int ret[] = getPrunedCounts();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[ret.length-1];
    }
    @Override
    public int minPruned() {
        int ret[] = getPrunedCounts();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[0];
    }
    
    @Override
    public int calcByteSize() {
        int toRet = 0;
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.SizeRestrictedHT)
                toRet += ((SizeRestrictedHT)this.ensemble[i]).calcByteSize();
            else 
                return 0;
        }
        return toRet;
    }
    
    @Override
    public int getDecisionNodeCount() {
        int toRet = 0;
        for (Classifier ensemble1 : this.ensemble) {
            if (ensemble1 instanceof wrapper.SizeRestrictedHT) {
                toRet += ((SizeRestrictedHT) ensemble1).getDecisionNodeCount();
            } else { 
                return 0;
            }
        }
        return toRet;
    }
    @Override
    public int getActiveLeafNodeCount() {
        int toRet = 0;
        for (Classifier ensemble1 : this.ensemble) {
            if (ensemble1 instanceof wrapper.SizeRestrictedHT) {
                toRet += ((SizeRestrictedHT) ensemble1).getActiveLeafNodeCount();
            } else { 
                return 0;
            }
        }
        return toRet;
    }
    @Override
    public int getInactiveLeafNodeCount() {
        int toRet = 0;
        for (Classifier ensemble1 : this.ensemble) {
            if (ensemble1 instanceof wrapper.SizeRestrictedHT) {
                toRet += ((SizeRestrictedHT) ensemble1).getInactiveLeafNodeCount();
            } else { 
                return 0;
            }
        }
        return toRet;
    }

    @Override
    public double getResetCount() {
        int toRet = 0;
        int pow = this.firstClassifierSizeOption.getValue();
        int sum = 0;
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.SizeRestrictedHT)
                toRet += pow *((SizeRestrictedHT)this.ensemble[i]).getResetCount();
            else 
                return resetCount;
            sum += pow;
            pow *= 2;
        }
        double ret = (double)toRet / sum;
        return resetCount; //(double) Math.round(ret * 100)/100;
    }
    
    @Override
    public double getPrunedAlternateTrees() {
        int toRet = 0;
        int pow = this.firstClassifierSizeOption.getValue();
        int sum = 0;
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.SizeRestrictedHT)
                toRet += pow *((SizeRestrictedHT)this.ensemble[i]).getPrunedAlternateTrees();
            else 
                return 0;
            sum += pow;
            pow *= 2;
        }
        double ret = (double)toRet / sum;
        return (double) Math.round(ret * 100)/100;
    }
    
    @Override
    public double measureTreeDepth() {
        double toRet = 0;
        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.SizeRestrictedHT)
                toRet += ((SizeRestrictedHT)this.ensemble[i]).measureTreeDepth();
            else 
                return 0;
        }
        //return toRet;
        return (double) Math.round(toRet * 100 / this.ensemble.length)/100;
    }
}