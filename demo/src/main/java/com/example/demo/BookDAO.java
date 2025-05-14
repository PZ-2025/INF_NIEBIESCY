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
            ksiazki.ISBN
        FROM ksiazki 
        INNER JOIN autorzy ON ksiazki.id_autora = autorzy.id_autora
        gatunki ON ksiazki.id_gatunku = gatunki.id_gatunku
        WHERE ksiazki.id_ksiazki = ?
    """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookId);  // ← Dodano parametr ID
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String idKsiazki = resultSet.getString("id_ksiazki");
                String autor = resultSet.getString("nazwa");
                String tytul = resultSet.getString("tytul");
                String wydawnictwo = resultSet.getString("wydawnictwo");
                String dataDodania = resultSet.getString("data_dodania");
                String isbn = resultSet.getString("ISBN");

                books.add(new BookDetails(idKsiazki, autor, tytul, wydawnictwo, dataDodania, isbn));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

}
