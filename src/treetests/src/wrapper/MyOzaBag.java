/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrapper;

/**
 *
 * @author mahmud
 */
/*
 *    MyOzaBag.java
 *    Copyright (C) 2007 University of Waikato, Hamilton, New Zealand
 *    @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program. If not, see <http://www.gnu.org/licenses/>.
 *    
 */

import java.util.Arrays;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.Classifier;
import weka.core.Instance;

import moa.core.DoubleVector;
import moa.core.Measurement;
import moa.core.MiscUtils;
import moa.options.ClassOption;
import moa.options.IntOption;

/**
 * Incremental on-line bagging of Oza and Russell.
 *
 * <p>Oza and Russell developed online versions of bagging and boosting for
 * Data Streams. They show how the process of sampling bootstrap replicates
 * from training data can be simulated in a data stream context. They observe
 * that the probability that any individual example will be chosen for a
 * replicate tends to a Poisson(1) distribution.</p>
 *
 * <p>[OR] N. Oza and S. Russell. Online bagging and boosting.
 * In Artiï¬�cial Intelligence and Statistics 2001, pages 105â€“112.
 * Morgan Kaufmann, 2001.</p>
 *
 * <p>Parameters:</p> <ul>
 * <li>-l : Classiï¬�er to train</li>
 * <li>-s : The number of models in the bag</li> </ul>
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public class MyOzaBag extends AbstractClassifier {

    @Override
    public String getPurposeString() {
        return "Incremental on-line bagging of Oza and Russell.";
    }
        
    private static final long serialVersionUID = 1L;

    public ClassOption baseLearnerOption = new ClassOption("baseLearner", 'l',
            "Classifier to train.", Classifier.class, "wrapper.MyHoeffdingTree");

    public IntOption ensembleSizeOption = new IntOption("ensembleSize", 's',
            "The number of models in the bag.", 10, 1, Integer.MAX_VALUE);

    protected Classifier[] ensemble;

    @Override
    public void resetLearningImpl() {
        this.ensemble = new Classifier[this.ensembleSizeOption.getValue()];
        Classifier baseLearner = (Classifier) getPreparedClassOption(this.baseLearnerOption);
        baseLearner.resetLearning();
        for (int i = 0; i < this.ensemble.length; i++) {
            this.ensemble[i] = baseLearner.copy();
        }
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
        for (int i = 0; i < this.ensemble.length; i++) {
            int k = MiscUtils.poisson(1.0, this.classifierRandom);
            if (k > 0) {
                Instance weightedInst = (Instance) inst.copy();
                weightedInst.setWeight(inst.weight() * k);
                this.ensemble[i].trainOnInstance(weightedInst);
            }
        }
    }

    @Override
    public double[] getVotesForInstance(Instance inst) {
        DoubleVector combinedVote = new DoubleVector();
        for (int i = 0; i < this.ensemble.length; i++) {
            DoubleVector vote = new DoubleVector(this.ensemble[i].getVotesForInstance(inst));
            if (vote.sumOfValues() > 0.0) {
                vote.normalize();
                combinedVote.addValues(vote);
            }
        }
        return combinedVote.getArrayRef();
    }

    @Override
    public boolean isRandomizable() {
        return true;
    }


    @Override
    protected Measurement[] getModelMeasurementsImpl() {
        return new Measurement[]{new Measurement("ensemble size",
                    this.ensemble != null ? this.ensemble.length : 0)};
    }

    @Override
    public Classifier[] getSubClassifiers() {
        return this.ensemble.clone();
    }
    
    @Override
    public void getModelDescription(StringBuilder out, int indent) {        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.MyHoeffdingTree
                    && ((MyHoeffdingTree)this.ensemble[i]).measureTreeDepth() != 0) {
                
                StringBuilder outx = new StringBuilder();
                out.append("--------TREE--------: "+ i +":\n");
                ((MyHoeffdingTree)this.ensemble[i]).getModelDescription(outx, indent);
                out.append(outx.toString());
            }
        }
    }
    
    public int [] measureTreeDepths() {
        int [] toRet = new int[this.ensemble.length];
        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.MyHoeffdingTree)
                toRet[i] = ((MyHoeffdingTree)this.ensemble[i]).measureTreeDepth();
            else 
                return null;
        }
        return toRet;
    }
    public int maxDepth() {
        int ret[] = measureTreeDepths();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[ret.length-1];
    }
    public int minDepth() {
        int ret[] = measureTreeDepths();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[0];
    }
    public int [] getResetCounts() {
        int [] toRet = new int[this.ensemble.length];
        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.MyHoeffdingTree)
                toRet[i] = ((MyHoeffdingTree)this.ensemble[i]).getResetCount();
            else 
                return null;
        }
        return toRet;
    }
    public int maxReset() {
        int ret[] = getResetCounts();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[ret.length-1];
    }
    public int minReset() {
        int ret[] = getResetCounts();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[0];
    }
    public int [] getPrunedCounts() {
        int [] toRet = new int[this.ensemble.length];
        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.MyHoeffdingTree)
                toRet[i] = ((MyHoeffdingTree)this.ensemble[i]).getPrunedAlternateTrees();
            else 
                return null;
        }
        return toRet;
    }
    public int maxPruned() {
        int ret[] = getPrunedCounts();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[ret.length-1];
    }
    public int minPruned() {
        int ret[] = getPrunedCounts();
        if (ret == null) return 0;
        Arrays.sort(ret);
        return ret[0];
    }
    
    public int calcByteSize() {
        int toRet = 0;
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.MyHoeffdingTree)
                toRet += ((MyHoeffdingTree)this.ensemble[i]).calcByteSize();
            else if (this.ensemble[i] instanceof wrapper.MyHoeffdingAdaptiveTree)
                toRet += ((MyHoeffdingAdaptiveTree)this.ensemble[i]).calcByteSize();
            else if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet += ((MyASHoeffdingTree)this.ensemble[i]).calcByteSize();
            
        }
        return toRet;
    }
    
    public int getDecisionNodeCount() {
        int toRet = 0;
        for (Classifier ensemble1 : this.ensemble) {
            if (ensemble1 instanceof wrapper.MyHoeffdingTree) {
                toRet += ((MyHoeffdingTree) ensemble1).getDecisionNodeCount();
            } else if (ensemble1 instanceof wrapper.MyHoeffdingAdaptiveTree) {
                toRet += ((MyHoeffdingAdaptiveTree) ensemble1).getDecisionNodeCount();
            } else if (ensemble1 instanceof wrapper.MyASHoeffdingTree) {
                toRet += ((MyASHoeffdingTree) ensemble1).getDecisionNodeCount();
            }
        }
        return toRet;
    }
    public int getActiveLeafNodeCount() {
        int toRet = 0;
        for (Classifier ensemble1 : this.ensemble) {
            if (ensemble1 instanceof wrapper.MyHoeffdingTree) {
                toRet += ((MyHoeffdingTree) ensemble1).getActiveLeafNodeCount();
            } else if (ensemble1 instanceof wrapper.MyHoeffdingAdaptiveTree) {
                toRet += ((MyHoeffdingAdaptiveTree) ensemble1).getActiveLeafNodeCount();
            } else if (ensemble1 instanceof wrapper.MyASHoeffdingTree) {
                toRet += ((MyASHoeffdingTree) ensemble1).getActiveLeafNodeCount();
            }
        }
        return toRet;
    }
    public int getInactiveLeafNodeCount() {
        int toRet = 0;
        for (Classifier ensemble1 : this.ensemble) {
            if (ensemble1 instanceof wrapper.MyHoeffdingTree) {
                toRet += ((MyHoeffdingTree) ensemble1).getInactiveLeafNodeCount();
            } else if (ensemble1 instanceof wrapper.MyHoeffdingAdaptiveTree) {
                toRet += ((MyHoeffdingAdaptiveTree) ensemble1).getInactiveLeafNodeCount();
            } else if (ensemble1 instanceof wrapper.MyASHoeffdingTree) {
                toRet += ((MyASHoeffdingTree) ensemble1).getInactiveLeafNodeCount();
            }
        }
        return toRet;
    }

    public double getResetCount() {
        return 0;
    }
    
    public double getPrunedAlternateTrees() {
        return 0;
    }
    
    public double measureTreeDepth() {
        double toRet = 0;
        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.MyHoeffdingTree)
                toRet += ((MyHoeffdingTree)this.ensemble[i]).measureTreeDepth();
            
            else if (this.ensemble[i] instanceof wrapper.MyHoeffdingAdaptiveTree)
                toRet += ((MyHoeffdingAdaptiveTree)this.ensemble[i]).measureTreeDepth();
            else if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet += ((MyASHoeffdingTree)this.ensemble[i]).measureTreeDepth();
        }
        //return toRet;
        return (double) Math.round(toRet * 100 / this.ensemble.length)/100;
    }
}
