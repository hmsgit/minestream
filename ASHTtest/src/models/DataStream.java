/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import moa.streams.ArffFileStream;

/**
 *
 * @author mahmud
 */
public class DataStream extends ArffFileStream {

    public DataStream(String filename, int classIndex) {
        super(filename, classIndex);
    }

}
