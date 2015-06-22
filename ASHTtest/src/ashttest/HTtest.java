/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ashttest;

import dbaccess.DBConnection;
import java.util.ArrayList;
import moa.evaluation.WindowClassificationPerformanceEvaluator;
import moa.options.IntOption;
import moa.streams.ArffFileStream;
import moa.streams.InstanceStream;
import models.BayesianStream;
import weka.core.Instance;
import wrapper.MyHoeffdingTree;
import moa.classifiers.trees.*;
import moa.classifiers.meta.*;
import wrapper.*;

/**
 *
 * @author mahmud
 */
public class HTtest {
    private final static int outputFrequency = 1000; //each X instances, evaluator outputs result
    //private final static int WIDTH = 1000; //each WIDTH instances, evaluator is reset
    
    
    //private static  ASHoeffdingTree xht = new  ASHoeffdingTree();
    //private static  MyHoeffdingTree ht = new  wrapper.MyHoeffdingTree();
    private  MyHoeffdingAdaptiveTree ht;// = new  MyHoeffdingAdaptiveTree();
    
    //private static wrapper.MyOzaBagASHT ht = new wrapper.MyOzaBagASHT();
    
    static InstanceStream trainingStream;

    double start1;

    public void test(InstanceStream inStream, MyHoeffdingAdaptiveTree classifier) throws Exception {
        long startTime = System.currentTimeMillis();
        
        //oza bag
        //ht.baseLearnerOption.setValueViaCLIString("trees.ASHoeffdingTree");
        //ht.firstClassifierSizeOption.setValue(1);
        //ht.ensembleSizeOption.setValue(5);
        
        String dataset = "tweetdb";
        String usr = "root";
        String pass = "";
        trainingStream = inStream; //new moa.streams.ArffFileStream("cluster.csv", -1);
        ht = classifier;
        
        System.out.println("-:\t");
        
        //ht.leafpredictionOption.setChosenIndex(2); //NB
        ht.prepareForUse();
        ht.setModelContext(trainingStream.getHeader());
        //ht.gracePeriodOption.setValue(1000); // default = 200
        
        WindowClassificationPerformanceEvaluator evaluator = new WindowClassificationPerformanceEvaluator();
        evaluator.widthOption = new IntOption("Width", 'w', "Window width", 2000); //default!

        // Prepare classifier for learning
        int numberSamplesCorrect = 0;
        int numberSamples = 0;
        //Keeping track of where we are
        int windowCount = outputFrequency;
        int falsePositives = 0;
        int falseNegatives = 0;
        int truePositives = 0;
        int trueNegatives = 0;
        int lastRow = 0;
        
        int olddepth = -1;
        int olddepths []= new int[5]; //for asht
        int notcounted = 0;
        StringBuilder prev = null;
        
        Instance trainInst = trainingStream.nextInstance();
        int frequency[] = new int[trainInst.numAttributes()];
        while (trainingStream.hasMoreInstances()) {
            if (numberSamples % 1000 == 0)
                System.out.print(numberSamples + "\t");
            windowCount--;

            numberSamples++;
            if (numberSamples >= 200000) break;
            
            double[] votes = ht.getVotesForInstance(trainInst);
            
            //evaluator.addResult(trainInst, votes);
            
            if (votes.length != 0 && votes.length != 1) {
                if (ht.correctlyClassifies(trainInst)) {
                    if (votes[0] >= votes[1])
                        truePositives++;
                    else
                        trueNegatives++;
                    numberSamplesCorrect++;
                } else {
                    if (votes[0] >= votes[1])
                        falsePositives++;
                    else
                        falseNegatives++;
                }
            } else {
                notcounted++;
            }
            ht.trainOnInstance(trainInst);
            
            boolean isIncrease = false;
//            for (int i = 0; i < olddepths.length; i++)
//                if (olddepths[i] < ht.measureTreeDepths()[i])
//                    isIncrease = true;
            
            
            StringBuilder sb = new StringBuilder();
            ht.getModelDescription(sb, 0);
            if (olddepth < ht.measureTreeDepth()) {
            //if (isIncrease) {
                if(prev != null)
                    System.out.println(numberSamples +": \n"+ prev.toString());
                
                System.out.println(numberSamples +": \n"+ sb.toString());
                
            }
            prev = sb;
            olddepth = ht.measureTreeDepth();
            //olddepths = ht.measureTreeDepths();
            
            trainInst = trainingStream.nextInstance();
        }
        
        double accuracy = 100.0 * (double) numberSamplesCorrect / (double) numberSamples;
        //System.out.println("\nNumber of Performance Measurements: " + evaluator.getPerformanceMeasurements().length);
        //System.out.println("Number of Performance Measurements: " + evaluator.getPerformanceMeasurements().toString());
        System.out.println("Classic Prequential Preprocessed " + numberSamples + " instances processed with " + accuracy + "% accuracy");
        System.out.println("not counted: " + notcounted);
        System.out.println("False positives: " + falsePositives);
        System.out.println("False negatives: " + falseNegatives);
        System.out.println("True positives: " + truePositives);
        System.out.println("True negatives: " + trueNegatives);
        System.out.println("Sensitivity/True Positive Rate: " + 100.0 * (double)truePositives/((double)truePositives + (double)falseNegatives));
        System.out.println("Specificity/True Negative Rate: " + 100.0 * (double)trueNegatives/((double)trueNegatives + (double)falsePositives));
        double precision = 100.0 * (double)truePositives / ((double)truePositives + (double)falsePositives);
        double f1score = 200.0 * (double)truePositives / (2.0* (double)truePositives + (double)falsePositives + (double)falseNegatives);
        System.out.println("Precision/Positive Predictive Value: " + precision);
        System.out.println("F1 Score: " + f1score);

        System.out.println("Runtime: " + (System.currentTimeMillis() - startTime));
    }
}
