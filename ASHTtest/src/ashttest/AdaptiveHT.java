/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ashttest;

import dbaccess.DBConnection;
import java.util.ArrayList;
import moa.classifiers.trees.HoeffdingAdaptiveTree;
import moa.evaluation.WindowClassificationPerformanceEvaluator;
import moa.options.IntOption;
import moa.streams.InstanceStream;
import models.BayesianStream;
import models.Tweet;
import weka.core.Instance;

/**
 *
 * @author mahmud
 */
public class AdaptiveHT {

    /**
     * @param args the command line arguments
     */


    private static DBConnection connection;
    //evaulator window size
    private final static int outputFrequency = 1000; //each X instances, evaluator outputs result
    //private final static int WIDTH = 1000; //each WIDTH instances, evaluator is reset


    // CLASSIFIER
    /**
     * Naive Bayes Multinomial Model
     */
    private static HoeffdingAdaptiveTree asht = new HoeffdingAdaptiveTree();
    /**
     * This is the Java code for a prequential evaluation
     * Connects to Controller.java and fetches Tweets from database
     */
    /**
     * The trainingStream that is generated from the database
     */
    static InstanceStream trainingStream;
    static ArrayList<Tweet> training;

    /**
     * Filter that converts the instances into tf*idf Vector representation
     */
    //static StringToWordVector vector;

    // Variables for the time measurements
    double start1;

    public static void maind(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        
        String dataset = "tweetdb";
        String usr = "root";
        String pass = "";
        connection = new DBConnection(dataset, usr, pass);
        training = connection.trainingSet;
        
        // transformation of the set into a stream for tarining
        trainingStream = new BayesianStream(training);
        //vector = ((BayesianStream) trainingStream).getVector();
        
        WindowClassificationPerformanceEvaluator evaluator = new WindowClassificationPerformanceEvaluator();
        evaluator.widthOption = new IntOption("Width", 'w', "Window width", 1000); //default!

        // Prepare classifier for learning
        asht.prepareForUse();
        asht.setModelContext(trainingStream.getHeader());
        //Count number of correctly classified tweets
        int numberSamplesCorrect = 0;
        int numberSamples = 0;
        //Keeping track of where we are
        int windowCount = outputFrequency;
        int falsePositives = 0;
        int falseNegatives = 0;
        int truePositives = 0;
        int trueNegatives = 0;
        int lastRow = 0;
        int elsecount = 0;
        
        while (trainingStream.hasMoreInstances()) {
            if (((BayesianStream) trainingStream).numInstances % 1000 == 0)
                System.out.print(((BayesianStream) trainingStream).numInstances + "\t");
            windowCount--;
            Instance trainInst = trainingStream.nextInstance();

            numberSamples++;
            double[] votes = asht.getVotesForInstance(trainInst);
            
            //evaluator.addResult(trainInst, votes);
            
            if (votes.length != 0 && votes.length != 1)
                if (asht.correctlyClassifies(trainInst)) {
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
            else 
                elsecount++;
            asht.trainOnInstance(trainInst);

        }
        double accuracy = 100.0 * (double) numberSamplesCorrect / (double) numberSamples;
        //System.out.println("Number of Performance Measurements: " + evaluator.getPerformanceMeasurements().length);
        System.out.println("\nClassic Prequential Preprocessed " + numberSamples + 
                "\ninstances processed with " + accuracy + "% accuracy"+
                "\nelsecount: " + elsecount);
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
