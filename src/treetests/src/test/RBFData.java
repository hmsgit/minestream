/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import weka.core.Instance;
import wrapper.generator.MyRandomRBFGeneratorDrift;

/**
 *
 * @author mahmud
 */
public class RBFData {
    
    public static void main(String[] args) {
        File file = new File("rbf.csv");
        StringBuilder stats = new StringBuilder();
        
        MyRandomRBFGeneratorDrift is = new MyRandomRBFGeneratorDrift();
        is.numClassesOption.setValue(2);
        is.instanceRandomSeedOption.setValue((int)System.nanoTime());
        is.modelRandomSeedOption.setValue((int)System.nanoTime());
        
        is.numAttsOption.setValue(2);
        is.numCentroidsOption.setValue(5);
        is.numDriftCentroidsOption.setValue(0);
        is.speedChangeOption.setValue(1);
        is.prepareForUse();
        
        stats.append("Centroid,X,Y,Class\n");
        for (int i = 0; i < 200; i++) {
            Instance x = is.nextInstance();
            String str = x.toString();
            //str = str.replace(",", " ");
            str = str.replace("class", "Class");
            stats.append("#" + (is._curCentroidIndex +1));
            stats.append(","+str+"\n");
            System.out.println(x.toString());
        }
        
        new MainRandRBF().writeStatsInFile(file, stats);
    }
}
