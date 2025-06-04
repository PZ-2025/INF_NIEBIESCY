package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "bibliotekaDB";
        String databaseUser = "root";
        String databasePassword = "";
        //String databaseUrl = "jdbc:mariadb://localhost:3307/" + databaseName;
        String databaseUrl = "jdbc:mariadb://localhost:3306/" + databaseName;

        try{
            Class.forName("org.mariadb.jdbc.Driver");
            databaseLink = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}
