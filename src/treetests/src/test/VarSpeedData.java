/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import weka.core.Instance;
import wrapper.generator.VarSpeedRBFGenerator;

/**
 *
 * @author mahmud
 */
public class VarSpeedData {
    
    public static void main(String[] args) {
        File file = new File("var.csv");
        StringBuilder stats = new StringBuilder();
        
        VarSpeedRBFGenerator is = new VarSpeedRBFGenerator();
        is.numClassesOption.setValue(2);
        is.instanceRandomSeedOption.setValue((int)System.nanoTime());
        is.modelRandomSeedOption.setValue((int)System.nanoTime());
        
        is.numAttsOption.setValue(2);
        is.numCentroidsOption.setValue(40);
        is.numInstancesToReconfig = 5;
        is.numDriftCentroidsOption.setValue(0);
        is.prepareForUse();
        
        stats.append("T,Pool,Centroid,X,Y,Class\n");
        for (int i = 0; i < 200; i++) {
            Instance x = is.nextInstance();
            String str = x.toString();
            //str = str.replace(",", " ");
            str = str.replace("class", "Class");
            stats.append("T" + (i/50 +1));
            stats.append(",#" + (is._curPoolIndex +1));
            stats.append("," + (is._curCentroidIndex +1));
            stats.append(","+str+"\n");
            System.out.println(x.toString());
        }
        
        new Main().writeStatsInFile(file, stats);
    }
}
