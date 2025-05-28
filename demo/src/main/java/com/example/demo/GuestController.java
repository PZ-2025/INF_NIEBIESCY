package com.example.demo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class GuestController {

    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> autorColumn;
    @FXML private TableColumn<Book, String> tytulColumn;
    @FXML private TableColumn<Book, String> gatunekColumn;
    @FXML private TableColumn<Book, String> wydawnictwoColumn;
    @FXML private TableColumn<Book, String> iloscColumn;

    /**
     * VBox zdefiniowany w pliku FXML, do którego dynamicznie dodawane są sekcje
     * liter alfabetu i odpowiadających im autorów.
     */
    @FXML
    private VBox alphabetBox;
    @FXML
    private Label loginButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label registerButton;


    /**
     * Mapa przechowująca listę autorów przypisanych do liter alfabetu.
     * Kluczem jest litera alfabetu (np. "A"), a wartością lista nazwisk autorów.
     */
    private Map<String, String[]> authorsMap = new HashMap<>();

    /**
     * Metoda wywoływana automatycznie po załadowaniu kontrolera (FXML).
     * Ładuje dane autorów z bazy danych oraz generuje widok alfabetu od A do Z.
     */
    @FXML
    public void initialize() {
        loginButton.setOnMouseClicked(this::otworzLogowanie);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        registerButton.setOnMouseClicked(this::otworzRejestracje);

        loadAuthorsFromDatabase();

        // Generowanie alfabetu A-Z
        createLetterSection("*"); // Dodaj sekcję dla '*'
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            createLetterSection(String.valueOf(letter));
        }

        // Ustawienie fabryk dla komórek tabeli
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        tytulColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        gatunekColumn.setCellValueFactory(new PropertyValueFactory<>("gatunek"));
        wydawnictwoColumn.setCellValueFactory(new PropertyValueFactory<>("wydawnictwo"));
        iloscColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));

        // Ładowanie książek z bazy danych
        loadBooksFromDatabase();
    }

    /**
     * Tworzy sekcję dla jednej litery alfabetu, zawierającą etykietę litery oraz (opcjonalnie) listę autorów,
     * których nazwiska zaczynają się na tę literę. Lista autorów może być rozwijana i zwijana po kliknięciu.
     *
     * @param letter Litera alfabetu (np. "A", "B", ...) dla której tworzona jest sekcja.
     */
    private void createLetterSection(String letter) {
        // VBox dla jednej sekcji litery (etykieta + lista autorów)
        VBox letterSection = new VBox(5);

        // Etykieta z literą (np. "A")
        Label letterLabel = new Label(letter);
        letterLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        letterLabel.setTextFill(Color.WHITE);
        letterLabel.setPadding(new javafx.geometry.Insets(0, 0, 0, 20));
        letterLabel.setEffect(new DropShadow(5.0, 2.0, 2.0, Color.BLACK));

        // VBox zawierający autorów przypisanych do litery
        VBox authorsBox = new VBox();
        authorsBox.setVisible(false);       // Początkowo ukryty
        authorsBox.setManaged(false);       // Nie zajmuje miejsca w układzie
        authorsBox.setMaxHeight(0);         // Bez wysokości – niewidoczny

        // Pobierz autorów z mapy i dodaj ich jako etykiety do authorsBox
        String[] authors = authorsMap.get(letter);
        if (authors != null) {
            for (String author : authors) {
                Label authorLabel = new Label(author);
                authorLabel.setStyle("-fx-font-size: 18;");
                authorLabel.setTextFill(Color.WHITE);
                authorLabel.setPadding(new javafx.geometry.Insets(0, 0, 0, 50));
                authorLabel.setEffect(new DropShadow(5.0, 2.0, 2.0, Color.BLACK));

                // Dodaj nasłuchiwanie na kliknięcie na nazwisko autora
                authorLabel.setOnMouseClicked(event -> {
                    // Wyświetlanie książek tylko tego autora
                    loadBooksByAuthor(author);
                });

                authorsBox.getChildren().add(authorLabel);
            }
        }

        // Po kliknięciu w literę – rozwijaj lub zwijaj listę autorów
        letterLabel.setOnMouseClicked(event -> {
            boolean isVisible = authorsBox.isVisible();
            if (isVisible) {
                // Zwijanie – ukryj i ustaw brak wysokości
                authorsBox.setMaxHeight(0);
            } else {
                // Rozwijanie – ustaw pełną wysokość
                authorsBox.setMaxHeight(Double.MAX_VALUE);
            }
            // Przełącz widoczność i zarządzanie układem
            authorsBox.setVisible(!isVisible);
            authorsBox.setManaged(!isVisible);
        });

        if (letter.equals("*")) {
            letterLabel.setOnMouseClicked(event -> {
                loadBooksFromDatabase();  // Funkcja do załadowania wszystkich książek
            });
        }

        letterSection.getChildren().add(letterLabel);
        letterSection.getChildren().add(authorsBox);

        alphabetBox.getChildren().add(letterSection);
    }

    /**
     * Ładuje listę autorów z bazy danych i grupuje ich według pierwszej litery nazwiska,
     * zapisując wyniki w mapie {@code authorsMap}, gdzie kluczem jest litera alfabetu,
     * a wartością tablica autorów rozpoczynających się od tej litery.
     *
     * W przypadku braku autorów dla danej litery mapa zostaje zainicjowana pustą tablicą.
     * Działa na tabeli "autorzy", zakładając, że kolumna z nazwiskiem to "nazwa".
     */
    private void loadAuthorsFromDatabase() {
        // Nawiązanie połączenia z bazą danych
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getConnection();

        String query = "SELECT nazwa FROM autorzy";

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String authorName = resultSet.getString("nazwa");

                // Sprawdzenie czy nazwa nie jest pusta
                if (authorName != null && !authorName.isEmpty()) {
                    String firstLetter = authorName.substring(0, 1).toUpperCase();

                    // Jeśli brak wpisu dla tej litery, inicjalizuj pustą tablicę
                    authorsMap.computeIfAbsent(firstLetter, k -> new String[0]);

                    // Rozszerz istniejącą tablicę o nowego autora
                    String[] currentAuthors = authorsMap.get(firstLetter);
                    String[] newAuthors = new String[currentAuthors.length + 1];
                    System.arraycopy(currentAuthors, 0, newAuthors, 0, currentAuthors.length);
                    newAuthors[currentAuthors.length] = authorName;
                    authorsMap.put(firstLetter, newAuthors);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Obsługa błędów – warto zamienić na logowanie w realnym projekcie
        }
    }

    /**
     * Ładuje książki z bazy danych i dodaje je do tabeli {@code booksTable}.
     * Każda książka ma przypisane informacje: autor, tytuł, wydawnictwo, ilość.
     */
    private void loadBooksFromDatabase() {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getConnection();

        BookDAO dao = new BookDAO();
        ObservableList<Book> books = dao.loadBooksFromDatabase(conn);

        booksTable.setItems(books);
    }

    private void loadBooksByAuthor(String authorName) {
        // Nawiązanie połączenia z bazą danych
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getConnection();

        String query = "SELECT ksiazki.*, autorzy.nazwa, gatunek.nazwa_gatunku " +
                        "FROM ksiazki " +
                        "INNER JOIN autorzy ON ksiazki.id_autora = autorzy.id_autora " +
                        "INNER JOIN gatunek ON ksiazki.id_gatunku = gatunek.id_gatunku " +
                        "WHERE autorzy.nazwa = ?";


        ObservableList<Book> books = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, authorName);  // Ustawienie parametru zapytania

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String autor = resultSet.getString("nazwa"); // Złączony autor
                String tytul = resultSet.getString("tytul");
                String gatunek = resultSet.getString("nazwa_gatunku");
                String wydawnictwo = resultSet.getString("wydawnictwo");
                String ilosc = resultSet.getString("ilosc");

                books.add(new Book(autor, tytul,gatunek , wydawnictwo, ilosc));
            }

            // Ustawienie książek w tabeli
            booksTable.setItems(books);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void otworzLogowanie(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku login-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

    private void otworzKsiegozbior(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("guest-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku czytelnik-dostepne-ksiazki-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            Stage stage = (Stage) booksButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

    private void otworzRejestracje(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rejestracja-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku rejestracja-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 450);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }
}



