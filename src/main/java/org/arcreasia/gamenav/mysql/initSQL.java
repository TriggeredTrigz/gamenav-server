package org.arcreasia.gamenav.mysql;

import java.sql.*;

public class initSQL {

    static Connection con = null;
    public static Statement stmt = null;
    ResultSet resultSet = null;

    public static void connectSQL() {
        try {
            // Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","$PASSWORD");
            stmt = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createDB() {
        try {
            resultSet = con.getMetaData().getCatalogs();
            boolean dbExists = false;
            while ( resultSet.next() ) {
                if ( "dbnav".equals(resultSet.getString(1)) ) {
                    System.out.println("dbnav exists."); 
                    stmt.executeUpdate("use dbnav;");
                    System.out.println("dbnav database selected.");
                    dbExists = true;
                    break;
                }
            }
            if ( dbExists == false ) {
                System.out.println("dbnav does not exist.");
                stmt.executeUpdate("create database if not exists dbnav;"); 
                System.out.println("dbnav database created.");
                stmt.executeUpdate("use dbnav;");
                System.out.println("dbnav database selected.");
                stmt.executeUpdate("create table userDetails(userID int not null AUTO_INCREMENT, name varchar(255) not null unique, email varchar(255) unique not null, password varchar(255) not null, favGame varchar(8000), PRIMARY KEY (userID));");
                System.out.println("userDetails table created.");
                stmt.executeUpdate("create table steamSpyGameDetails(appID int not null unique, name varchar(255), developers varchar(255), publishers varchar(255), positive int, negative int, owners varchar(40), price varchar(10), initialPrice varchar(10), discount varchar(5), ccu int, languages varchar(1000),genres varchar(1000), tags varchar(1000), PRIMARY KEY (appID));");
                System.out.println("steamSpyGameDetails table created.");
                stmt.executeUpdate("create table steamAppList(appID int not null unique, name varchar(255));");
                System.out.println("steamAppList table created.");
            }
        } catch (Exception e) {
            if ( "java.sql.SQLSyntaxErrorException" != e.getClass().getName() ) e.printStackTrace();
        }
    }

}
