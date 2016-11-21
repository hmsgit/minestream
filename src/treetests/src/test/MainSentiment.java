/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dbaccess.DataFile;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import moa.streams.InstanceStream;
import models.BayesianStream;
import models.Tweet;
import util.Preprocessor;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.gui.explorer.PreprocessPanel;
import wrapper.CoBagSRHT;
import wrapper.MyASHoeffdingTree;
import wrapper.MyHoeffdingTree;
import wrapper.MyOzaBagASHT;
import wrapper.SizeRestrictedHT;

/**
 *
 * @author mahmud
 */
public class MainSentiment {
    static InstanceStream trainingStream;
    static ArrayList<Tweet> training;
    
    static StringToWordVector vector;
    
    public void writeStatsInFile(File file, StringBuilder stats) {
        try {
            if (!file.exists())
                file.createNewFile();
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(stats.toString());
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(MainRandRBF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void test_ASHT() {
        File file = new File("asht-s160.txt");        
        StringBuilder stats = new StringBuilder();
        //vector = ((BayesianStream) trainingStream).getVector();
        
        MyASHoeffdingTree classifier = new MyASHoeffdingTree();
        classifier.gracePeriodOption.setValue(400);
        classifier.tieThresholdOption.setValue(0.005f);
        classifier.binarySplitsOption.setValue(false);
        classifier.setMaxSize(8);
        classifier.setResetTree(true);
        
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    trainingStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainRandRBF.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        writeStatsInFile(file, stats);
        System.out.println(stats.toString());
    }
    
    private void test_BagSRHT() {
        File file = new File("bagsrht-s160.txt");        
        StringBuilder stats = new StringBuilder();
        //vector = ((BayesianStream) trainingStream).getVector();
        
        CoBagSRHT classifier = new CoBagSRHT();
        SizeRestrictedHT base = new SizeRestrictedHT();
        base.gracePeriodOption.setValue(400);
        base.tieThresholdOption.setValue(0.005f);
        base.binarySplitsOption.setValue(false);
        classifier.ensembleSizeOption.setValue(3);
        classifier.resetTreesOption.setValue(false);
        classifier.baseLearnerOption.setCurrentObject(base);
        
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    trainingStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainRandRBF.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        writeStatsInFile(file, stats);
        System.out.println(stats.toString());
    }
    
    
    private void test_BagASHT() {
        File file = new File("bagasht-s160.txt");        
        StringBuilder stats = new StringBuilder();
        
        MyOzaBagASHT classifier = new MyOzaBagASHT();
        MyASHoeffdingTree base = new MyASHoeffdingTree();
        base.gracePeriodOption.setValue(400);
        base.tieThresholdOption.setValue(0.005f);
        base.binarySplitsOption.setValue(false);
        classifier.ensembleSizeOption.setValue(3);
        classifier.resetTreesOption.setValue(false);
        classifier.baseLearnerOption.setCurrentObject(base);
        
        try {
            new RunTest().test(
                    trainingStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainRandRBF.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        writeStatsInFile(file, stats);
        System.out.println(stats.toString());
    }
    
    
    public static void main(String[] args) throws Exception {
        
//        DataFile rf = new DataFile("processed.training.1600k.csv", 0, "r");
//        rf.read();
//        training = rf.trainingSet;
        
//        Preprocessor pp = new Preprocessor();
//        for (int i = 0; i < training.size(); ++i) {
//            //System.out.println(training.get(i).getText());
//            String s = pp.proceed(training.get(i).getText());
//            training.get(i).setText(s);
//            if (i%1000 == 0) System.out.println(i+" ");
//        }
//        Collections.sort(training, new CustomComparator());//        
//        DataFile wf = new DataFile("processed.training.1600k.csv", 0, "w");
//        wf.write(training);
//        System.out.println("SORTED!");
        
        training = new dbaccess.DBConnection("tweetdb", "root", "").trainingSet;
        
        trainingStream = new BayesianStream(training);
        
//        while (trainingStream.hasMoreInstances()) {
//            System.out.println(trainingStream.nextInstance().toString());
//        }
        
        if (args.length == 0) {
            System.out.println("all");
            new MainSentiment().test_BagASHT();
            training = new dbaccess.DBConnection("tweetdb", "root", "").trainingSet;
            trainingStream = new BayesianStream(training);
            new MainSentiment().test_BagSRHT();
            training = new dbaccess.DBConnection("tweetdb", "root", "").trainingSet;
            trainingStream = new BayesianStream(training);
            new MainSentiment().test_ASHT();
        } else if (args[0].equals("1")) {
            System.out.println("1");
            new MainSentiment().test_BagASHT();
        } else if (args[0].equals("2")) {
            System.out.println("2");
            new MainSentiment().test_BagSRHT();
        } else if (args[0].equals("3")) {
            System.out.println("3");
            new MainSentiment().test_ASHT();
        }
    }

    private static class CustomComparator implements Comparator<Tweet> {
        @Override
        public int compare(Tweet o1, Tweet o2) {
            return (int) (Long.parseLong(o1.getId()) - Long.parseLong(o2.getId()));
        }
    }
}
