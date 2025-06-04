package com.example.demo;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

import java.sql.Connection;
import java.sql.DriverManager;

public class EmbeddedMariaDB {
    private DB db;

    public void startMariaDB() throws Exception {
        Class.forName("org.mariadb.jdbc.Driver");

        DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
        //config.setPort(3307);
        config.setPort(3306);
        db = DB.newEmbeddedDB(config.build());
        db.start();
        System.out.println("MariaDB started on port 3307");

        // Utwórz bazę, ale pomiń tworzenie użytkownika jeśli to nie jest konieczne
        //String url = "jdbc:mariadb://localhost:3307";
        String url = "jdbc:mariadb://localhost:3306";
        try (Connection conn = DriverManager.getConnection(url, "root", "")) {
            conn.createStatement().execute("CREATE DATABASE IF NOT EXISTS bibliotekaDB");
        }

        if (!waitForMariaDB(20, 1500)) {
            throw new RuntimeException("MariaDB nie wystartowało na porcie 3307!");
        }
    }

    public boolean waitForMariaDB(int maxRetries, int delayMs) throws InterruptedException {
        //String url = "jdbc:mariadb://localhost:3307/information_schema";
        String url = "jdbc:mariadb://localhost:3306/information_schema";
        String user = "root";
        String password = "";

        for (int i = 0; i < maxRetries; i++) {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                System.out.println("Połączono z MariaDB w próbie nr: " + (i+1));
                return true;
            } catch (Exception e) {
                System.out.println("Próba nr " + (i+1) + " nieudana, czekam... " + e.getMessage());
                Thread.sleep(delayMs);
            }
        }
        return false;
    }



    public void stopMariaDB() throws Exception {
        if (db != null) db.stop();
    }


    public static void main(String[] args) throws Exception {
        EmbeddedMariaDB embeddedMariaDB = new EmbeddedMariaDB();
        embeddedMariaDB.startMariaDB();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                embeddedMariaDB.stopMariaDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
