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
 *    MyOzaBoost.java
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
import moa.options.FlagOption;
import moa.options.IntOption;

/**
 * Incremental on-line boosting of Oza and Russell.
 *
 * <p>See details in:<br /> N. Oza and S. Russell. Online bagging and boosting.
 * In Artiï¬�cial Intelligence and Statistics 2001, pages 105â€“112. Morgan
 * Kaufmann, 2001.</p> <p>For the boosting method, Oza and Russell note that the
 * weighting procedure of AdaBoost actually divides the total example weight
 * into two halves â€“ half of the weight is assigned to the correctly classiï¬�ed
 * examples, and the other half goes to the misclassiï¬�ed examples. They use the
 * Poisson distribution for deciding the random probability that an example is
 * used for training, only this time the parameter changes according to the
 * boosting weight of the example as it is passed through each model in
 * sequence.</p>
 *
 * <p>Parameters:</p> <ul> <li>-l : Classiï¬�er to train</li> <li>-s : The number
 * of models to boost</li> <li>-p : Boost with weights only; no poisson</li>
 * </ul>
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @version $Revision: 7 $
 */
public class MyOzaBoost extends AbstractClassifier {

    private static final long serialVersionUID = 1L;

    @Override
    public String getPurposeString() {
        return "Incremental on-line boosting of Oza and Russell.";
    }

    public ClassOption baseLearnerOption = new ClassOption("baseLearner", 'l',
            "Classifier to train.", Classifier.class, "wrapper.MyHoeffdingTree");

    public IntOption ensembleSizeOption = new IntOption("ensembleSize", 's',
            "The number of models to boost.", 10, 1, Integer.MAX_VALUE);

    public FlagOption pureBoostOption = new FlagOption("pureBoost", 'p',
            "Boost with weights only; no poisson.");

    protected Classifier[] ensemble;

    protected double[] scms;

    protected double[] swms;

    @Override
    public void resetLearningImpl() {
        this.ensemble = new Classifier[this.ensembleSizeOption.getValue()];
        Classifier baseLearner = (Classifier) getPreparedClassOption(this.baseLearnerOption);
        baseLearner.resetLearning();
        for (int i = 0; i < this.ensemble.length; i++) {
            this.ensemble[i] = baseLearner.copy();
        }
        this.scms = new double[this.ensemble.length];
        this.swms = new double[this.ensemble.length];
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
        double lambda_d = 1.0;
        for (int i = 0; i < this.ensemble.length; i++) {
            double k = this.pureBoostOption.isSet() ? lambda_d : MiscUtils.poisson(lambda_d, this.classifierRandom);
            if (k > 0.0) {
                Instance weightedInst = (Instance) inst.copy();
                weightedInst.setWeight(inst.weight() * k);
                this.ensemble[i].trainOnInstance(weightedInst);
            }
            if (this.ensemble[i].correctlyClassifies(inst)) {
                this.scms[i] += lambda_d;
                lambda_d *= this.trainingWeightSeenByModel / (2 * this.scms[i]);
            } else {
                this.swms[i] += lambda_d;
                lambda_d *= this.trainingWeightSeenByModel / (2 * this.swms[i]);
            }
        }
    }

    protected double getEnsembleMemberWeight(int i) {
        double em = this.swms[i] / (this.scms[i] + this.swms[i]);
        if ((em == 0.0) || (em > 0.5)) {
            return 0.0;
        }
        double Bm = em / (1.0 - em);
        return Math.log(1.0 / Bm);
    }

    public double[] getVotesForInstance(Instance inst) {
        DoubleVector combinedVote = new DoubleVector();
        for (int i = 0; i < this.ensemble.length; i++) {
            double memberWeight = getEnsembleMemberWeight(i);
            if (memberWeight > 0.0) {
                DoubleVector vote = new DoubleVector(this.ensemble[i].getVotesForInstance(inst));
                if (vote.sumOfValues() > 0.0) {
                    vote.normalize();
                    vote.scaleValues(memberWeight);
                    combinedVote.addValues(vote);
                }
            } else {
                break;
            }
        }
        return combinedVote.getArrayRef();
    }

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
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree
                    && ((MyASHoeffdingTree)this.ensemble[i]).measureTreeDepth() != 0) {
                
                StringBuilder outx = new StringBuilder();
                out.append("--------TREE--------: "+ i +":\n");
                ((MyASHoeffdingTree)this.ensemble[i]).getModelDescription(outx, indent);
                out.append(outx.toString());
            }
        }
    }
    
    public int [] measureTreeDepths() {
        int [] toRet = new int[this.ensemble.length];
        
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet[i] = ((MyASHoeffdingTree)this.ensemble[i]).measureTreeDepth();
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
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet[i] = ((MyASHoeffdingTree)this.ensemble[i]).getResetCount();
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
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet[i] = ((MyASHoeffdingTree)this.ensemble[i]).getPrunedAlternateTrees();
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
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet += ((MyASHoeffdingTree)this.ensemble[i]).calcByteSize();
            else 
                return 0;
        }
        return toRet;
    }
    
    public int getDecisionNodeCount() {
        int toRet = 0;
        for (Classifier ensemble1 : this.ensemble) {
            if (ensemble1 instanceof wrapper.MyASHoeffdingTree) {
                toRet += ((MyASHoeffdingTree) ensemble1).getDecisionNodeCount();
            } else { 
                return 0;
            }
        }
        return toRet;
    }
    public int getActiveLeafNodeCount() {
        int toRet = 0;
        for (Classifier ensemble1 : this.ensemble) {
            if (ensemble1 instanceof wrapper.MyASHoeffdingTree) {
                toRet += ((MyASHoeffdingTree) ensemble1).getActiveLeafNodeCount();
            } else { 
                return 0;
            }
        }
        return toRet;
    }
    public int getInactiveLeafNodeCount() {
        int toRet = 0;
        for (Classifier ensemble1 : this.ensemble) {
            if (ensemble1 instanceof wrapper.MyASHoeffdingTree) {
                toRet += ((MyASHoeffdingTree) ensemble1).getInactiveLeafNodeCount();
            } else { 
                return 0;
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
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet += ((MyASHoeffdingTree)this.ensemble[i]).measureTreeDepth();
            else 
                return 0;
        }
        //return toRet;
        return (double) Math.round(toRet * 100 / this.ensemble.length)/100;
    }
}

