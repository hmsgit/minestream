/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrapper;
/*
 *    MyOzaBagASHT.java
 *    Copyright (C) 2008 University of Waikato, Hamilton, New Zealand
 *    @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
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

import com.sun.javafx.scene.traversal.Algorithm;
import java.util.Arrays;
import java.util.Collection;
import moa.classifiers.Classifier;
import moa.core.DoubleVector;
import moa.core.MiscUtils;
import moa.options.IntOption;
import moa.options.FlagOption;
import weka.core.Instance;
import weka.core.Utils;

/**
 * Bagging using trees of different size.
 * The Adaptive-Size Hoeffding Tree (ASHT) is derived from the Hoeffding Tree
 * algorithm with the following differences:
 * <ul>
 * <li> it has a maximum number of split nodes, or size
 * <li> after one node splits, if the number of split nodes of the ASHT tree
 * is higher than the maximum value, then it deletes some nodes to reduce its size
 * </ul>
 * The intuition behind this method is as follows: smaller trees adapt
 * more quickly to changes, and larger trees do better during periods with
 * no or little change, simply because they were built on more data. Trees
 * limited to size s will be reset about twice as often as trees with a size
 * limit of 2s. This creates a set of different reset-speeds for an ensemble of such
 * trees, and therefore a subset of trees that are a good approximation for the
 * current rate of change. It is important to note that resets will happen all
 * the time, even for stationary datasets, but this behaviour should not have
 * a negative impact on the ensembleâ€™s predictive performance.
 * When the tree size exceeds the maximun size value, there are two different
 * delete options: <ul>
 * <li> delete the oldest node, the root, and all of its children except the one
 * where the split has been made. After that, the root of the child not
 * deleted becomes the new root
 * <li> delete all the nodes of the tree, i.e., restart from a new root.
 * </ul>
 * The maximum allowed size for the n-th ASHT tree is twice the maximum
 * allowed size for the (n-1)-th tree. Moreover, each tree has a weight
 * proportional to the inverse of the square of its error, and it monitors its
 * error with an exponential weighted moving average (EWMA) with alpha = .01.
 * The size of the first tree is 2.
 * <br/><br/>
 * With this new method, it is attempted to improve bagging performance
 * by increasing tree diversity. It has been observed that boosting tends to
 * produce a more diverse set of classifiers than bagging, and this has been
 * cited as a factor in increased performance.<br/>
 * See more details in:<br/><br/>
 * Albert Bifet, Geoff Holmes, Bernhard Pfahringer, Richard Kirkby,
 * and Ricard GavaldÃ . New ensemble methods for evolving data
 * streams. In 15th ACM SIGKDD International Conference on Knowledge
 * Discovery and Data Mining, 2009.<br/><br/>
 * The learner must be MyASHoeffdingTree, a Hoeffding Tree with a maximum
 * size value.<br/><br/>
 * Example:<br/><br/>
 * <code>MyOzaBagASHT -l MyASHoeffdingTree -s 10 -u -r </code>
 * Parameters:<ul>
 * <li>Same parameters as <code>MyOzaBag</code>
 * <li>-f : the size of first classifier in the bag.
 * <li>-u : Enable weight classifiers
 * <li>-e : Reset trees when size is higher than the max
 * </ul>
 *
 * @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 * @version $Revision: 7 $
 */
public class MyOzaBagASHT extends MyOzaBag {
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

    @Override
    public void resetLearningImpl() {
        this.ensemble = new Classifier[this.ensembleSizeOption.getValue()];
        this.error = new double[this.ensembleSizeOption.getValue()];
        Classifier baseLearner = (Classifier) getPreparedClassOption(this.baseLearnerOption);
        baseLearner.resetLearning();
        int pow = this.firstClassifierSizeOption.getValue(); //EXTENSION TO ASHT
        for (int i = 0; i < this.ensemble.length; i++) {
            this.ensemble[i] = baseLearner.copy();
            this.error[i] = 0.0;
            ((MyASHoeffdingTree) this.ensemble[i]).setMaxSize(pow); //EXTENSION TO ASHT
            if ((this.resetTreesOption != null)
                    && this.resetTreesOption.isSet()) {
                ((MyASHoeffdingTree) this.ensemble[i]).setResetTree();
            }
            pow *= 2; //EXTENSION TO ASHT
        }
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
        int trueClass = (int) inst.classValue();
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
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree
                    && ((MyASHoeffdingTree)this.ensemble[i]).measureTreeDepth() != 0) {
                
                StringBuilder outx = new StringBuilder();
                out.append("--------TREE--------: "+ i +":\n");
                ((MyASHoeffdingTree)this.ensemble[i]).getModelDescription(outx, indent);
                out.append(outx.toString());
            }
        }
    }
    
    @Override
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
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet[i] = ((MyASHoeffdingTree)this.ensemble[i]).getResetCount();
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
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet[i] = ((MyASHoeffdingTree)this.ensemble[i]).getPrunedAlternateTrees();
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
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet += ((MyASHoeffdingTree)this.ensemble[i]).calcByteSize();
            else 
                return 0;
        }
        return toRet;
    }
    
    @Override
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
    @Override
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
    @Override
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

    @Override
    public double getResetCount() {
        int toRet = 0;
        int pow = this.firstClassifierSizeOption.getValue();
        int sum = 0;
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet += pow *((MyASHoeffdingTree)this.ensemble[i]).getResetCount();
            else 
                return 0;
            sum += pow;
            pow *= 2;
        }
        double ret = (double)toRet / sum;
        return (double) Math.round(ret * 100)/100;
    }
    
    @Override
    public double getPrunedAlternateTrees() {
        int toRet = 0;
        int pow = this.firstClassifierSizeOption.getValue();
        int sum = 0;
        for (int i = 0; i < this.ensemble.length; i++) {
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet += pow *((MyASHoeffdingTree)this.ensemble[i]).getPrunedAlternateTrees();
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
            if (this.ensemble[i] instanceof wrapper.MyASHoeffdingTree)
                toRet += ((MyASHoeffdingTree)this.ensemble[i]).measureTreeDepth();
            else 
                return 0;
        }
        //return toRet;
        return (double) Math.round(toRet * 100 / this.ensemble.length)/100;
    }
}
