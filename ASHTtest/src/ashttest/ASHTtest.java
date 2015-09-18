/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ashttest;

import moa.evaluation.BasicClassificationPerformanceEvaluator;
import wrapper.*;

import moa.classifiers.Classifier;
import moa.evaluation.WindowClassificationPerformanceEvaluator;
import moa.options.IntOption;
import moa.streams.InstanceStream;
import weka.core.Instance;

/**
 *
 * @author mahmud
 */

public class ASHTtest {
    private  Classifier classifier;
    static InstanceStream trainingStream;
    private StringBuilder stats;
    
    private boolean printSteps = true;

    public void test(InstanceStream inStream, Classifier inclassifier, StringBuilder outstats) 
            throws Exception {
        
        classifier = inclassifier;
        trainingStream = inStream;
        stats = outstats;
        
        if (classifier instanceof MyHoeffdingTree) {
            
        } else if (classifier instanceof MyOzaBagASHT) {
            ((MyOzaBagASHT)classifier).baseLearnerOption.setValueViaCLIString("wrapper.MyASHoeffdingTree");
        }
        
        classifier.prepareForUse();
        classifier.setModelContext(trainingStream.getHeader());
        
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        
        WindowClassificationPerformanceEvaluator windowEval = new WindowClassificationPerformanceEvaluator();
        windowEval.widthOption = new IntOption("Width", 'w', "Window width", TestParameters.WIDTH); //default!
        BasicClassificationPerformanceEvaluator basicEval = new BasicClassificationPerformanceEvaluator();
        
        //Keeping track of where we are
        int numberInstance = 0;
        int windowCount = TestParameters.WIDTH;
        int tp = 0, tn = 0, fp = 0, fn = 0;
        int xtp = 0, xtn = 0, xfp = 0, xfn = 0;
        
        stats.append("Count,\tTime,\tTP,\tFP,\tTN,\tFN,"
                + "\tAccu,\tKappa,\tKpa_t,\tMem,"
                + "\tDepth,\tTSize,\tDcNod,\tActLf,\tInacLf,"
                + "\tReset,\tAlter,\tSwitch,\tPrune,"
                + "\tClOp0,\tClOp1,\tClOp2,\tClOp3\n");
        
        Instance trainInst = trainingStream.nextInstance();
        while (trainingStream.hasMoreInstances()
                && numberInstance++ < TestParameters.NUMBER_OF_INSTANCES) {
            
            double[] votes = classifier.getVotesForInstance(trainInst);
            windowEval.addResult(trainInst, votes);
            basicEval.addResult(trainInst, votes);
            
            // manual counting
            if (votes.length != 0 && votes.length != 1) {
                if (classifier.correctlyClassifies(trainInst)) {
                    if (votes[0] >= votes[1])
                        tp++;
                    else
                        tn++;
                } else {
                    if (votes[0] >= votes[1])
                        fp++;
                    else
                        fn++;
                }
            }
            
            classifier.trainOnInstance(trainInst);
            trainInst = trainingStream.nextInstance();
            
            if (--windowCount == 0) {
                windowCount = TestParameters.WIDTH;
                
                elapsedTime += System.currentTimeMillis() - startTime;
                xtp = tp - xtp; xtn = tn - xtn; xfp = fp - xfp; xfn = fn - xfn;
                
                StringBuilder stepStats;
                stepStats = printSteps == true ? stats: new StringBuilder();
                
                stepStats.append(numberInstance     + ",\t");
                stepStats.append((double)elapsedTime/1000        + ",\t");
                stepStats.append(xtp        + ",\t");
                stepStats.append(xfp        + ",\t");
                stepStats.append(xtn        + ",\t");
                stepStats.append(xfn        + ",\t");
                
                stepStats.append((double)Math.round(windowEval.getFractionCorrectlyClassified()*10000)/100   + ",\t");
                stepStats.append((double)Math.round(windowEval.getKappaStatistic()*10000)/100                + ",\t");
                stepStats.append((double)Math.round(windowEval.getKappaTemporalStatistic()*10000)/100      + ",\t");
                
                printClassifierInfos(stepStats);
                //printClassifierInfos();
                //printTree();
                stepStats.append("\n");
                
                startTime = System.currentTimeMillis();
            }
        }
        elapsedTime += System.currentTimeMillis() - startTime;
        
        if (printSteps == true) stats.append("\n");
        stats.append(--numberInstance               + ",\t");
        stats.append((double)elapsedTime/1000       + ",\t");
        stats.append(tp        + ",\t");
        stats.append(fp        + ",\t");
        stats.append(tn        + ",\t");
        stats.append(fn        + ",\t");

        stats.append((double)Math.round(basicEval.getFractionCorrectlyClassified()*10000)/100   + ",\t");
        stats.append((double)Math.round(basicEval.getKappaStatistic()*10000)/100                + ",\t");
        stats.append((double)Math.round(basicEval.getKappaTemporalStatistic()*10000)/100      + ",\t");
        
        printClassifierInfos();
        //printTree();
        stats.append("\n\n");
    }
    
    void printClassifierInfos() {
        printClassifierInfos(stats);
    }
    void printClassifierInfos(StringBuilder stats) {
        if (classifier instanceof MyHoeffdingTree) {
            MyHoeffdingTree _xht = (MyHoeffdingTree) classifier;
            stats.append(-_xht.calcByteSize()+ ",\t");

            stats.append(_xht.measureTreeDepth()+ ",\t");
            stats.append(_xht.getDecisionNodeCount()+_xht.getActiveLeafNodeCount()
                    +_xht.getInactiveLeafNodeCount()+ ",\t");
            stats.append(_xht.getDecisionNodeCount()+ ",\t");
            stats.append(_xht.getActiveLeafNodeCount()+ ",\t");
            stats.append(_xht.getInactiveLeafNodeCount()+ ",\t");

            stats.append(_xht.getResetCount()+ ",\t");
            stats.append(_xht.getAlternateTreeCount()+ ",\t");
            stats.append(_xht.getSwitchedAlternateTrees()+ ",\t");
            stats.append(_xht.getPrunedAlternateTrees()+ ",\t");

            stats.append(0+ ",\t");
            stats.append(0+ ",\t");
            stats.append(0+ ",\t");
            stats.append(0+ ",\t");

        } else if (classifier instanceof OzaBagSRHT) {
            OzaBagSRHT _xht = (OzaBagSRHT) classifier;
            stats.append(-_xht.calcByteSize()+ ",\t");

            stats.append(_xht.measureTreeDepth()+ ",\t"); // Average depth
            stats.append(_xht.getDecisionNodeCount()+_xht.getActiveLeafNodeCount()
                    +_xht.getInactiveLeafNodeCount()+ ",\t"); // total
            stats.append(_xht.getDecisionNodeCount()+ ",\t"); // total
            stats.append(_xht.getActiveLeafNodeCount()+ ",\t"); // total
            stats.append(_xht.getInactiveLeafNodeCount()+ ",\t"); // total

            stats.append(_xht.getResetCount()+ ",\t"); // weighted reset count
            stats.append(0 + ",\t");
            stats.append(0 + ",\t");
            stats.append(_xht.getPrunedAlternateTrees()+ ",\t"); // 0, if reset

            //ClOp0-1-2-3
            stats.append(_xht.maxDepth()+   ",\t");
            stats.append(_xht.minDepth()+   ",\t");
            if (_xht.resetTreesOption.isSet()) {
                stats.append(_xht.maxReset()+   ",\t");
                stats.append(_xht.minReset()+   ",\t");
            } else {
                stats.append(_xht.maxPruned()+   ",\t");
                stats.append(_xht.minPruned()+   ",\t");
            }
        } else if (classifier instanceof MyOzaBagASHT) {
            MyOzaBagASHT _xht = (MyOzaBagASHT) classifier;
            stats.append(-_xht.calcByteSize()+ ",\t");

            stats.append(_xht.measureTreeDepth()+ ",\t"); // Average depth
            stats.append(_xht.getDecisionNodeCount()+_xht.getActiveLeafNodeCount()
                    +_xht.getInactiveLeafNodeCount()+ ",\t"); // total
            stats.append(_xht.getDecisionNodeCount()+ ",\t"); // total
            stats.append(_xht.getActiveLeafNodeCount()+ ",\t"); // total
            stats.append(_xht.getInactiveLeafNodeCount()+ ",\t"); // total

            stats.append(_xht.getResetCount()+ ",\t"); // weighted reset count
            stats.append(0 + ",\t");
            stats.append(0 + ",\t");
            stats.append(_xht.getPrunedAlternateTrees()+ ",\t"); // 0, if reset

            //ClOp0-1-2-3
            stats.append(_xht.maxDepth()+   ",\t");
            stats.append(_xht.minDepth()+   ",\t");
            if (_xht.resetTreesOption.isSet()) {
                stats.append(_xht.maxReset()+   ",\t");
                stats.append(_xht.minReset()+   ",\t");
            } else {
                stats.append(_xht.maxPruned()+   ",\t");
                stats.append(_xht.minPruned()+   ",\t");
            }
        } else if (classifier instanceof MyOzaBagAdwin) {
            MyOzaBagAdwin _xht = (MyOzaBagAdwin) classifier;
            stats.append(-_xht.calcByteSize()+ ",\t");

            stats.append(_xht.measureTreeDepth()+ ",\t"); // Average depth
            stats.append(_xht.getDecisionNodeCount()+_xht.getActiveLeafNodeCount()
                    +_xht.getInactiveLeafNodeCount()+ ",\t"); // total
            stats.append(_xht.getDecisionNodeCount()+ ",\t"); // total
            stats.append(_xht.getActiveLeafNodeCount()+ ",\t"); // total
            stats.append(_xht.getInactiveLeafNodeCount()+ ",\t"); // total

            stats.append(_xht.getResetCount()+ ",\t"); // weighted reset count
            stats.append(0 + ",\t");
            stats.append(0 + ",\t");
            stats.append(_xht.getPrunedAlternateTrees()+ ",\t"); // 0, if reset

            //ClOp0-1-2-3
            stats.append(_xht.maxDepth()+   ",\t");
            stats.append(_xht.minDepth()+   ",\t");
            
            stats.append(_xht.maxReset()+   ",\t");
            stats.append(_xht.minReset()+   ",\t");
        } else if (classifier instanceof MyOzaBoostAdwin) {
            MyOzaBoostAdwin _xht = (MyOzaBoostAdwin) classifier;
            stats.append(-_xht.calcByteSize()+ ",\t");

            stats.append(_xht.measureTreeDepth()+ ",\t"); // Average depth
            stats.append(_xht.getDecisionNodeCount()+_xht.getActiveLeafNodeCount()
                    +_xht.getInactiveLeafNodeCount()+ ",\t"); // total
            stats.append(_xht.getDecisionNodeCount()+ ",\t"); // total
            stats.append(_xht.getActiveLeafNodeCount()+ ",\t"); // total
            stats.append(_xht.getInactiveLeafNodeCount()+ ",\t"); // total

            stats.append(_xht.getResetCount()+ ",\t"); // weighted reset count
            stats.append(0 + ",\t");
            stats.append(0 + ",\t");
            stats.append(_xht.getPrunedAlternateTrees()+ ",\t"); // 0, if reset

            //ClOp0-1-2-3
            stats.append(_xht.maxDepth()+   ",\t");
            stats.append(_xht.minDepth()+   ",\t");
            
            stats.append(_xht.maxReset()+   ",\t");
            stats.append(_xht.minReset()+   ",\t");
        }
        //stats.append("\n");
    }
    
    void printTree() {
        StringBuilder sb = new StringBuilder();
        if (classifier instanceof MyHoeffdingTree) {
            ((MyHoeffdingTree)classifier).getModelDescription(sb, 0);            
        } else if (classifier instanceof MyOzaBagASHT) {
            ((MyOzaBagASHT)classifier).getModelDescription(sb, 0);
        } else if (classifier instanceof MyOzaBagAdwin) {
            ((MyOzaBagAdwin)classifier).getModelDescription(sb, 0);
        } else if (classifier instanceof MyOzaBoostAdwin) {
            ((MyOzaBoostAdwin)classifier).getModelDescription(sb, 0);
        }
        System.out.println("\n"+sb.toString());
    }
}
