/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plotcsv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mahmud
 */
public class Plotcsv {
    /**
     * @param args the command line arguments
     */
    
    public DBConnection db;
    String location = "D:/Thesis/tex/tex/res/";
    
    public Plotcsv() {
        db = new DBConnection("tweetdb", "root", "");
    }
    
    public void writecsv(String filename, String head, String query) {
        StringBuilder towrt = new StringBuilder();
        File file = new File(filename);
        towrt.append(head).append("\n");
        towrt.append(db.execute(query));
        
        try {
            if (!file.exists())
                file.createNewFile();
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(towrt.toString());
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Plotcsv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        
        
//        new Plotcsv().windowaccu();
        
        new Plotcsv().speed();
        new Plotcsv().grace();
        new Plotcsv().others("centroid", 3);
        new Plotcsv().others("driftcentroid", 4);
        new Plotcsv().others("tiethresh", 5);
        new Plotcsv().others("binsplit", 6);
        
        
        new Plotcsv().speedx();
        new Plotcsv().gracex();
        new Plotcsv().othersx("centroid", 3);
        new Plotcsv().othersx("driftcentroid", 4);
        new Plotcsv().othersx("tiethresh", 5);
        new Plotcsv().othersx("binsplit", 6);
        
        new Plotcsv().maxsize();
        new Plotcsv().ensize();
        new Plotcsv().ifreset();
        new Plotcsv().firsttree();
        
        new Plotcsv().maxsizex();
        new Plotcsv().ensizex();
        new Plotcsv().ifresetx();
        new Plotcsv().firsttreex();
    }
    public void windowaccu() {
        // speed vs xyz
        writecsv(location + "1-rnd-count-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT count, algo, accuracy\n" +
                "FROM  reswin\n" +
                "WHERE dataset = 'VSRBF' AND speed = 0.01 AND grace = 200\n" +
                "GROUP BY algo, count"
        );

    }
    public void speed() {
        // speed vs xyz
        writecsv(location + "1-rnd-speed-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, accuracy FROM ressum WHERE dataset = 'RandRBF' AND grace = 200 GROUP BY algo, speed"
        );
        writecsv(location + "1-rnd-speed-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, time FROM ressum WHERE dataset = 'RandRBF' AND grace = 200 GROUP BY algo, speed"
        );
        writecsv(location + "1-rnd-speed-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, kappa FROM ressum WHERE dataset = 'RandRBF' AND grace = 200 GROUP BY algo, speed"
        );
        writecsv(location + "1-rnd-speed-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, memory FROM ressum WHERE dataset = 'RandRBF' AND grace = 200 GROUP BY algo, speed"
        );
        writecsv(location + "1-rnd-speed-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, depth FROM ressum WHERE dataset = 'RandRBF' AND grace = 200 GROUP BY algo, speed"
        );
        writecsv(location + "1-rnd-speed-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, tsize FROM ressum WHERE dataset = 'RandRBF' AND grace = 200 GROUP BY algo, speed"
        );
    }
    public void grace() {
        writecsv(location + "2-rnd-grace-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, accuracy FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        writecsv(location + "2-rnd-grace-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, time FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        writecsv(location + "2-rnd-grace-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, kappa FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        writecsv(location + "2-rnd-grace-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, memory FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        writecsv(location + "2-rnd-grace-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, depth FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        writecsv(location + "2-rnd-grace-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, tsize FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        
    }
    public void others(String what, int i) {
        writecsv(location + i+ "-rnd-"+ what +"-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, accuracy FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        writecsv(location + i+ "-rnd-" + what + "-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, time FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        writecsv(location + i+ "-rnd-" + what + "-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, kappa FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        writecsv(location + i+ "-rnd-" + what + "-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, memory FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        writecsv(location + i+ "-rnd-" + what + "-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, depth FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        writecsv(location + i+ "-rnd-" + what + "-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, tsize FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        
    }
    public void speedx() {
        // speed vs xyz
        writecsv(location + "1-vs-speed-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, accuracy FROM ressum WHERE dataset = 'VSRBF' AND grace = 200 GROUP BY algo, speed"
        );
        writecsv(location + "1-vs-speed-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, time FROM ressum WHERE dataset = 'VSRBF' AND grace = 200 GROUP BY algo, speed"
        );
        writecsv(location + "1-vs-speed-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, kappa FROM ressum WHERE dataset = 'VSRBF' AND grace = 200 GROUP BY algo, speed"
        );
        writecsv(location + "1-vs-speed-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, memory FROM ressum WHERE dataset = 'VSRBF' AND grace = 200 GROUP BY algo, speed"
        );
        writecsv(location + "1-vs-speed-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, depth FROM ressum WHERE dataset = 'VSRBF' AND grace = 200 GROUP BY algo, speed"
        );
        writecsv(location + "1-vs-speed-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT speed, algo, tsize FROM ressum WHERE dataset = 'VSRBF' AND grace = 200 GROUP BY algo, speed"
        );
    }
    public void gracex() {
        writecsv(location + "2-vs-grace-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, accuracy FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        writecsv(location + "2-vs-grace-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, time FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        writecsv(location + "2-vs-grace-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, kappa FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        writecsv(location + "2-vs-grace-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, memory FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        writecsv(location + "2-vs-grace-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, depth FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        writecsv(location + "2-vs-grace-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT grace, algo, tsize FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, grace"
        );
        
    }
    public void othersx(String what, int i) {
        writecsv(location + i+ "-vs-"+ what +"-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, accuracy FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        writecsv(location + i+ "-vs-" + what + "-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, time FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        writecsv(location + i+ "-vs-" + what + "-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, kappa FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        writecsv(location + i+ "-vs-" + what + "-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, memory FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        writecsv(location + i+ "-vs-" + what + "-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, depth FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        writecsv(location + i+ "-vs-" + what + "-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT " + what + ", algo, tsize FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, " + what + ""
        );
        
    }
    
    
    public void maxsize() {        
        writecsv(location + "7-rnd-maxsize-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, accuracy FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
        writecsv(location + "7-rnd-maxsize-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, time FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
        writecsv(location + "7-rnd-maxsize-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, kappa FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
        writecsv(location + "7-rnd-maxsize-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, memory FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
        writecsv(location + "7-rnd-maxsize-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, depth FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
        writecsv(location + "7-rnd-maxsize-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, tsize FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
    }
    public void ensize() {
        writecsv(location + "8-rnd-ensize-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, accuracy FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, ensize"
        );
        writecsv(location + "8-rnd-ensize-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, time FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, ensize"
        );
        writecsv(location + "8-rnd-ensize-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, kappa FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, ensize"
        );
        writecsv(location + "8-rnd-ensize-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, memory FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, ensize"
        );
        writecsv(location + "8-rnd-ensize-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, depth FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, ensize"
        );
        writecsv(location + "8-rnd-ensize-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, tsize FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, ensize"
        );
    }
    public void ifreset() {
        writecsv(location + "9-rnd-ifreset-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, accuracy FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
        writecsv(location + "9-rnd-ifreset-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, time FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
        writecsv(location + "9-rnd-ifreset-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, kappa FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
        writecsv(location + "9-rnd-ifreset-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, memory FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
        writecsv(location + "9-rnd-ifreset-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, depth FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
        writecsv(location + "9-rnd-ifreset-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, tsize FROM ressum WHERE dataset = 'RandRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
    }
    public void firsttree() {
        writecsv(location + "10-rnd-firsttree-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, accuracy FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, firsttree"
        );
        writecsv(location + "10-rnd-firsttree-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, time FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, firsttree"
        );
        writecsv(location + "10-rnd-firsttree-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, kappa FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, firsttree"
        );
        writecsv(location + "10-rnd-firsttree-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, memory FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, firsttree"
        );
        writecsv(location + "10-rnd-firsttree-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, depth FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, firsttree"
        );
        writecsv(location + "10-rnd-firsttree-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, tsize FROM ressum WHERE dataset = 'RandRBF' GROUP BY algo, firsttree"
        );
    }
        
    public void maxsizex() {        
        writecsv(location + "7-vs-maxsize-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, accuracy FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
        writecsv(location + "7-vs-maxsize-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, time FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
        writecsv(location + "7-vs-maxsize-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, kappa FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
        writecsv(location + "7-vs-maxsize-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, memory FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
        writecsv(location + "7-vs-maxsize-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, depth FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
        writecsv(location + "7-vs-maxsize-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT maxsize, algo, tsize FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, maxsize"
        );
    }
    public void ensizex() {
        writecsv(location + "8-vs-ensize-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, accuracy FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, ensize"
        );
        writecsv(location + "8-vs-ensize-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, time FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, ensize"
        );
        writecsv(location + "8-vs-ensize-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, kappa FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, ensize"
        );
        writecsv(location + "8-vs-ensize-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, memory FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, ensize"
        );
        writecsv(location + "8-vs-ensize-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, depth FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, ensize"
        );
        writecsv(location + "8-vs-ensize-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ensize, algo, tsize FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, ensize"
        );
    }
    public void ifresetx() {
        writecsv(location + "9-vs-ifreset-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, accuracy FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
        writecsv(location + "9-vs-ifreset-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, time FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
        writecsv(location + "9-vs-ifreset-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, kappa FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
        writecsv(location + "9-vs-ifreset-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, memory FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
        writecsv(location + "9-vs-ifreset-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, depth FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
        writecsv(location + "9-vs-ifreset-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT ifreset, algo, tsize FROM ressum WHERE dataset = 'VSRBF' AND speed = 0.01 GROUP BY algo, ifreset"
        );
    }
    public void firsttreex() {
        writecsv(location + "10-vs-firsttree-accu.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, accuracy FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, firsttree"
        );
        writecsv(location + "10-vs-firsttree-time.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, time FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, firsttree"
        );
        writecsv(location + "10-vs-firsttree-kappa.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, kappa FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, firsttree"
        );
        writecsv(location + "10-vs-firsttree-memory.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, memory FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, firsttree"
        );
        writecsv(location + "10-vs-firsttree-depth.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, depth FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, firsttree"
        );
        writecsv(location + "10-vs-firsttree-tsize.csv", 
                "X,Algorithm,value", 
                "SELECT DISTINCT firsttree, algo, tsize FROM ressum WHERE dataset = 'VSRBF' GROUP BY algo, firsttree"
        );
    }
}
