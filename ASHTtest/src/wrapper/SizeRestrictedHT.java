/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrapper;

import weka.core.Instance;

/**
 *
 * @author mahmud
 */

public class SizeRestrictedHT extends MyHoeffdingAdaptiveTree {
    private static final long serialVersionUID = 1L;
    
    private int resetCount = 0;
    private int pruneCount = 0;

    @Override
    public String getPurposeString() {
        return "Size Resticted Adaptive Size Hoeffding Tree used in Bagging using trees of different size.";
    }    
    
    protected int maxSize = 10000; //EXTENSION TO ASHT

    protected boolean resetTree = true;
    
    
    private int shouldResetAfter = 1000;
    private int numSizeLimitCrossed = 0;
    
    public void setTolerance(int value) {shouldResetAfter = value;}
    public int getToleranceRemain() {return shouldResetAfter - numSizeLimitCrossed;}

    @Override
    public void resetLearningImpl() {
        this.treeRoot = null;
        this.decisionNodeCount = 0;
        this.activeLeafNodeCount = 0;
        this.inactiveLeafNodeCount = 0;
        this.inactiveLeafByteSizeEstimate = 0.0;
        this.activeLeafByteSizeEstimate = 0.0;
        this.byteSizeEstimateOverheadFraction = 1.0;
        this.growthAllowed = true;
        
        numSizeLimitCrossed = 0;
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
        if (this.treeRoot == null) {
            this.treeRoot = newLearningNode();
            this.activeLeafNodeCount = 1;
        }
        MyHoeffdingTree.FoundNode foundNode = this.treeRoot.filterInstanceToLeaf(inst, null, -1);
        MyHoeffdingTree.Node leafNode = foundNode.node;
        if (leafNode == null) {
            leafNode = newLearningNode();
            foundNode.parent.setChild(foundNode.parentBranch, leafNode);
            this.activeLeafNodeCount++;
        }
        if (leafNode instanceof MyHoeffdingTree.LearningNode) {
            MyHoeffdingTree.LearningNode learningNode = (MyHoeffdingTree.LearningNode) leafNode;
            learningNode.learnFromInstance(inst, this);
            if (this.growthAllowed
                    && (learningNode instanceof MyHoeffdingTree.ActiveLearningNode)) {
                MyHoeffdingTree.ActiveLearningNode activeLearningNode = (MyHoeffdingTree.ActiveLearningNode) learningNode;
                double weightSeen = activeLearningNode.getWeightSeen();
                if (weightSeen
                        - activeLearningNode.getWeightSeenAtLastSplitEvaluation() >= this.gracePeriodOption.getValue()) {
                    
                    int currentDNcount = this.decisionNodeCount;
                    if (this.decisionNodeCount < this.maxSize) {
                        attemptToSplit(activeLearningNode, foundNode.parent,
                                foundNode.parentBranch);
                    } else {
                        ActiveLearningNode backup = (ActiveLearningNode) activeLearningNode.copy();
                        int alnc = this.activeLeafNodeCount;
                        attemptToSplit(activeLearningNode, foundNode.parent,
                                foundNode.parentBranch);
                        if (this.decisionNodeCount != currentDNcount) {
                            numSizeLimitCrossed++;
                            
                            this.activeLeafNodeCount++;
                            this.decisionNodeCount--;
                            this.activeLeafNodeCount = alnc;
                            foundNode.parent.setChild(foundNode.parentBranch, backup);
                        }
                    }
                    if (numSizeLimitCrossed >= shouldResetAfter) {
                        resetLearningImpl();
                        resetCount++;
                    }
                    //EXTENSION TO ASHT
                    // if size too big, resize tree ONLY Split Nodes
//                    while (this.decisionNodeCount >= this.maxSize && this.treeRoot instanceof MyHoeffdingTree.SplitNode) {
//                        if (this.resetTree == false) {
//                            resizeTree(this.treeRoot, ((MyHoeffdingTree.SplitNode) this.treeRoot).instanceChildIndex(inst));
//                            this.treeRoot = ((MyHoeffdingTree.SplitNode) this.treeRoot).getChild(((MyHoeffdingTree.SplitNode) this.treeRoot).instanceChildIndex(inst));
//                            this.decisionNodeCount--; //It should be here, it wasn't
//                            pruneCount++;
//                        } else {
//                            resetLearningImpl();
//                            resetCount++;
//                        }
//                    }
                    activeLearningNode.setWeightSeenAtLastSplitEvaluation(weightSeen);
                }
            }
        }
        if (this.trainingWeightSeenByModel
                % this.memoryEstimatePeriodOption.getValue() == 0) {
            estimateModelByteSizes();
        }
    }

    //EXTENSION TO ASHT
    public void setMaxSize(int mSize) {
        this.maxSize = mSize;
    }

    public void setResetTree() {
        this.resetTree = true;
    }
    public void setResetTree(boolean status) {
        this.resetTree = status;
    }

    public void deleteNode(MyHoeffdingTree.Node node, int childIndex) {
        MyHoeffdingTree.Node child = ((MyHoeffdingTree.SplitNode) node).getChild(childIndex);
        //if (child != null) {
        //}
        if (child instanceof MyHoeffdingTree.SplitNode) {
            for (int branch = 0; branch < ((MyHoeffdingTree.SplitNode) child).numChildren(); branch++) {
                deleteNode(child, branch);
            }
            this.decisionNodeCount--;
        } else if (child instanceof MyHoeffdingTree.InactiveLearningNode) {
            this.inactiveLeafNodeCount--;
        } else if (child instanceof MyHoeffdingTree.ActiveLearningNode) {
            this.activeLeafNodeCount--;
        }
        child = null;
    }

    public void resizeTree(MyHoeffdingTree.Node node, int childIndex) {
        //Assume that this is root node
        if (node instanceof MyHoeffdingTree.SplitNode) {
            for (int branch = 0; branch < ((MyHoeffdingTree.SplitNode) node).numChildren(); branch++) {
                if (branch != childIndex) {
                    deleteNode(node, branch);
                }
            }
        }
    }
    @Override
    public int getResetCount() {return resetCount;}
    @Override
    public int getPrunedAlternateTrees() {return pruneCount;}
}
