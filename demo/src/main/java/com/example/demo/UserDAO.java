package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class UserDAO {

    public static ObservableList<UserViewModel> getAllUsers() {
        ObservableList<UserViewModel> users = FXCollections.observableArrayList();

        String query = """
                SELECT c.imie, c.nazwisko, c.email,
                       (SELECT COUNT(*) FROM wypozyczenia w WHERE w.id_czytelnika = c.id_czytelnika) AS liczba_wypozyczen,
                       (SELECT COUNT(*) FROM rezerwacje r WHERE r.id_czytelnika = c.id_czytelnika) AS liczba_rezerwacji
                FROM czytelnicy c
                """;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection conn = connectNow.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                users.add(new UserViewModel(
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("email"),
                        "czytelnik",
                        rs.getInt("liczba_wypozyczen"),
                        rs.getInt("liczba_rezerwacji")
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

            String selectQuery = "SELECT * FROM czytelnicy WHERE email = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setString(1, email);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int id_czytelnika = rs.getInt("id_czytelnika");
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

                PreparedStatement deleteResStmt = conn.prepareStatement("DELETE FROM rezerwacje WHERE id_czytelnika = ?");
                deleteResStmt.setInt(1, id_czytelnika);
                deleteResStmt.executeUpdate();

                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM czytelnicy WHERE email = ?");
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

            String selectQuery = "SELECT * FROM pracownicy WHERE email = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setString(1, email);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
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

                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM pracownicy WHERE email = ?");
                deleteStmt.setString(1, email);
                deleteStmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


