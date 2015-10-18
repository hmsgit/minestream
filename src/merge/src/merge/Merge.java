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
public class Merge {

    /**
     * @param args the command line arguments
     */
    
    
//                                if (type == 0) x.algo = "HT";
//                                else if (type == 1) x.algo = "AdaHT";
//                                else if (type == 2) x.algo = "ASHT";
    
//                                else if (type == 3) x.algo = "BagHT";
//                                else if (type == 4) x.algo = "BagAdaHT";
//                                else if (type == 5) x.algo = "BagAdwin";
//                                else if (type == 6) x.algo = "BagASHT";
//                                else if (type == 7) x.algo = "BagSRHT";
    
//                                else if (type == 8) x.algo = "BoostHT";
//                                else if (type == 9) x.algo = "BoostAdaHT";
    public static void main(String[] args) {
        Read r = new Read();
//        r.read("D:/Thesis/tex/src/merge/txt/ht-randrbf.txt", 0);
//        r.read("D:/Thesis/tex/src/merge/txt/adaht-randrbf.txt", 1);
//        r.read("D:/Thesis/tex/src/merge/txt/asht-randrbf.txt", 2);
//        
//        r.read("D:/Thesis/tex/src/merge/txt/ozabaght-randrbf.txt", 3);
//        r.read("D:/Thesis/tex/src/merge/txt/ozabagadaht-randrbf.txt", 4);
//        r.read("D:/Thesis/tex/src/merge/txt/ozabagadwin-randrbf.txt", 5);
//        r.read("D:/Thesis/tex/src/merge/txt/ozabagasht-randrbf.txt", 6);
//        r.read("D:/Thesis/tex/src/merge/txt/ozabagsrht-randrbf.txt", 7);
//        
//        r.read("D:/Thesis/tex/src/merge/txt/ozaboostht-randrbf.txt", 8);
//        r.read("D:/Thesis/tex/src/merge/txt/ozaboostadaht-randrbf.txt", 9);
        
        r.dtname = "VSRBF2";
        r.read("D:/Thesis/tex/src/merge/txt/ht-processedrbf.txt", 0);
        r.read("D:/Thesis/tex/src/merge/txt/adaht-processedrbf.txt", 1);
        r.read("D:/Thesis/tex/src/merge/txt/asht-processedrbf.txt", 2);
        
        r.read("D:/Thesis/tex/src/merge/txt/ozabaght-processedrbf.txt", 3);
        r.read("D:/Thesis/tex/src/merge/txt/ozabagadaht-processedrbf.txt", 4);
        r.read("D:/Thesis/tex/src/merge/txt/ozabagadwin-processedrbf.txt", 5);
        r.read("D:/Thesis/tex/src/merge/txt/ozabagasht-processedrbf.txt", 6);
        r.read("D:/Thesis/tex/src/merge/txt/ozabagsrht-processedrbf.txt", 7);
        
        r.read("D:/Thesis/tex/src/merge/txt/ozaboostht-processedrbf.txt", 8);
        r.read("D:/Thesis/tex/src/merge/txt/ozaboostadaht-processedrbf.txt", 9);
        
        for (Data d : r.list) 
            System.out.println(d.toString());
    }
    
}
