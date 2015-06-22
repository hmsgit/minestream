/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 *
 * @author mahmud
 */
public class Tweet {	
    String tweet;
    String type;
    String id;
    String user;
    String query;
    java.util.Date date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    double[] classificationValues;


    public String getText() {

        return tweet;
    }

    public void setText(String tweet) {
        this.tweet = tweet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public java.util.Date getDate() {
            return date;
    }
    public String getDateString() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

//    public void setDate(String date) {
//            this.date = date;
//    }
    public void setDate(java.util.Date date) {
        this.date = date;
    }
    
    @Override
    public String toString() {
        return "Class: " + type + ", tweet: " + tweet;
    }
}
