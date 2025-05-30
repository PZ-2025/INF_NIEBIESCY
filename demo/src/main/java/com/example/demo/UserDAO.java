package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class UserDAO {

    public static ObservableList<UserViewModel> getAllUsers() {
        ObservableList<UserViewModel> users = FXCollections.observableArrayList();

        String czytelnicyQuery = "SELECT id_czytelnika, imie, nazwisko, email FROM czytelnicy";
        String pracownicyQuery = "SELECT imie, nazwisko, email, rola FROM pracownicy";

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection conn = connectNow.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rsCzytelnicy = stmt.executeQuery(czytelnicyQuery);
            while (rsCzytelnicy.next()) {
                users.add(new UserViewModel(
                        rsCzytelnicy.getString("imie"),
                        rsCzytelnicy.getString("nazwisko"),
                        rsCzytelnicy.getString("email"),
                        "czytelnik"
                ));
            }

            ResultSet rsPracownicy = stmt.executeQuery(pracownicyQuery);
            while (rsPracownicy.next()) {
                users.add(new UserViewModel(
                        rsPracownicy.getString("imie"),
                        rsPracownicy.getString("nazwisko"),
                        rsPracownicy.getString("email"),
                        rsPracownicy.getString("rola")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void promoteCzytelnikToPracownik(String email) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection conn = connectNow.getConnection();

            // znajdź czytelnika
            String selectQuery = "SELECT * FROM czytelnicy WHERE email = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setString(1, email);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int id_czytelnika = rs.getInt("id_czytelnika");
                // dodaj do pracowników
                String insertQuery = "INSERT INTO pracownicy (imie, nazwisko, tel, email, haslo, ulica, miasto, rola) VALUES (?, ?, ?, ?, ?, ?, ?, 'bibliotekarz')";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, rs.getString("imie"));
                insertStmt.setString(2, rs.getString("nazwisko"));
                insertStmt.setString(3, String.valueOf(rs.getLong("tel")));
                insertStmt.setString(4, rs.getString("email"));
                insertStmt.setString(5, rs.getString("haslo"));
                insertStmt.setString(6, rs.getString("ulica"));
                insertStmt.setString(7, rs.getString("miasto"));

                insertStmt.executeUpdate();

                // usuń rezerwacje czytelnika
                String deleteReservations = "DELETE FROM rezerwacje WHERE id_czytelnika = ?";
                PreparedStatement deleteResStmt = conn.prepareStatement(deleteReservations);
                deleteResStmt.setInt(1, id_czytelnika);
                deleteResStmt.executeUpdate();

                // usuń z czytelników
                String deleteQuery = "DELETE FROM czytelnicy WHERE email = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setString(1, email);
                deleteStmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downgradePracownikToCzytelnik(String email) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection conn = connectNow.getConnection();

            // znajdź pracownika
            String selectQuery = "SELECT * FROM pracownicy WHERE email = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setString(1, email);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // dodaj do czytelników
                String insertQuery = "INSERT INTO czytelnicy (imie, nazwisko, tel, email, haslo, ulica, miasto) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, rs.getString("imie"));
                insertStmt.setString(2, rs.getString("nazwisko"));
                insertStmt.setString(3, String.valueOf(rs.getLong("tel")));
                insertStmt.setString(4, rs.getString("email"));
                insertStmt.setString(5, rs.getString("haslo"));
                insertStmt.setString(6, rs.getString("ulica"));
                insertStmt.setString(7, rs.getString("miasto"));

                insertStmt.executeUpdate();

                // usuń z pracowników
                String deleteQuery = "DELETE FROM pracownicy WHERE email = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setString(1, email);
                deleteStmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

