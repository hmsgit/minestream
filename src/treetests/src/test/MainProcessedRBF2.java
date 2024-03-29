/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import wrapper.*;
import wrapper.generator.*;

/**
 *
 * @author mahmud
 */


public class MainProcessedRBF2 {
    private void writeStatsInFile(File file, StringBuilder stats) {
        try {
            if (!file.exists())
                file.createNewFile();
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(stats.toString());
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String test_HT_RandRBF (double speedChange,
            int numCentroid,
            int numDrift,
            int gracePeriod,
            double tieThreshold,
            boolean binarySplit) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator();
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(speedChange);
        inStream.numCentroidsOption.setValue(numCentroid);
        inStream.numDriftCentroidsOption.setValue(numDrift);
        stats.append("HT-ProcessRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        MyHoeffdingTree classifier = new MyHoeffdingTree();
        classifier.gracePeriodOption.setValue(gracePeriod);
        classifier.tieThresholdOption.setValue(tieThreshold);
        classifier.binarySplitsOption.setValue(binarySplit);
        stats.append("GracePeriod:,\t").append(classifier.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(classifier.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(classifier.binarySplitsOption.isSet())
                .append("\n");
        
        System.out.println("Starting ... " + speedChange +" "+ numCentroid +" "+numDrift+
                " " + gracePeriod + " " + tieThreshold + " " + binarySplit);
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_HT_RandRBF() {
        File file = new File("ht-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            double speedChange,
//            int numCentroid,
//            int numDrift,
//            int gracePeriod,
//            double tieThreshold,
//            boolean binarySplit
        stats.append(test_HT_RandRBF(0,         100, 100, 200, .05, false));
        stats.append(test_HT_RandRBF(0.001,     100, 100, 200, .05, false));
        stats.append(test_HT_RandRBF(0.01,      100, 100, 200, .05, false));
        stats.append(test_HT_RandRBF(0.1,       100, 100, 200, .05, false));
        stats.append(test_HT_RandRBF(1,         100, 100, 200, .05, false));
        
        
//        stats.append(test_HT_RandRBF(0.01,  50,   50, 200, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  150, 100, 200, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  200, 100, 200, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_HT_RandRBF(0.01,  100,  20, 200, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  100,  40, 200, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  100,  60, 200, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  100,  80, 200, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 400, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 800, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 1600, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .001, false));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .005, false));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .01, false));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .05, false));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .05, true));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .05, true));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .05, true));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .05, true));
//        stats.append(test_HT_RandRBF(0.01,  100, 100, 200, .05, true));
        
        writeStatsInFile(file, stats);
    }
    
    private String test_AdaHT_RandRBF (double speedChange,
        int numCentroid,
        int numDrift,
        int gracePeriod,
        double tieThreshold,
        boolean binarySplit) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator();
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(speedChange);
        inStream.numCentroidsOption.setValue(numCentroid);
        inStream.numDriftCentroidsOption.setValue(numDrift);
        stats.append("AdaHT-ProcessRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        MyHoeffdingAdaptiveTree classifier = new MyHoeffdingAdaptiveTree();
        classifier.gracePeriodOption.setValue(gracePeriod);
        classifier.tieThresholdOption.setValue(tieThreshold);
        classifier.binarySplitsOption.setValue(binarySplit);
        stats.append("GracePeriod:,\t").append(classifier.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(classifier.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(classifier.binarySplitsOption.isSet())
                .append("\n");
        
        System.out.println("Starting ... " + speedChange +" "+ numCentroid +" "+numDrift+
                " " + gracePeriod + " " + tieThreshold + " " + binarySplit);
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_AdaHT_RandRBF() {
        File file = new File("adaht-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            double speedChange,
//            int numCentroid,
//            int numDrift,
//            int gracePeriod,
//            double tieThreshold,
//            boolean binarySplit
        stats.append(test_AdaHT_RandRBF(0,         100, 100, 200, .05, false));
        stats.append(test_AdaHT_RandRBF(0.001,     100, 100, 200, .05, false));
        stats.append(test_AdaHT_RandRBF(0.01,      100, 100, 200, .05, false));
        stats.append(test_AdaHT_RandRBF(0.1,       100, 100, 200, .05, false));
        stats.append(test_AdaHT_RandRBF(1,         100, 100, 200, .05, false));
        
        
//        stats.append(test_AdaHT_RandRBF(0.01,  50,   50, 200, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  150, 100, 200, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  200, 100, 200, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_AdaHT_RandRBF(0.01,  100,  20, 200, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100,  40, 200, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100,  60, 200, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100,  80, 200, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 400, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 800, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 1600, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .001, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .005, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .01, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .05, false));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .01, true));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .01, true));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .01, true));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .01, true));
//        stats.append(test_AdaHT_RandRBF(0.01,  100, 100, 200, .01, true));
        
        writeStatsInFile(file, stats);
    }
    
    private String test_ASHT_RandRBF (
        int maxSize,
        boolean reset) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator();
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(0.01);
        inStream.numCentroidsOption.setValue(100);
        inStream.numDriftCentroidsOption.setValue(100);
        stats.append("ASHT-ProcessRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        MyASHoeffdingTree classifier = new MyASHoeffdingTree();
        classifier.gracePeriodOption.setValue(200);
        classifier.tieThresholdOption.setValue(0.05f);
        classifier.binarySplitsOption.setValue(false);
        classifier.setMaxSize(maxSize);
        classifier.setResetTree(reset);
        stats.append("GracePeriod:,\t").append(classifier.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(classifier.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(classifier.binarySplitsOption.isSet())
                .append(",\t\nMaxSize:,\t").append(maxSize)
                .append(",\tReset:,\t").append(reset)
                .append("\n");
        
        System.out.println("Starting ... " + maxSize +" "+ reset );
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_ASHT_RandRBF() {
        File file = new File("asht-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            double maxSize,
//            boolean reset        
        stats.append(test_ASHT_RandRBF(1, true));
        stats.append(test_ASHT_RandRBF(2, true));
        stats.append(test_ASHT_RandRBF(4, true));
        stats.append(test_ASHT_RandRBF(8, true));
        stats.append(test_ASHT_RandRBF(16, true));        
        stats.append(test_ASHT_RandRBF(32, true));
        stats.append(test_ASHT_RandRBF(64, true));
        stats.append(test_ASHT_RandRBF(128, true));
        
        stats.append(test_ASHT_RandRBF(1, false));
        stats.append(test_ASHT_RandRBF(2, false));
        stats.append(test_ASHT_RandRBF(4, false));
        stats.append(test_ASHT_RandRBF(8, false));
        stats.append(test_ASHT_RandRBF(16, false));        
        stats.append(test_ASHT_RandRBF(32, false));
        stats.append(test_ASHT_RandRBF(64, false));
        stats.append(test_ASHT_RandRBF(128, false));
        
        writeStatsInFile(file, stats);
    }
    
    
    private String test_OzaBagHT_RandRBF (
        int ensSize,
        double speedChange,
        int numCentroid,
        int numDrift,
        int gracePeriod,
        double tieThreshold,
        boolean binarySplit) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator();
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(speedChange);
        inStream.numCentroidsOption.setValue(numCentroid);
        inStream.numDriftCentroidsOption.setValue(numDrift);
        stats.append("OzaBagHT-ProcessRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        MyOzaBag classifier = new MyOzaBag();
        MyHoeffdingTree base = new MyHoeffdingTree();
        base.gracePeriodOption.setValue(gracePeriod);
        base.tieThresholdOption.setValue(tieThreshold);
        base.binarySplitsOption.setValue(binarySplit);
        classifier.ensembleSizeOption.setValue(ensSize);
        classifier.baseLearnerOption.setCurrentObject(base);
        stats.append("GracePeriod:,\t").append(base.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(base.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(base.binarySplitsOption.isSet())
                .append(",\tBaseClassifier:,\t").append("HT")
                .append(",\t\nEnsembleSize:,\t").append(ensSize)
                .append("\n");
        
        System.out.println("Starting ... " + ensSize );
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_OzaBagHT_RandRBF() {
        File file = new File("ozabaght-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            int ensSize
//            double speedChange,
//            int numCentroid,
//            int numDrift,
//            int gracePeriod,
//            double tieThreshold,
//            boolean binarySplit
//        stats.append(test_OzaBagHT_RandRBF(1, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(2, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(3, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(4, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(6, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(7, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(8, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(9, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(10, 0, 100, 100, 200, .05, false));
//        
        
        stats.append(test_OzaBagHT_RandRBF(5, 0,         100, 100, 200, .05, false));
        stats.append(test_OzaBagHT_RandRBF(5, 0.001,     100, 100, 200, .05, false));
        stats.append(test_OzaBagHT_RandRBF(5, 0.01,      100, 100, 200, .05, false));
        stats.append(test_OzaBagHT_RandRBF(5, 0.1,       100, 100, 200, .05, false));
        stats.append(test_OzaBagHT_RandRBF(5, 1,         100, 100, 200, .05, false));
        
        
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  50,   50, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  150, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  200, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100,  20, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100,  40, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100,  60, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100,  80, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 400, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 800, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 1600, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .001, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .005, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .01, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        
        writeStatsInFile(file, stats);
    }
    
    private String test_OzaBagAdaHT_RandRBF (
        int ensSize,
        double speedChange,
        int numCentroid,
        int numDrift,
        int gracePeriod,
        double tieThreshold,
        boolean binarySplit) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator();
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(speedChange);
        inStream.numCentroidsOption.setValue(numCentroid);
        inStream.numDriftCentroidsOption.setValue(numDrift);
        stats.append("OzaBagAdaHT-ProcessRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        MyOzaBag classifier = new MyOzaBag();
        MyHoeffdingAdaptiveTree base = new MyHoeffdingAdaptiveTree();
        base.gracePeriodOption.setValue(gracePeriod);
        base.tieThresholdOption.setValue(tieThreshold);
        base.binarySplitsOption.setValue(binarySplit);
        classifier.ensembleSizeOption.setValue(ensSize);
        classifier.baseLearnerOption.setCurrentObject(base);
        stats.append("GracePeriod:,\t").append(base.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(base.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(base.binarySplitsOption.isSet())
                .append(",\tBaseClassifier:,\t").append("HT")
                .append(",\t\nEnsembleSize:,\t").append(ensSize)
                .append("\n");
        
        System.out.println("Starting ... " + ensSize );
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_OzaBagAdaHT_RandRBF() {
        File file = new File("ozabagadaht-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            int ensSize
//            double speedChange,
//            int numCentroid,
//            int numDrift,
//            int gracePeriod,
//            double tieThreshold,
//            boolean binarySplit
//        stats.append(test_OzaBagAdaHT_RandRBF(1, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(2, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(3, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(4, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(6, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(7, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(8, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(9, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(10, 0, 100, 100, 200, .05, false));
        
        
        stats.append(test_OzaBagAdaHT_RandRBF(5, 0,         100, 100, 200, .05, false));
        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.001,     100, 100, 200, .05, false));
        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,      100, 100, 200, .05, false));
        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.1,       100, 100, 200, .05, false));
        stats.append(test_OzaBagAdaHT_RandRBF(5, 1,         100, 100, 200, .05, false));
        
        
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  50,   50, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  150, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  200, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100,  20, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100,  40, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100,  60, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100,  80, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 400, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 800, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 1600, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .001, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .005, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
        
        writeStatsInFile(file, stats);
    }
    
    
    private String test_OzaBagASHT_RandRBF (
            int ensSize,
            int firstClassifierSize,
            boolean reset,
            double speedChange,
            int numCentroid,
            int numDrift,
            int gracePeriod,
            double tieThreshold,
            boolean binarySplit) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator();
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(speedChange);
        inStream.numCentroidsOption.setValue(numCentroid);
        inStream.numDriftCentroidsOption.setValue(numDrift);
        stats.append("OzaBagASHT-ProcessRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        MyOzaBagASHT classifier = new MyOzaBagASHT();
        MyASHoeffdingTree base = new MyASHoeffdingTree();
        base.gracePeriodOption.setValue(gracePeriod);
        base.tieThresholdOption.setValue(tieThreshold);
        base.binarySplitsOption.setValue(binarySplit);
        classifier.ensembleSizeOption.setValue(ensSize);
        classifier.resetTreesOption.setValue(reset);
        classifier.baseLearnerOption.setCurrentObject(base);
        stats.append("GracePeriod:,\t").append(base.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(base.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(base.binarySplitsOption.isSet())
                .append(",\tBaseClassifier:,\t").append("HT")
                .append(",\t\nEnsembleSize:,\t").append(ensSize)
                .append(",\tFirstClassifier:,\t").append(firstClassifierSize)
                .append(",\tReset:,\t").append(reset)
                .append("\n");
        
        System.out.println("Starting ... " + ensSize + " " + firstClassifierSize + " " + reset);
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_OzaBagASHT_RandRBF() {
        File file = new File("ozabagasht-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            int ensSize
//            int firstClassifierSize,
//            boolean reset,
//            double speedChange,
//            int numCentroid,
//            int numDrift,
//            int gracePeriod,
//            double tieThreshold,
//            boolean binarySplit
//        stats.append(test_OzaBagASHT_RandRBF(1, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(2, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(3, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(4, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(6, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(7, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(8, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(9, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(10, 2, true, 0, 100, 100, 200, .05, false));
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 1, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 3, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 4, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 5, true, 0, 100, 100, 200, .05, false));
//        
//        stats.append(test_OzaBagASHT_RandRBF(1, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(2, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(3, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(4, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(6, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(7, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(8, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(9, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(10, 2, false, 0, 100, 100, 200, .05, false));
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 1, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 3, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 4, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 5, false, 0, 100, 100, 200, .05, false));
        
        
        
        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0,         100, 100, 200, .05, false));
        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.001,     100, 100, 200, .05, false));
        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,      100, 100, 200, .05, false));
        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.1,       100, 100, 200, .05, false));
        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 1,         100, 100, 200, .05, false));
        
        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  50,   50, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  150, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  200, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100,  20, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100,  40, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100,  60, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100,  80, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 400, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 800, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 1600, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .001, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .005, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, true));
//        
//        
//        
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0,         100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.001,     100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,      100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.1,       100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 1,         100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  50,   50, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  150, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  200, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100,  20, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100,  40, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100,  60, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100,  80, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 400, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 800, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 1600, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .001, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .005, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagASHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, true));
        
        writeStatsInFile(file, stats);
    }
    
    
    private String test_OzaBagAdwin_RandRBF (
            int ensSize,
            double speedChange,
            int numCentroid,
            int numDrift,
            int gracePeriod,
            double tieThreshold,
            boolean binarySplit) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator();
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(speedChange);
        inStream.numCentroidsOption.setValue(numCentroid);
        inStream.numDriftCentroidsOption.setValue(numDrift);
        stats.append("OzaBagAdwin-ProcessRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        MyOzaBagAdwin classifier = new MyOzaBagAdwin();
        MyHoeffdingTree base = new MyHoeffdingTree();
        base.gracePeriodOption.setValue(gracePeriod);
        base.tieThresholdOption.setValue(tieThreshold);
        base.binarySplitsOption.setValue(binarySplit);
        classifier.ensembleSizeOption.setValue(ensSize);
        classifier.baseLearnerOption.setCurrentObject(base);
        stats.append("GracePeriod:,\t").append(base.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(base.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(base.binarySplitsOption.isSet())
                .append(",\tBaseClassifier:,\t").append("HT")
                .append(",\t\nEnsembleSize:,\t").append(ensSize)
                .append("\n");
        
        System.out.println("Starting ... " + ensSize + " "  + " " );
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_OzaBagAdwin_RandRBF() {
        File file = new File("ozabagadwin-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            int ensSize
//            int firstClassifierSize,
//            boolean reset,
//            double speedChange,
//            int numCentroid,
//            int numDrift,
//            int gracePeriod,
//            double tieThreshold,
//            boolean binarySplit
//        stats.append(test_OzaBagAdwin_RandRBF(1, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(2, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(3, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(4, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(6, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(7, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(8, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(9, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(10, 0, 100, 100, 200, .05, false));
//        
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0, 100, 100, 200, .05, false));
        
        stats.append(test_OzaBagAdwin_RandRBF(5, 0,         100, 100, 200, .05, false));
        stats.append(test_OzaBagAdwin_RandRBF(5, 0.001,     100, 100, 200, .05, false));
        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,      100, 100, 200, .05, false));
        stats.append(test_OzaBagAdwin_RandRBF(5, 0.1,       100, 100, 200, .05, false));
        stats.append(test_OzaBagAdwin_RandRBF(5, 1,         100, 100, 200, .05, false));
        
        
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  50,   50, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  150, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  200, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100,  20, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100,  40, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100,  60, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100,  80, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 400, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 800, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 1600, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .001, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .005, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, true));
        
        writeStatsInFile(file, stats);
    }
    
    
    private String test_OzaBoostHT_RandRBF (
            int ensSize,
            double speedChange,
            int numCentroid,
            int numDrift,
            int gracePeriod,
            double tieThreshold,
            boolean binarySplit) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator();
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(speedChange);
        inStream.numCentroidsOption.setValue(numCentroid);
        inStream.numDriftCentroidsOption.setValue(numDrift);
        stats.append("OzaBoostHT-ProcessRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        MyOzaBoost classifier = new MyOzaBoost();
        MyHoeffdingTree base = new MyHoeffdingTree();
        base.gracePeriodOption.setValue(gracePeriod);
        base.tieThresholdOption.setValue(tieThreshold);
        base.binarySplitsOption.setValue(binarySplit);
        classifier.ensembleSizeOption.setValue(ensSize);
        classifier.baseLearnerOption.setCurrentObject(base);
        stats.append("GracePeriod:,\t").append(base.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(base.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(base.binarySplitsOption.isSet())
                .append(",\tBaseClassifier:,\t").append("HT")
                .append(",\t\nEnsembleSize:,\t").append(ensSize)
                .append("\n");
        
        System.out.println("Starting ... " + ensSize + " "  + " " );
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_OzaBoostHT_RandRBF() {
        File file = new File("ozaboostht-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            int ensSize
//            int firstClassifierSize,
//            boolean reset,
//            double speedChange,
//            int numCentroid,
//            int numDrift,
//            int gracePeriod,
//            double tieThreshold,
//            boolean binarySplit
//        stats.append(test_OzaBoostHT_RandRBF(1, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(2, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(3, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(4, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(6, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(7, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(8, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(9, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(10, 0, 100, 100, 200, .05, false));
        
        
        stats.append(test_OzaBoostHT_RandRBF(5, 0,         100, 100, 200, .05, false));
        stats.append(test_OzaBoostHT_RandRBF(5, 0.001,     100, 100, 200, .05, false));
        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,      100, 100, 200, .05, false));
        stats.append(test_OzaBoostHT_RandRBF(5, 0.1,       100, 100, 200, .05, false));
        stats.append(test_OzaBoostHT_RandRBF(5, 1,         100, 100, 200, .05, false));
        
        
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  50,   50, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  150, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  200, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100,  20, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100,  40, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100,  60, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100,  80, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 400, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 800, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 1600, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .001, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .005, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .01, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
        
        writeStatsInFile(file, stats);
    }
    
    
    private String test_OzaBoostAdaHT_RandRBF (
            int ensSize,
            double speedChange,
            int numCentroid,
            int numDrift,
            int gracePeriod,
            double tieThreshold,
            boolean binarySplit) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator();
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(speedChange);
        inStream.numCentroidsOption.setValue(numCentroid);
        inStream.numDriftCentroidsOption.setValue(numDrift);
        stats.append("OzaBoostAdaHT-ProcessRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        MyOzaBoost classifier = new MyOzaBoost();
        MyHoeffdingAdaptiveTree base = new MyHoeffdingAdaptiveTree();
        base.gracePeriodOption.setValue(gracePeriod);
        base.tieThresholdOption.setValue(tieThreshold);
        base.binarySplitsOption.setValue(binarySplit);
        classifier.ensembleSizeOption.setValue(ensSize);
        classifier.baseLearnerOption.setCurrentObject(base);
        stats.append("GracePeriod:,\t").append(base.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(base.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(base.binarySplitsOption.isSet())
                .append(",\tBaseClassifier:,\t").append("HT")
                .append(",\t\nEnsembleSize:,\t").append(ensSize)
                .append("\n");
        
        System.out.println("Starting ... " + ensSize + " "  + " " );
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_OzaBoostAdaHT_RandRBF() {
        File file = new File("ozaboostadaht-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            int ensSize
//            int firstClassifierSize,
//            boolean reset,
//            double speedChange,
//            int numCentroid,
//            int numDrift,
//            int gracePeriod,
//            double tieThreshold,
//            boolean binarySplit
//        stats.append(test_OzaBoostAdaHT_RandRBF(1, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(2, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(3, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(4, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(6, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(7, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(8, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(9, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(10, 0, 100, 100, 200, .05, false));
        
        
        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0,         100, 100, 200, .05, false));
        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.001,     100, 100, 200, .05, false));
        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,      100, 100, 200, .05, false));
        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.1,       100, 100, 200, .05, false));
        stats.append(test_OzaBoostAdaHT_RandRBF(5, 1,         100, 100, 200, .05, false));
        
        
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  50,   50, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  150, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  200, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100,  20, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100,  40, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100,  60, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100,  80, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 400, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 800, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 1600, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .001, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .005, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostAdaHT_RandRBF(5, 0.01,  100, 100, 200, .01, true));
        
        writeStatsInFile(file, stats);
    }
    
    
    
    private String test_OzaBoostAdwin_RandRBF (
            int ensSize,
            double speedChange,
            int numCentroid,
            int numDrift,
            int gracePeriod,
            double tieThreshold,
            boolean binarySplit) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator();
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(speedChange);
        inStream.numCentroidsOption.setValue(numCentroid);
        inStream.numDriftCentroidsOption.setValue(numDrift);
        stats.append("OzaBoostAdwin-ProcessRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        MyOzaBoostAdwin classifier = new MyOzaBoostAdwin();
        MyHoeffdingTree base = new MyHoeffdingTree();
        base.gracePeriodOption.setValue(gracePeriod);
        base.tieThresholdOption.setValue(tieThreshold);
        base.binarySplitsOption.setValue(binarySplit);
        classifier.ensembleSizeOption.setValue(ensSize);
        classifier.baseLearnerOption.setCurrentObject(base);
        stats.append("GracePeriod:,\t").append(base.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(base.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(base.binarySplitsOption.isSet())
                .append(",\tBaseClassifier:,\t").append("HT")
                .append(",\t\nEnsembleSize:,\t").append(ensSize)
                .append("\n");
        
        System.out.println("Starting ... " + ensSize + " "  + " " );
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainProcessedRBF2.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_OzaBoostAdwin_RandRBF() {
        File file = new File("ozaboostadwin-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            int ensSize
//            int firstClassifierSize,
//            boolean reset,
//            double speedChange,
//            int numCentroid,
//            int numDrift,
//            int gracePeriod,
//            double tieThreshold,
//            boolean binarySplit
//        stats.append(test_OzaBoostAdwin_RandRBF(1, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(2, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(3, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(4, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(6, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(7, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(8, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(9, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(10, 0, 100, 100, 200, .05, false));
        
        
        stats.append(test_OzaBoostAdwin_RandRBF(5, 0,         100, 100, 200, .05, false));
        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.001,     100, 100, 200, .05, false));
        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,      100, 100, 200, .05, false));
        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.1,       100, 100, 200, .05, false));
        stats.append(test_OzaBoostAdwin_RandRBF(5, 1,         100, 100, 200, .05, false));
        
        
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  50,   50, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  150, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  200, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100,  20, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100,  40, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100,  60, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100,  80, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 400, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 800, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 1600, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .001, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .005, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBoostAdwin_RandRBF(5, 0.01,  100, 100, 200, .01, true));
//        
        writeStatsInFile(file, stats);
    }
    
    private String test_OzaBagSRHT_RandRBF (
            int ensSize,
            int firstClassifierSize,
            boolean reset,
            double speedChange,
            int numCentroid,
            int numDrift,
            int gracePeriod,
            double tieThreshold,
            boolean binarySplit) {
        
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator inStream = new VarSpeedRBFGenerator(300, 5, 1000);
        inStream.speedChangeOption.setValue(0.1);
        
        inStream.prepareForUse();
        inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        inStream.speedChangeOption.setValue(speedChange);
        inStream.numCentroidsOption.setValue(numCentroid);
        inStream.numDriftCentroidsOption.setValue(numDrift);
        stats.append("OzaBagSRHT-RandomRBF\n")
                .append("Speed Change:,\t").append(inStream.speedChangeOption.getValue())
                .append(",\tCentroids:,\t").append(inStream.numCentroidsOption.getValue())
                .append(",\tDriftCentroid:\t").append(inStream.numDriftCentroidsOption.getValue())
                .append("\n");
        
        CoBagSRHT classifier = new CoBagSRHT();
        SizeRestrictedHT base = new SizeRestrictedHT();
        base.gracePeriodOption.setValue(gracePeriod);
        base.tieThresholdOption.setValue(tieThreshold);
        base.binarySplitsOption.setValue(binarySplit);
        classifier.ensembleSizeOption.setValue(ensSize);
        classifier.resetTreesOption.setValue(reset);
        classifier.baseLearnerOption.setCurrentObject(base);
        stats.append("GracePeriod:,\t").append(base.gracePeriodOption.getValue())
                .append(",\tTieThreshold:,\t").append(base.tieThresholdOption.getValue())
                .append(",\tBinarySplit:,\t").append(base.binarySplitsOption.isSet())
                .append(",\tBaseClassifier:,\t").append("HT")
                .append(",\t\nEnsembleSize:,\t").append(ensSize)
                .append(",\tFirstClassifier:,\t").append(firstClassifierSize)
                .append(",\tReset:,\t").append(reset)
                .append("\n");
        
        System.out.println("Starting ... " + ensSize + " " + firstClassifierSize + " " + reset);
        try {
            new RunTest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier,
                    stats
            );
        } catch (Exception ex) {
            Logger.getLogger(MainRandRBF.class.getName()).log(Level.SEVERE, null, ex);
        }
        stats.append("\n\n\n\n");
        return stats.toString();
    }
    
    private void test_OzaBagSRHT_RandRBF() {
        File file = new File("ozabagsrht-processedrbf.txt");
        
        StringBuilder stats = new StringBuilder();
//            int ensSize
//            int firstClassifierSize,
//            boolean reset,
//            double speedChange,
//            int numCentroid,
//            int numDrift,
//            int gracePeriod,
//            double tieThreshold,
//            boolean binarySplit
//        stats.append(test_OzaBagSRHT_RandRBF(1, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(2, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(3, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(4, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(6, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(7, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(8, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(9, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(10, 2, true, 0, 100, 100, 200, .05, false));
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 1, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 3, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 4, true, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 5, true, 0, 100, 100, 200, .05, false));
//        
//        stats.append(test_OzaBagSRHT_RandRBF(1, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(2, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(3, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(4, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(6, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(7, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(8, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(9, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(10, 2, false, 0, 100, 100, 200, .05, false));
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 1, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 3, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 4, false, 0, 100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 5, false, 0, 100, 100, 200, .05, false));
        
        
        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0,         100, 100, 200, .05, false));
        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.001,     100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,      100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.1,       100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 1,         100, 100, 200, .05, false));
        
        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  50,   50, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  150, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  200, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100,  20, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100,  40, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100,  60, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100,  80, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 400, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 800, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 1600, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .001, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .005, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, true, 0.01,  100, 100, 200, .01, true));
//        
//        
//        
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0,         100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.001,     100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,      100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.1,       100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 1,         100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  50,   50, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  150, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  200, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  250, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100,  20, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100,  40, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100,  60, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100,  80, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .05, false));
//        
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 400, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 800, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 1600, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 3200, .05, false));
//        
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .001, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .005, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .05, false));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .1, false));
//        
//        
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, true));
//        stats.append(test_OzaBagSRHT_RandRBF(5, 2, false, 0.01,  100, 100, 200, .01, true));
        
        writeStatsInFile(file, stats);
    }
    
    


    
    
    
    public static void main(String[] args) {
//        new MainProcessedRBF2().test_HT_RandRBF();
//        new MainProcessedRBF2().test_AdaHT_RandRBF();
//        new MainProcessedRBF2().test_ASHT_RandRBF();
//        
//        new MainProcessedRBF2().test_OzaBagHT_RandRBF();
//        new MainProcessedRBF2().test_OzaBagAdaHT_RandRBF();
//        new MainProcessedRBF2().test_OzaBagASHT_RandRBF();
//        new MainProcessedRBF2().test_OzaBagAdwin_RandRBF();
////        
//        new MainProcessedRBF2().test_OzaBoostHT_RandRBF();
//        new MainProcessedRBF2().test_OzaBoostAdaHT_RandRBF();
//        new MainProcessedRBF2().test_OzaBoostAdwin_RandRBF();
        
        
        new MainProcessedRBF2().test_OzaBagSRHT_RandRBF();
    }
    
}
