/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbaccess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import models.Tweet;

/**
 *
 * @author mahmud
 */
public class DBConnection {
    private Connection connection;
    public ArrayList<Tweet> trainingSet = new ArrayList<Tweet>();

    PreparedStatement statement;
    String start = "";
    java.util.Date date;

    ResultSet rs;

    PreparedStatement stmt;

    public DBConnection(String db, String user, String password) throws SQLException, ClassNotFoundException, IOException{
            Class.forName("com.mysql.jdbc.Driver");
            
        connection = DriverManager.getConnection("jdbc:mysql://localhost/"+db, user, password);

        stmt = connection.prepareStatement("SELECT sentiment, minimalText, STR_TO_DATE(date, '%W %M %d %T PDT %Y') AS stamp "
                + "FROM tweets ORDER BY stamp ASC "
                + "LIMIT 0, 20000"
        );
        rs = stmt.executeQuery();
        System.out.println("Executed SQL query");

        this.generateDataSet();
        System.out.println("Generated dataset");
    }

    public void generateDataSet() 
        throws SQLException, IOException {
        
        int i = 0;
        while(	rs.next()){
                String type = rs.getString(1);

                String text = rs.getString(2);

		date = util.Utilx.convertToUtilDate(rs.getDate(3));

                Tweet tweet = new Tweet();
                tweet.setText(text);
                tweet.setType(type);
		tweet.setDate(date);

                trainingSet.add(tweet);
                i++;
         }
    }
}
