/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ashttest;

import moa.evaluation.WindowClassificationPerformanceEvaluator;
import moa.options.IntOption;
import moa.streams.InstanceStream;
import weka.core.Instance;
import moa.evaluation.BasicClassificationPerformanceEvaluator;
import wrapper.*;

/**
 *
 * @author mahmud
 */
public class HTtest {
    private  MyHoeffdingTree ht;
    static InstanceStream trainingStream;

    public void test(InstanceStream inStream, MyHoeffdingTree classifier) 
            throws Exception {
        
        ht = classifier;
        trainingStream = inStream;
        ht.prepareForUse();
        ht.setModelContext(trainingStream.getHeader());
        //ht.leafpredictionOption.setChosenIndex(2); //NB
        //ht.gracePeriodOption.setValue(1000); // default = 200
        
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
                System.out.print(-ht.calcByteSize()+ ",\t");
                
                System.out.print(ht.measureTreeDepth()+ ",\t");
                System.out.print(ht.getDecisionNodeCount()+ht.getActiveLeafNodeCount()
                        +ht.getInactiveLeafNodeCount()+ ",\t");
                System.out.print(ht.getDecisionNodeCount()+ ",\t");
                System.out.print(ht.getActiveLeafNodeCount()+ ",\t");
                System.out.print(ht.getInactiveLeafNodeCount()+ ",\t");
                
                System.out.print(ht.getResetCount()+ ",\t");
                System.out.print(ht.getAlternateTreeCount()+ ",\t");
                System.out.print(ht.getSwitchedAlternateTrees()+ ",\t");
                System.out.print(ht.getPrunedAlternateTrees()+ ",\t");
                
                System.out.print(0+ ",\t");
                System.out.print(0+ ",\t");
                System.out.print(0+ ",\t");
                System.out.print(0+ ",\t");
                //StringBuilder sb = new StringBuilder();
                //ht.getModelDescription(sb, 0);
                //System.out.println(sb.toString());
                System.out.println();
                
                startTime = System.currentTimeMillis();
            }
        }
        elapsedTime += System.currentTimeMillis() - startTime;
        
        System.out.println();
        
        System.out.print(--numberInstance     + ",\t");
        System.out.print((double)elapsedTime/1000        + ",\t");
        System.out.print(tp        + ",\t");
        System.out.print(fp        + ",\t");
        System.out.print(tn        + ",\t");
        System.out.print(fn        + ",\t");

        System.out.print((double)Math.round(basicEval.getFractionCorrectlyClassified()*10000)/100   + ",\t");
        System.out.print((double)Math.round(basicEval.getKappaStatistic()*10000)/100                + ",\t");
        System.out.print((double)Math.round(basicEval.getKappaTemporalStatistic()*10000)/100      + ",\t");
        System.out.print(-ht.calcByteSize()+ ",\t");
        
        System.out.print(ht.measureTreeDepth()+ ",\t");
        System.out.print(ht.getDecisionNodeCount()+ht.getActiveLeafNodeCount()
                +ht.getInactiveLeafNodeCount()+ ",\t");
        System.out.print(ht.getDecisionNodeCount()+ ",\t");
        System.out.print(ht.getActiveLeafNodeCount()+ ",\t");
        System.out.print(ht.getInactiveLeafNodeCount()+ ",\t");
        
        System.out.print(ht.getResetCount()+ ",\t");
        System.out.print(ht.getAlternateTreeCount()+ ",\t");
        System.out.print(ht.getSwitchedAlternateTrees()+ ",\t");
        System.out.print(ht.getPrunedAlternateTrees()+ ",\t");
        
        System.out.print(0+ ",\t");
        System.out.print(0+ ",\t");
        System.out.print(0+ ",\t");
        System.out.print(0+ ",\t");
        System.out.println();
    }
}
