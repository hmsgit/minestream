/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbaccess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 *
 * @author mahmud
 */

public class ReadFile {    
    private InputStream fis;
    private InputStreamReader isr;
    private BufferedReader br;
    public ArrayList<String []> trainingSet = new ArrayList<String[]>();

    public int classIndex;

    String rs = "";

    public ReadFile(String filename, int classIndex) throws IOException{
        fis = new FileInputStream(filename);
        isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        br = new BufferedReader(isr);
        this.classIndex = classIndex;
        
        generateDataSet();
        System.out.println("Generated dataset");
    }

    public final void generateDataSet() throws IOException {        
        while((rs = br.readLine()) != null){
            String attrs[] = rs.split(",");
            
            trainingSet.add(attrs);
         }
    }
}
