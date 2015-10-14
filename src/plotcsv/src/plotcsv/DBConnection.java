/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plotcsv;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mahmud
 */
public class DBConnection {
    private Connection connection;

    String start = "";
    java.util.Date date;

    ResultSet rs;

    PreparedStatement stmt;

    public DBConnection(String db, String user, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+db, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String execute(String query) {
        String toRet = "";
        try {
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                ResultSetMetaData rsmd = rs.getMetaData();
                int cols = rsmd.getColumnCount();
                
                for (int k = 1; k <= cols; k++) {
                    String s = rs.getString(k);
                    toRet += s;
                    if (k == cols) continue;
                    toRet += ",";
                }
                toRet += "\n";
             }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toRet;
    }

}
