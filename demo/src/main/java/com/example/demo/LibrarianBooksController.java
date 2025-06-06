package com.example.demo;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LibrarianBooksController {
    @FXML private TableView<BookDetails> booksTable;
    @FXML private TableColumn<BookDetails, String> idColumn;
    @FXML private TableColumn<BookDetails, String> tytulColumn;
    @FXML private TableColumn<BookDetails, String> gatunekColumn;
    @FXML private TableColumn<BookDetails, String> autorColumn;
    @FXML private TableColumn<BookDetails, String> dataDodaniaColumn;
    @FXML private TableColumn<BookDetails, String> wydawnictwoColumn;
    @FXML private TableColumn<BookDetails, String> isbnColumn;
    @FXML private TableColumn<BookDetails, String> iloscColumn;
    @FXML private TextField iloscField;
    @FXML private TextField tytulField;
    @FXML private Button editIloscButton;
    @FXML private Label errorLabel;

    @FXML
    private VBox alphabetBox;

    @FXML
    private Label reservationsButton;
    @FXML
    private Label loansButton;
    @FXML
    private Label zwrotyButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label logoutButton;
    private Pracownik aktualnyPracownik;

    private Map<String, String[]> authorsMap = new HashMap<>();

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize() {
        reservationsButton.setOnMouseClicked(this::otworzRezerwacje);
        loansButton.setOnMouseClicked(this::otworzWypozyczenia);
        zwrotyButton.setOnMouseClicked(this::otworzZwroty);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        logoutButton.setOnMouseClicked(this::wyloguj);
        editIloscButton.setOnAction(e -> {
            handleEditIlosc();
            handleUpdateIloscByTitle();
        });

        loadAuthorsFromDatabase();

        // Generowanie alfabetu A-Z
        createLetterSection("*"); // Dodaj sekcję dla '*'
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            createLetterSection(String.valueOf(letter));
        }

        // Ładowanie danych książek
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idKsiazki"));
        tytulColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        gatunekColumn.setCellValueFactory(new PropertyValueFactory<>("gatunek"));
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        dataDodaniaColumn.setCellValueFactory(new PropertyValueFactory<>("dataDodania"));
        wydawnictwoColumn.setCellValueFactory(new PropertyValueFactory<>("wydawnictwo"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        iloscColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));

        booksTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tytulField.setText(newSelection.getTytul());
            } else {
                tytulField.clear();
            }
        });

        refreshTable();
    }

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
                refreshTable();  // Funkcja do załadowania wszystkich książek
            });
        }

        letterSection.getChildren().add(letterLabel);
        letterSection.getChildren().add(authorsBox);

        alphabetBox.getChildren().add(letterSection);
    }

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

    private void handleUpdateIloscByTitle() {
        String tytul = tytulField.getText();
        String nowaIlosc = iloscField.getText();

        if (tytul.isEmpty() || !nowaIlosc.matches("\\d+")) {
            errorLabel.setText("Wprowadź tytuł i poprawną ilość.");
            return;
        }

        try {
            Connection conn = new DatabaseConnection().getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE ksiazki SET ilosc = ? WHERE tytul = ?");
            stmt.setInt(1, Integer.parseInt(nowaIlosc));
            stmt.setString(2, tytul);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                errorLabel.setText("Nie znaleziono książki.");
            } else {
                errorLabel.setText("Zaktualizowano.");
                refreshTable();
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            errorLabel.setText("Błąd bazy danych.");
            e.printStackTrace();
        }
    }

    private void handleEditIlosc() {
        BookDetails selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Nie wybrano książki.");
            return;
        }

        String newIlosc = iloscField.getText();
        if (!newIlosc.matches("\\d+")) {
            errorLabel.setText("Podaj poprawną liczbę.");
            return;
        }

        try {
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE ksiazki SET ilosc = ? WHERE id_ksiazki = ?");
            stmt.setInt(1, Integer.parseInt(newIlosc));
            stmt.setInt(2, Integer.parseInt(selected.getIdKsiazki()));
            stmt.executeUpdate();

            refreshTable();
            errorLabel.setText("Zaktualizowano.");
        } catch (SQLException e) {
            errorLabel.setText("Błąd aktualizacji.");
            e.printStackTrace();
        }
    }

    private void loadBooksByAuthor(String authorName) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getConnection();

        String query = "SELECT ksiazki.id_ksiazki, autorzy.nazwa, ksiazki.tytul, gatunek.nazwa_gatunku, " +
                "ksiazki.wydawnictwo, ksiazki.data_dodania, ksiazki.ISBN, ksiazki.ilosc " +
                "FROM ksiazki " +
                "INNER JOIN autorzy ON ksiazki.id_autora = autorzy.id_autora " +
                "INNER JOIN gatunek ON ksiazki.id_gatunku = gatunek.id_gatunku " +
                "WHERE autorzy.nazwa = ?";

        ObservableList<BookDetails> books = FXCollections.observableArrayList();

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, authorName);
            ResultSet resultSet = statement.executeQuery();

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

            booksTable.setItems(books);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void refreshTable() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn.getConnection();

        if (conn == null) {
            System.out.println("Błąd: brak połączenia z bazą danych");
            return;
        }

        BookDAO dao = new BookDAO(conn);
        ObservableList<BookDetails> books = dao.loadAllBookDetails(conn);

        booksTable.setItems(books);
    }

    private void otworzRezerwacje(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bibliotekarz-rezerwacje-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku bibliotekarz-rezerwacje-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            LibrarianReservationsController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) reservationsButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

    private void otworzWypozyczenia(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bibliotekarz-wypozyczanie-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku bibliotekarz-wypozyczanie-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            LibrarianLoanController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) loansButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

    private void otworzKsiegozbior(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bibliotekarz-ksiegozbior-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku bibliotekarz-ksiegozbior-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            LibrarianBooksController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) booksButton.getScene().getWindow();
            Scene scene = new Scene(root, 1170, 700);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

    private void otworzZwroty(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bibliotekarz-zwroty-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku bibliotekarz-zwroty-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            LibrarianZwrotyController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) zwrotyButton.getScene().getWindow();
            Scene scene = new Scene(root, 1170, 700);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

    private void wyloguj(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku login-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }
}
