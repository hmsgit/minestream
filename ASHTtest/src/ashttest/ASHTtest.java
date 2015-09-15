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
    private  Classifier ht;
    static InstanceStream trainingStream;

    public void test(InstanceStream inStream, Classifier classifier) 
            throws Exception {
        
        ht = classifier;
        trainingStream = inStream;
        
        if (ht instanceof MyHoeffdingTree) {
            
        } else if (ht instanceof MyOzaBagASHT) {
            ((MyOzaBagASHT)ht).baseLearnerOption.setValueViaCLIString("wrapper.MyASHoeffdingTree");
        }
        
        ht.prepareForUse();
        ht.setModelContext(trainingStream.getHeader());
        
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
        
        System.out.println("Count,\tTime,\tTP,\tFP,\tTN,\tFN,"
                + "\tAccu,\tKappa,\tKpa_t,\tMem,"
                + "\tDepth,\tTSize,\tDcNod,\tActLf,\tInacLf,"
                + "\tReset,\tAlter,\tSwitch,\tPrune,"
                + "\tClOp0,\tClOp1,\tClOp2,\tClOp3");
        
        Instance trainInst = trainingStream.nextInstance();
        while (trainingStream.hasMoreInstances()
                && numberInstance++ < TestParameters.NUMBER_OF_INSTANCES) {
            
            double[] votes = ht.getVotesForInstance(trainInst);
            windowEval.addResult(trainInst, votes);
            basicEval.addResult(trainInst, votes);
            
            // manual counting
            if (votes.length != 0 && votes.length != 1) {
                if (ht.correctlyClassifies(trainInst)) {
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
            
            ht.trainOnInstance(trainInst);
            trainInst = trainingStream.nextInstance();
            
            if (--windowCount == 0) {
                windowCount = TestParameters.WIDTH;
                
                elapsedTime += System.currentTimeMillis() - startTime;
                xtp = tp - xtp; xtn = tn - xtn; xfp = fp - xfp; xfn = fn - xfn;
                
                System.out.print(numberInstance     + ",\t");
                System.out.print((double)elapsedTime/1000        + ",\t");
                System.out.print(xtp        + ",\t");
                System.out.print(xfp        + ",\t");
                System.out.print(xtn        + ",\t");
                System.out.print(xfn        + ",\t");
                
                System.out.print((double)Math.round(windowEval.getFractionCorrectlyClassified()*10000)/100   + ",\t");
                System.out.print((double)Math.round(windowEval.getKappaStatistic()*10000)/100                + ",\t");
                System.out.print((double)Math.round(windowEval.getKappaTemporalStatistic()*10000)/100      + ",\t");
                
                printClassifierInfos();
                //printTree();
                System.out.println();
                
                startTime = System.currentTimeMillis();
            }
        }
        elapsedTime += System.currentTimeMillis() - startTime;
        
        System.out.println();        
        System.out.print(--numberInstance               + ",\t");
        System.out.print((double)elapsedTime/1000       + ",\t");
        System.out.print(tp        + ",\t");
        System.out.print(fp        + ",\t");
        System.out.print(tn        + ",\t");
        System.out.print(fn        + ",\t");

        System.out.print((double)Math.round(basicEval.getFractionCorrectlyClassified()*10000)/100   + ",\t");
        System.out.print((double)Math.round(basicEval.getKappaStatistic()*10000)/100                + ",\t");
        System.out.print((double)Math.round(basicEval.getKappaTemporalStatistic()*10000)/100      + ",\t");
        
        printClassifierInfos();
        //printTree();
        System.out.println();
    }
    
    void printClassifierInfos() {
        if (ht instanceof MyHoeffdingTree) {
            MyHoeffdingTree _xht = (MyHoeffdingTree) ht;
            System.out.print(-_xht.calcByteSize()+ ",\t");

            System.out.print(_xht.measureTreeDepth()+ ",\t");
            System.out.print(_xht.getDecisionNodeCount()+_xht.getActiveLeafNodeCount()
                    +_xht.getInactiveLeafNodeCount()+ ",\t");
            System.out.print(_xht.getDecisionNodeCount()+ ",\t");
            System.out.print(_xht.getActiveLeafNodeCount()+ ",\t");
            System.out.print(_xht.getInactiveLeafNodeCount()+ ",\t");

            System.out.print(_xht.getResetCount()+ ",\t");
            System.out.print(_xht.getAlternateTreeCount()+ ",\t");
            System.out.print(_xht.getSwitchedAlternateTrees()+ ",\t");
            System.out.print(_xht.getPrunedAlternateTrees()+ ",\t");

            System.out.print(0+ ",\t");
            System.out.print(0+ ",\t");
            System.out.print(0+ ",\t");
            System.out.print(0+ ",\t");

        } else if (ht instanceof MyOzaBagASHT) {
            MyOzaBagASHT _xht = (MyOzaBagASHT) ht;
            System.out.print(-_xht.calcByteSize()+ ",\t");

            System.out.print(_xht.measureTreeDepth()+ ",\t"); // Average depth
            System.out.print(_xht.getDecisionNodeCount()+_xht.getActiveLeafNodeCount()
                    +_xht.getInactiveLeafNodeCount()+ ",\t"); // total
            System.out.print(_xht.getDecisionNodeCount()+ ",\t"); // total
            System.out.print(_xht.getActiveLeafNodeCount()+ ",\t"); // total
            System.out.print(_xht.getInactiveLeafNodeCount()+ ",\t"); // total

            System.out.print(_xht.getResetCount()+ ",\t"); // weighted reset count
            System.out.print(0 + ",\t");
            System.out.print(0 + ",\t");
            System.out.print(_xht.getPrunedAlternateTrees()+ ",\t"); // 0, if reset

            //ClOp0-1-2-3
            System.out.print(_xht.maxDepth()+   ",\t");
            System.out.print(_xht.minDepth()+   ",\t");
            if (_xht.resetTreesOption.isSet()) {
                System.out.print(_xht.maxReset()+   ",\t");
                System.out.print(_xht.minReset()+   ",\t");
            } else {
                System.out.print(_xht.maxPruned()+   ",\t");
                System.out.print(_xht.minPruned()+   ",\t");
            }
        }
    }
    void printTree() {
        StringBuilder sb = new StringBuilder();
        if (ht instanceof MyHoeffdingTree) {
            ((MyHoeffdingTree)ht).getModelDescription(sb, 0);            
        } else if (ht instanceof MyOzaBagASHT) {
            ((MyOzaBagASHT)ht).getModelDescription(sb, 0);
        }
        System.out.println("\n"+sb.toString());
    }
}
