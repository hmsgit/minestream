/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package format;

/**
 *
 * @author mahmud
 */
public class Data {
    public String dataset;
    public String algo;
    public String speed;
    public String centroid;
    public String driftcentroid;
    public String grace;
    public String tiethresh;
    public String binsplit;
    
    public String maxsize;
    public String ifreset;
    
    public String enssize;
    public String firsttree;
    
    public String count;
    public String time;
    public String tp, fp, tn, fn;
    public String accuracy;
    public String kappa;
    public String tempkappa;
    public String memory;
    
    public String depth;
    public String size;
    public String decisionnode;
    public String activeleaf;
    public String inactiveleaf;
    public String reset;
    public String alter;
    public String switching;
    public String prune;
    
    public String cop1;
    public String cop2;
    public String cop3;
    public String cop4;
    
    public Data() {
        dataset = "0";
        algo = "0";
        speed = "0";
        centroid = "0";
        driftcentroid = "0";
        grace = "0";
        tiethresh = "0";
        binsplit = "0";

        maxsize = "0";
        ifreset = "0";

        enssize = "0";

        count = "0";
        time = "0";
        tp = "0"; fp = "0"; tn = "0"; fn = "0";
        accuracy = "0";
        kappa = "0";
        tempkappa = "0";
        memory = "0";

        depth = "0";
        size = "0";
        decisionnode = "0";
        activeleaf = "0";
        inactiveleaf = "0";
        reset = "0";
        alter = "0";
        switching = "0";
        prune = "0";

        cop1 = "0";
        cop2 = "0";
        cop3 = "0";
        cop4 = "0";
    }
    
    @Override
    public String toString() {
        
        return dataset + ","+
            algo + ","+
            speed + ","+
            centroid + ","+
            driftcentroid + ","+
            grace + ","+
            tiethresh + ","+
            binsplit + ","+

            maxsize + ","+
            ifreset + ","+

            enssize + ","+
            firsttree + ","+

            count + ","+
            time + ","+
            tp + ","+ fp + ","+ tn + ","+ fn + ","+
            accuracy + ","+
            kappa + ","+
            tempkappa + ","+
            memory + ","+

            depth + ","+
            size + ","+
            decisionnode + ","+
            activeleaf + ","+
            inactiveleaf + ","+
            reset + ","+
            alter + ","+
            switching + ","+
            prune + ","+

            cop1 + ","+
            cop2 + ","+
            cop3 + ","+
            cop4;
    }
}
