/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merge;

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
    public Data(Data x) {
        dataset = x.dataset;
        algo = x.algo;
        speed = x.speed;
        centroid = x.centroid;
        driftcentroid = x.driftcentroid;
        grace = x.grace;
        tiethresh = x.tiethresh;
        binsplit = x.binsplit;

        maxsize = x.maxsize;
        ifreset = x.ifreset;

        enssize = x.enssize;

        count = x.count;
        time = x.time;
        tp = x.tp; fp = x.fp; tn = x.tn; fn = x.fn;
        accuracy = x.accuracy;
        kappa = x.kappa;
        tempkappa = x.tempkappa;
        memory = x.memory;

        depth = x.depth;
        size = x.size;
        decisionnode = x.decisionnode;
        activeleaf = x.activeleaf;
        inactiveleaf = x.inactiveleaf;
        reset = x.reset;
        alter = x.alter;
        switching = x.switching;
        prune = x.prune;

        cop1 = x.cop1;
        cop2 = x.cop2;
        cop3 = x.cop3;
        cop4 = x.cop4;
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
