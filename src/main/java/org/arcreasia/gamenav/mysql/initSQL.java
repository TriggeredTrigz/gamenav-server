package org.arcreasia.gamenav.mysql;

import org.arcreasia.gamenav.globalMethods.logger;

import java.util.Scanner;
import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;

public class initSQL {

    public static Dotenv env = Dotenv.configure().load();

    final static Scanner sc = new Scanner(System.in);

    static Connection con = null;
    public static Statement stmt = null;
    ResultSet resultSet = null;

    public static void connectSQL() {
        try {
            logger.logApp.info("Establishing MySQL connection...");
            con = DriverManager.getConnection(env.get("SERVER_URL"),env.get("SERVER_USER"),env.get("SERVER_PW"));
            logger.logApp.info("Established MySQL connection.");
            stmt = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createDB() {
        logger.logApp.info("Initialising database creation.");
        try {
            resultSet = con.getMetaData().getCatalogs();
            boolean dbExists = false;
            while ( resultSet.next() ) {
                if ( "dbnav".equals(resultSet.getString(1)) ) {
                    // System.out.println("dbnav exists."); 
                    logger.logApp.info("dbnav exists.");
                    stmt.executeUpdate("use dbnav;");
                    // System.out.println("dbnav database selected.");
                    logger.logApp.info("dbnav database selected.");
                    dbExists = true;
                    break;
                }
            }
            if ( dbExists == false ) {
                // System.out.println("dbnav does not exist.");
                logger.logApp.info("dbanv does not exist.");
                stmt.executeUpdate("create database if not exists dbnav;"); 
                // System.out.println("dbnav database created.");
                logger.logApp.info("dbnav database created.");
                stmt.executeUpdate("use dbnav;");
                // System.out.println("dbnav database selected.");
                logger.logApp.info("dbnav database selected.");
                // stmt.executeUpdate("create table userDetails(userID int not null AUTO_INCREMENT, name varchar(255) not null unique, email varchar(255) unique not null, password varchar(255) not null, favGame varchar(8000), PRIMARY KEY (userID));");
                // System.out.println("userDetails table created.");
                // stmt.executeUpdate("create table steamSpyGameDetails(appID int not null unique, name varchar(255), developers varchar(255), publishers varchar(255), positive int, negative int, owners varchar(40), price varchar(10), initialPrice varchar(10), discount varchar(5), ccu int, languages varchar(1000),genres varchar(1000), tags varchar(1000), PRIMARY KEY (appID));");
                // System.out.println("steamSpyGameDetails table created.");
                // stmt.executeUpdate("create table steamAppList(appID int not null unique, name varchar(255));");
                // System.out.println("steamAppList table created.");
                // stmt.executeUpdate("create table steamGameList(appID int not null unique, name varchar(255))");
                // System.out.println("steamGameList table created.");
                // stmt.executeUpdate("create table steamDemoList(appID int not null unique, name varchar(255))");
                // System.out.println("steamDemoList table created.");
                // stmt.executeUpdate("create table steamOmitList(appID int not null unique)");
                // System.out.println("steamOmitList table created.");
            }
            if( 0==stmt.executeUpdate("create table if not exists steamList(appID int not null unique primary key, name varchar(255), type varchar(10) not null);") )
            logger.logApp.info("steamList table created.");
            if( 0==stmt.executeUpdate("create table if not exists steamGameDesc(appid int not null unique primary key, gameDesc text, price int not null, metacritic int, releaseDate varchar(25));") )
            logger.logApp.info("steamGameDesc table created.");
            if( 0==stmt.executeUpdate("create table if not exists supportedOS(appid int not null unique primary key, windows boolean, mac boolean, linux boolean);") )
            logger.logApp.info("supportedOS table created.");
            if( 0==stmt.executeUpdate("create table if not exists tags(appid int not null unique primary key);") )
            logger.logApp.info("tags table created.");
            if( 0==stmt.executeUpdate("create table if not exists genres(appid int not null unique primary key);") )
            logger.logApp.info("genres table created.");
            if( 0==stmt.executeUpdate("create table if not exists developers(appid int not null unique primary key);") )
            logger.logApp.info("developers table created.");
            if( 0==stmt.executeUpdate("create table if not exists publishers(appid int not null unique primary key);") )
            logger.logApp.info("publishers table created.");
            if( 0==stmt.executeUpdate("create table if not exists languages(appid int not null unique primary key);") )
            logger.logApp.info("languages table created.");
            logger.logApp.info("Database creation completed.");
        } catch (Exception e) {
            // e.printStackTrace();
            logger.logApp.info(e.toString());
            logger.logApp.info("Database creation completed with errors. Check logs.");
        }
    }

}
