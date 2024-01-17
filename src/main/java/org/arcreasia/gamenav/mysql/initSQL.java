package org.arcreasia.gamenav.mysql;

import java.sql.*;

public class initSQL {

    static Connection con = null;
    static Statement stmt = null;
    ResultSet resultSet = null;

    public initSQL() {
        try {
            // Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","*SRMrmp2023");
            stmt = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
