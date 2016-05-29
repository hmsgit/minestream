/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import models.Tweet;

/**
 *
 * @author mahmud
 */

public class DataFile {
    private InputStream fis;
    private OutputStream fos;
    private InputStreamReader isr;
    private OutputStreamWriter osw;
    private BufferedReader br;
    private BufferedWriter bw;
    public ArrayList<Tweet> trainingSet = new ArrayList<Tweet>();

    public int classIndex;

    String rs = "";

    public DataFile(String filename, int classIndex, String mode) throws IOException{
        if (mode.equals("r")) {
            fis = new FileInputStream(filename);
            isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            this.classIndex = classIndex;
        } else {        
            fos = new FileOutputStream(filename);
            osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
            bw = new BufferedWriter(osw);
            this.classIndex = classIndex;
        }
    }
    
    public void read() throws IOException{        
        generateDataSet();
        System.out.println("Generated dataset");
    }
    
    public void write() throws IOException{        
        writeDataSet(trainingSet);
        System.out.println("Writing Done");
    }
    public void write(ArrayList<Tweet> trainingSet) throws IOException{        
        writeDataSet(trainingSet);
        System.out.println("Writing Done");
    }

    public final void generateDataSet() throws IOException {        
        while((rs = br.readLine()) != null){
            rs =rs.replace("\"", "");
            
            String attrs[] = rs.split(",");
            
            Tweet twt = new Tweet();
            for (int i = 0; i < attrs.length; i++) {
                switch(i) {
                    case 0:
                        twt.setType(attrs[i]);
                        break;
                    case 1:
                        twt.setId(attrs[i]);
                        break;
                    case 2:
                        twt.setDate(attrs[i]);
                        break;
                    case 3:
                        twt.setQuery(attrs[i]);
                        break;
                    case 4:
                        twt.setUser(attrs[i]);
                        break;
                    case 5:
                        twt.setText(attrs[i]);
                        break;
                    default:
                        break;
                }
            }
            trainingSet.add(twt);
         }
    }

    private void writeDataSet(ArrayList<Tweet> trainingSet) throws IOException{
        for (Tweet t : trainingSet) {
            bw.write(t.getType()+","+t.getId()+","+t.getDate()+","+t.getQuery()
                    +","+t.getUser()+","+t.getText()+"\n");
        }
        bw.flush();
    }
}
