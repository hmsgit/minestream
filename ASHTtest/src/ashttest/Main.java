/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ashttest;

import java.util.logging.Level;
import java.util.logging.Logger;
import moa.classifiers.meta.OzaBagASHT;
import moa.streams.generators.RandomRBFGeneratorDrift;
import wrapper.*;

/**
 *
 * @author mahmud
 */

public class Main {
    private void testHTwithRandRBF() {
        RandomRBFGeneratorDrift inStream = new RandomRBFGeneratorDrift();
        inStream.prepareForUse();
        inStream.numDriftCentroidsOption.setValue(0);
        
        try {
            new HTtest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    new  MyHoeffdingAdaptiveTree()
            );
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void testHTwithRandRBFDrift() {
        RandomRBFGeneratorDrift inStream = new RandomRBFGeneratorDrift();
        inStream.prepareForUse();
        inStream.speedChangeOption.setValue(.01f);
        inStream.numCentroidsOption.setValue(140);
        //inStream.modelRandomSeedOption.setValue((int)System.nanoTime());
        //inStream.instanceRandomSeedOption.setValue((int)System.nanoTime());
        
        MyHoeffdingTree classifier = new MyASHoeffdingTree();
        ((MyASHoeffdingTree)classifier).setMaxSize(10);
        ((MyASHoeffdingTree)classifier).setResetTree();
                
        try {
            new ASHTtest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    classifier
            );
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    private void testHTwithProcessedRBF() {
        wrapper.MyRandomRBFGeneratorDrift inStream = new wrapper.MyRandomRBFGeneratorDrift(100,5,4000);
        inStream.prepareForUse();
        inStream.numDriftCentroidsOption.setValue(0);
        
        try {
            new HTtest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    new  MyHoeffdingAdaptiveTree()
            );
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void testHTwithProcessedRBFDrift() {
        wrapper.MyRandomRBFGeneratorDrift inStream = new wrapper.MyRandomRBFGeneratorDrift(300,5,2000);
        inStream.prepareForUse();
        inStream.speedChangeOption.setValue(1f);
        
        try {
            new HTtest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    new  MyHoeffdingAdaptiveTree()
            );
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void testASHTwithRandRBF() {
        wrapper.StackedRBFStream inStream = new wrapper.StackedRBFStream(3);
        inStream.numClassesOption.setValue(6);
        inStream.setClassStartIndexOption(1, 3);
        inStream.setClassStartIndexOption(2, 5);
        
        inStream.setSpeedChangeOpiton(1, 0.01f);
        inStream.setSpeedChangeOpiton(2, 0.1f);
        inStream.prepareForUse();
        
        try {
            new HTtest().test(
                    //new moa.streams.ArffFileStream("cluster.csv", -1),
                    inStream,
                    new  MyHoeffdingAdaptiveTree()
            );
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void testASHTwithRandRBFDrift() {
        RandomRBFGeneratorDrift inStream = new RandomRBFGeneratorDrift();
        inStream.prepareForUse();
        inStream.speedChangeOption.setValue(.01f);
        inStream.numCentroidsOption.setValue(140);
        
        MyOzaBagASHT classifier = new MyOzaBagASHT();
        classifier.firstClassifierSizeOption.setValue(2);
        classifier.ensembleSizeOption.setValue(3);
        //classifier.resetTreesOption.setValue(true);
        
        try {
            new ASHTtest().test(
                    inStream,
                    classifier
            );
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        //new Main().testHTwithRandRBF();
        
        new Main().testHTwithRandRBFDrift();
        //new Main().testHTwithProcessedRBFDrift();
        new Main().testASHTwithRandRBFDrift();
    }
    
}
