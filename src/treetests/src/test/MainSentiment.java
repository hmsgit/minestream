/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dbaccess.DataFile;
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
import wrapper.MyASHoeffdingTree;
import wrapper.MyHoeffdingTree;

/**
 *
 * @author mahmud
 */
public class MainSentiment {
    static InstanceStream trainingStream;
    static ArrayList<Tweet> training;
    
    static StringToWordVector vector;
    
    
    public static void main(String[] args) throws Exception {
        
        DataFile rf = new DataFile("processed.training.1600k.csv", 0, "r");
        rf.read();
        training = rf.trainingSet;
        
//        Preprocessor pp = new Preprocessor();
//        for (int i = 0; i < training.size(); ++i) {
//            //System.out.println(training.get(i).getText());
//            String s = pp.proceed(training.get(i).getText());
//            training.get(i).setText(s);
//            if (i%1000 == 0) System.out.println(i+" ");
//        }
//        Collections.sort(training, new CustomComparator());
//        
//        DataFile wf = new DataFile("processed.training.1600k.csv", 0, "w");
//        wf.write(training);
//        System.out.println("SORTED!");
        
//        training = new dbaccess.DBConnection("tweetdb", "root", "").trainingSet;
        
        trainingStream = new BayesianStream(training);
//        while (trainingStream.hasMoreInstances()) {
//            System.out.println(trainingStream.nextInstance().toString());
//        }
        
        StringBuilder stats = new StringBuilder();
        //vector = ((BayesianStream) trainingStream).getVector();
        
        MyASHoeffdingTree classifier = new MyASHoeffdingTree();
        classifier.gracePeriodOption.setValue(400);
        classifier.tieThresholdOption.setValue(0.005f);
        classifier.binarySplitsOption.setValue(false);
        classifier.setMaxSize(512);
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
        
        System.out.println(stats.toString());
    }

    private static class CustomComparator implements Comparator<Tweet> {
        @Override
        public int compare(Tweet o1, Tweet o2) {
            return (int) (Long.parseLong(o1.getId()) - Long.parseLong(o2.getId()));
        }
    }
}
