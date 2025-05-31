package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO {
    public ObservableList<Book> loadBooksFromDatabase(Connection connection) {
        ObservableList<Book> books = FXCollections.observableArrayList();
        String query = "SELECT ksiazki.*, autorzy.nazwa, gatunek.nazwa_gatunku " +
                "FROM ksiazki " +
                "INNER JOIN autorzy ON ksiazki.id_autora = autorzy.id_autora " +
                "INNER JOIN gatunek ON ksiazki.id_gatunku = gatunek.id_gatunku";


        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String autor = resultSet.getString("nazwa");
                String tytul = resultSet.getString("tytul");
                String gatunek = resultSet.getString("nazwa_gatunku");
                String wydawnictwo = resultSet.getString("wydawnictwo");
                String ilosc = resultSet.getString("ilosc");

                books.add(new Book(autor, tytul,gatunek, wydawnictwo, ilosc));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books; // Teraz zwracamy listę książek
    }

    // Dla KartaKsiazkiPdf
    public ObservableList<BookDetails> loadBookDetails(Connection connection, String bookId) {
        ObservableList<BookDetails> books = FXCollections.observableArrayList();
        String query = """
        SELECT 
            ksiazki.id_ksiazki, 
            autorzy.nazwa, 
            ksiazki.tytul, 
            gatunki.nazwa_gatunku,
            ksiazki.wydawnictwo, 
            ksiazki.data_dodania, 
            ksiazki.ISBN,
            ksiazki.ilosc
        FROM ksiazki 
        INNER JOIN autorzy ON ksiazki.id_autora = autorzy.id_autora
        INNER JOIN gatunki ON ksiazki.id_gatunku = gatunki.id_gatunku
        WHERE ksiazki.id_ksiazki = ?
    """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String idKsiazki = resultSet.getString("id_ksiazki");
                String autor = resultSet.getString("nazwa");
                String tytul = resultSet.getString("tytul");
                String wydawnictwo = resultSet.getString("wydawnictwo");
                String dataDodania = resultSet.getString("data_dodania");
                String isbn = resultSet.getString("ISBN");
                String ilosc = resultSet.getString("ilosc");
                String gatunek = resultSet.getString("nazwa_gatunku");

                books.add(new BookDetails(idKsiazki, autor, gatunek, tytul, wydawnictwo, dataDodania, isbn, ilosc));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // Dla AdminBooksController
    public ObservableList<BookDetails> loadAllBookDetails(Connection connection) {
        ObservableList<BookDetails> books = FXCollections.observableArrayList();
        String query = """
        SELECT 
            ksiazki.id_ksiazki, 
            autorzy.nazwa, 
            ksiazki.tytul, 
            gatunek.nazwa_gatunku,
            ksiazki.wydawnictwo, 
            ksiazki.data_dodania, 
            ksiazki.ISBN,
            ksiazki.ilosc
        FROM ksiazki 
        INNER JOIN autorzy ON ksiazki.id_autora = autorzy.id_autora
        INNER JOIN gatunek ON ksiazki.id_gatunku = gatunek.id_gatunku
    """;

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String idKsiazki = resultSet.getString("id_ksiazki");
                String autor = resultSet.getString("nazwa");
                String tytul = resultSet.getString("tytul");
                String gatunek = resultSet.getString("nazwa_gatunku");
                String wydawnictwo = resultSet.getString("wydawnictwo");
                String dataDodania = resultSet.getString("data_dodania");
                String isbn = resultSet.getString("ISBN");
                String ilosc = resultSet.getString("ilosc");

                books.add(new BookDetails(idKsiazki, autor, gatunek, tytul, wydawnictwo, dataDodania, isbn, ilosc));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // Dodaj książkę
    public void addBook(Connection connection, BookDetails book) {
        String query = """
            INSERT INTO ksiazki (id_autora, id_gatunku, tytul, wydawnictwo, data_dodania, ISBN, ilosc)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getAutor()); // zakładamy, że przekazujesz id_autora jako String
            statement.setString(2, book.getGatunek()); // id_gatunku
            statement.setString(3, book.getTytul());
            statement.setString(4, book.getWydawnictwo());
            statement.setString(5, book.getDataDodania());
            statement.setString(6, book.getIsbn());
            statement.setString(7, book.getIlosc());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Usuń książkę
    public void deleteBook(Connection connection, String bookId) {
        String query = "DELETE FROM ksiazki WHERE id_ksiazki=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Edytuj książkę
    public void updateBook(Connection connection, BookDetails book) {
        String query = """
            UPDATE ksiazki 
            SET id_autora=?, id_gatunku=?, tytul=?, wydawnictwo=?, data_dodania=?, ISBN=?, ilosc=?
            WHERE id_ksiazki=?
        """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getAutor());
            statement.setString(2, book.getGatunek());
            statement.setString(3, book.getTytul());
            statement.setString(4, book.getWydawnictwo());
            statement.setString(5, book.getDataDodania());
            statement.setString(6, book.getIsbn());
            statement.setString(7, book.getIlosc());
            statement.setString(8, book.getIdKsiazki());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
