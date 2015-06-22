import jxl.Workbook;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import moa.evaluation.WindowClassificationPerformanceEvaluator;
import moa.options.IntOption;
import moa.streams.InstanceStream;
import weka.core.Instance;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.ArrayList;



public class PrequentialEvaluationFadingPreprocessedExcel {

    private static ExtendedDatabaseConnectionPreprocessed connection;

    /**
     * Preprocessor to clean the Tweets
     */
    //Frequency of output of the evaluation results

    private static Preprocessor preprocessor;

    // CLASSIFIER
    /**
     * Naive Bayes Multinomial Model
     */

    private static NaiveBayesMultinomialFading mnb = new NaiveBayesMultinomialFading();
    //private static NaiveBayesMultinomialAggressive mnb = new NaiveBayesMultinomialAggressive();
    //private static NaiveBayesMultinomialVanilla mnb = new NaiveBayesMultinomialVanilla();
    //private static NaiveBayesMultinomial mnb = new NaiveBayesMultinomial();
    /**
     * This is the Java code for a prequential evaluation
     * Connects to Controller.java and fetches Tweets from database
     */
    /**
     * The trainingStream that is generated from the database
     */
    static InstanceStream trainingStream;
    static ArrayList<ExtendedTweet> training;

    /**
     * Filter that converts the instances into tf*idf Vector representation
     */
    static StringToWordVector vector;

    // Variables for the time measurements
    double start1;
    private static final int EVALUATOR_WINDOW = 1000;
    private final static int OUTPUT_FREQUENCY = EVALUATOR_WINDOW;
    /*
    Classifies with prequential evaulation and returns accuracy of prediction
     */
    public static double classify(double decaydegree) throws Exception {
        WritableWorkbook workbook = Workbook.createWorkbook(new java.io.File("output-prequential-fading-preprocessing-day-" + decaydegree + "-" + EVALUATOR_WINDOW + ".xls"));
        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
        preprocessor = new Preprocessor();

//		Define new performance evaulator
        WindowClassificationPerformanceEvaluator evaluator = new WindowClassificationPerformanceEvaluator();
        evaluator.widthOption = new IntOption("width",
                'w', "Size of Window", EVALUATOR_WINDOW);
        //String dataset = "training2";
        String dataset = "tweetdb";
        String usr = "root";
        String pass = "";
        connection = new ExtendedDatabaseConnectionPreprocessed(dataset, usr, pass);
        //this is the arraylist of tweets
        training = connection.trainingSet;
        // transformation of the set into a stream for training
        trainingStream = new ExtendedBayesianStream(training);

        vector = ((ExtendedBayesianStream) trainingStream).getVector();
        //Prepare classifier for learning
        mnb.prepareForUse();
        //communicate the number of classes and attributes
        mnb.setModelContext(trainingStream.getHeader());
        mnb.setDecayDegree(decaydegree); //----IMPORTANT----
        //Count number of correctly classified tweets
        int numberSamplesCorrect = 0;
        int numberSamples = 0;
        int numInstances = 1599744;
        int evalSize = 80000;
        double[] evalResults = new double[numInstances/evalSize];
        int evalCounter = 0;
        int correctCounter = 0;
        int falsePositives = 0;
        int falseNegatives = 0;
        int truePositives = 0;
        int trueNegatives = 0;
        //Evaluation window
        int windowCount = OUTPUT_FREQUENCY;
        int lastRow = 0;
        double lastDay = 0; //last classified day
		/*while (trainingStream.hasMoreInstances() && numberSamples < numInstances) {
			Instance trainInst = trainingStream.nextInstance();
			mnb.trainOnInstance(trainInst);
			numberSamples++;
			if(mnb.correctlyClassifies(trainInst)){

				numberSamplesCorrect++;
		}*/
        while (trainingStream.hasMoreInstances()) {
            windowCount--;
            //Batch learning
            Instance trainInst = trainingStream.nextInstance();


            //System.out.println("Last observed weight in training stream: " + trainInst.weight());
            //There is the error! Weight is the week not the day....
            //System.out.println("Not the same day. That means this new instance receives a new fading factor!");
            double[] votes = mnb.getVotesForInstance(trainInst);
            //for (double d: votes)
            //    System.out.println(d);
            evaluator.addResult(trainInst, votes);
            if (mnb.correctlyClassifies(trainInst)) {
                if (votes[0] >= votes[1])
                    truePositives++;
                else
                    trueNegatives++;
                numberSamplesCorrect++;
                correctCounter++;
            } else {
                if (votes[0] >= votes[1])
                    falsePositives++;
                else
                    falseNegatives++;
            }
            if(windowCount == 0)
            {
                //Write evaluation results to excel
                double firstAccuracy = evaluator.getFractionCorrectlyClassified();
                double firstKappa = evaluator.getKappaStatistic();
                /*double secondAccuracy = evaluator2.getFractionCorrectlyClassified();
                double secondKappa = evaluator2.getKappaStatistic();
                double thirdAccuracy = evaluator3.getFractionCorrectlyClassified();
                double thirdKappa = evaluator3.getKappaStatistic();*/
                Number firstKappaCell = new Number(0, lastRow, firstKappa);
                Number firstAccuracyCell = new Number(1, lastRow, firstAccuracy);
                /*Number secondKappaCell = new Number(2, lastRow, secondKappa);
                Number secondAccuracyCell = new Number(3, lastRow, secondAccuracy);
                Number thirdKappaCell = new Number(4, lastRow, thirdKappa);
                Number thirdAccuracyCell = new Number(5, lastRow, thirdAccuracy);*/
                sheet.addCell(firstKappaCell);
                sheet.addCell(firstAccuracyCell);
                /*sheet.addCell(secondAccuracyCell);
                sheet.addCell(secondKappaCell);
                sheet.addCell(thirdAccuracyCell);
                sheet.addCell(thirdKappaCell);*/
                windowCount = OUTPUT_FREQUENCY;
                lastRow++;
            }
            numberSamples++;
            lastDay = trainInst.weight();
            //Add evaluation result
            /*if (numberSamples % evalSize == 0) {
                evalResults[evalCounter] = 100.0 * (double) correctCounter / (double) evalSize;
                evalCounter++;
                correctCounter = 0;
            }*/
            mnb.trainOnInstance(trainInst);
        }


        double accuracy = 100.0*(double) numberSamplesCorrect / (double) numberSamples;
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
        //Print evaluation results
        /*for(double d: evalResults)
            System.out.println(d);*/
        //Write results into excel sheet
        workbook.write();
        workbook.close();
        return accuracy;


    }


    public static void main(String[] args) throws Exception {
        double decay1 = 0.2;
        //double decay2 = 0.3;
        //double decay3 = 0.3;
        //Use different algorithm parameters and find optimal one
        double accuracy1 = classify(decay1);
        //double accuracy2 = classify(decay2);
        //double accuracy3 = classify(decay3);
        System.out.println("Preprocessed Fading Prequential Evaluation classified with decayDegree " + decay1 + " and accuracy " + accuracy1);
        //System.out.println("Classified with decayDegree " + decay2 + " and accuracy " + accuracy2);
        //System.out.println("Classified with decayDegree " + decay3 + " and accuracy " + accuracy3);

    }


}