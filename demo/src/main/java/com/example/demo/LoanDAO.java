package com.example.demo;

import java.sql.*;

public class LoanDAO {

    private final Connection connection;

    public LoanDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean wypozyczKsiazke(String emailCzytelnika, String tytulKsiazki) {
        try {
            // 1. Pobierz id_czytelnika na podstawie e-maila
            String selectReader = "SELECT id_czytelnika FROM czytelnicy WHERE email = ?";
            PreparedStatement readerStmt = connection.prepareStatement(selectReader);
            readerStmt.setString(1, emailCzytelnika);
            ResultSet readerRs = readerStmt.executeQuery();

            if (!readerRs.next()) {
                System.out.println("Nie znaleziono czytelnika o podanym emailu.");
                return false;
            }
            int idCzytelnika = readerRs.getInt("id_czytelnika");

            // 2. Pobierz id_ksiazki oraz ilość
            String selectBook = "SELECT id_ksiazki, ilosc FROM ksiazki WHERE tytul = ?";
            PreparedStatement bookStmt = connection.prepareStatement(selectBook);
            bookStmt.setString(1, tytulKsiazki);
            ResultSet bookRs = bookStmt.executeQuery();

            if (!bookRs.next()) {
                System.out.println("Nie znaleziono książki o podanym tytule.");
                return false;
            }

            int idKsiazki = bookRs.getInt("id_ksiazki");
            int iloscDostepna = bookRs.getInt("ilosc");

            if (iloscDostepna <= 0) {
                System.out.println("Brak dostępnych egzemplarzy.");
                return false;
            }

            // 3. Wstaw do tabeli wypożyczenia
            String insertLoan = "INSERT INTO wypozyczenia (id_ksiazki, id_czytelnika) VALUES (?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertLoan);
            insertStmt.setInt(1, idKsiazki);
            insertStmt.setInt(2, idCzytelnika);
            insertStmt.executeUpdate();

            // 4. Zmniejsz ilość książek
            String updateIlosc = "UPDATE ksiazki SET ilosc = ilosc - 1 WHERE id_ksiazki = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateIlosc);
            updateStmt.setInt(1, idKsiazki);
            updateStmt.executeUpdate();

            // 5. Zaktualizuj ksiazki_wypozyczone
            String checkIfExists = "SELECT ilosc_wypozyczonych FROM ksiazki_wypozyczone WHERE id_ksiazki = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkIfExists);
            checkStmt.setInt(1, idKsiazki);
            ResultSet checkRs = checkStmt.executeQuery();

            if (checkRs.next()) {
                // update
                String updateCount = "UPDATE ksiazki_wypozyczone SET ilosc_wypozyczonych = ilosc_wypozyczonych + 1 WHERE id_ksiazki = ?";
                PreparedStatement updStmt = connection.prepareStatement(updateCount);
                updStmt.setInt(1, idKsiazki);
                updStmt.executeUpdate();
            } else {
                // insert
                String insertCount = "INSERT INTO ksiazki_wypozyczone (id_ksiazki, ilosc_wypozyczonych, ilosc_calkowita) VALUES (?, 1, ?)";
                PreparedStatement insStmt = connection.prepareStatement(insertCount);
                insStmt.setInt(1, idKsiazki);
                insStmt.setInt(2, iloscDostepna);
                insStmt.executeUpdate();
            }

            System.out.println("✅ Książka została wypożyczona.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

