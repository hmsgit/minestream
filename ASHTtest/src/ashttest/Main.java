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
import wrapper.MyHoeffdingAdaptiveTree;

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
        inStream.speedChangeOption.setValue(0.01f);
        
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
    private void testHTwithStackedRBF() {
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
    private void testASHTwithStackedRBF() {
        wrapper.StackedRBFStream inStream = new wrapper.StackedRBFStream(3);
        inStream.numClassesOption.setValue(6);
        inStream.setClassStartIndexOption(1, 3);
        inStream.setClassStartIndexOption(2, 5);
        
        inStream.setSpeedChangeOpiton(1, 0.01f);
        inStream.setSpeedChangeOpiton(2, 0.1f);
        inStream.prepareForUse();
        
        try {
            new ASHTtest().test(
                    inStream,
                    new wrapper.MyOzaBagASHT()
            );
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        //new Main().testHTwithStackedRBF();
        //new Main().testHTwithRandRBFDrift();
        //new Main().testHTwithRandRBF();
        
        new Main().testASHTwithStackedRBF();
    }
    
}
