/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package format;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author mahmud
 */
public class Read {
    public ArrayList<Data> list = new ArrayList<>();
    
    public void read (String filename, int type) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String curline;

            int ln = 0;
            Data x = new Data();
            while ((curline = br.readLine()) != null) {
                String [] vals;
                curline = curline.replace("\t", ",");
                curline = curline.replace(",,", ",");
                vals = curline.split(",");
                if (type <= 1) {
                    switch (ln++) {
                        case 0: //name algo
                            x.dataset = "RandRBF";
                            if (type == 0) x.algo = "HT";
                            else if (type == 1) x.algo = "AdaHT";
                            break;
                        case 1:
                            x.speed = vals[1];
                            x.centroid = vals[3];
                            x.driftcentroid = vals[5];
                            break;
                        case 2:
                            x.grace = vals[1];
                            x.tiethresh = vals[3];
                            x.binsplit = vals[5];
                            break;
                        case 3: //title
                            break;

                        case 4: // first window
                        case 23: // last window
                        case 24: //space
                            break;
                        case 25:
                            x.count = vals[0]; x.time = vals[1];
                            x.tp = vals[2]; x.fn = vals[3]; x.tn = vals[4]; x.fp = vals[5];
                            x.accuracy = vals[6]; x.kappa = vals[7]; x.tempkappa = vals[8];
                            x.memory = vals[9]; x.depth = vals[10]; x.size = vals[11];
                            x.decisionnode = vals[12]; x.activeleaf = vals[13]; x.inactiveleaf = vals[14];
                            x.reset = vals[15]; x.alter = vals[16]; x.switching = vals[17]; x.prune = vals[18];
                            x.cop1 = vals[19]; x.cop2 = vals[20]; x.cop3 = vals[21]; x.cop4 = vals[22]; 
                            break;

                        case 30:
                            list.add(x);
                            ln = 0;
                            x = new Data();
                            break;
                        default:
                            break;
                    }
                    break;
                }
                else if (type > 1) {
                    switch (ln++) {
                        case 0: //name algo
                            x.dataset = "RandRBF";
                            if (type == 2) x.algo = "ASHT";
                            else if (type == 3) x.algo = "BagHT";
                            else if (type == 4) x.algo = "BagAdaHT";
                            else if (type == 5) x.algo = "BagAdwin";
                            else if (type == 6) x.algo = "BagASHT";
                            else if (type == 7) x.algo = "BagSRHT";
                            else if (type == 8) x.algo = "BoostHT";
                            else if (type == 9) x.algo = "BoostAdaHT";
                            break;
                        case 1:
                            x.speed = vals[1];
                            x.centroid = vals[3];
                            x.driftcentroid = vals[5];
                            break;
                        case 2:
                            x.grace = vals[1];
                            x.tiethresh = vals[3];
                            x.binsplit = vals[5];
                            break;
                        case 3:
                            if (type == 2) {
                                x.maxsize = vals[1];
                                x.ifreset = vals[3];
                            } else if (type == 6 || type ==7) {
                                x.firsttree = vals[3];
                                x.ifreset = vals[5];
                            }else {
                                x.enssize = vals[1];
                            }
                            break;

                        case 5: // first window
                        case 24: // last window
                        case 25: //space
                            break;
                        case 26:
                            x.count = vals[0]; x.time = vals[1];
                            x.tp = vals[2]; x.fn = vals[3]; x.tn = vals[4]; x.fp = vals[5];
                            x.accuracy = vals[6]; x.kappa = vals[7]; x.tempkappa = vals[8];

                            if (type != 3 && type != 4 && type != 8 && type != 9) {
                            x.memory = vals[9]; x.depth = vals[10]; x.size = vals[11];
                            x.decisionnode = vals[12]; x.activeleaf = vals[13]; x.inactiveleaf = vals[14];
                            x.reset = vals[15]; x.alter = vals[16]; x.switching = vals[17]; x.prune = vals[18];
                            x.cop1 = vals[19]; x.cop2 = vals[20]; x.cop3 = vals[21]; x.cop4 = vals[22]; 
                            }
                            break;

                        case 31:
                            list.add(x);
                            ln = 0;
                            x = new Data();
                            break;
                        default:
                            break;
                    }
                    break;
                }
                
                //System.out.println(curline);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }         
    }
}
