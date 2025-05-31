package com.example.demo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminBooksController {
    @FXML
    private Label ordersButton;
    @FXML
    private Label usersButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label logoutButton;
    private Pracownik aktualnyPracownik;
    @FXML private TextField tytulField, autorField, gatunekField, dataDodaniaField, wydawnictwoField, isbnField, iloscField;
    @FXML private Button addButton, editButton, deleteButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TableView<BookDetails> booksTable;

    @FXML
    private TableColumn<BookDetails, String> idColumn;
    @FXML
    private TableColumn<BookDetails, String> tytulColumn;
    @FXML
    private TableColumn<BookDetails, String> gatunekColumn;
    @FXML
    private TableColumn<BookDetails, String> autorColumn;
    @FXML
    private TableColumn<BookDetails, String> dataDodaniaColumn;
    @FXML
    private TableColumn<BookDetails, String> wydawnictwoColumn;
    @FXML
    private TableColumn<BookDetails, String> isbnColumn;
    @FXML
    private TableColumn<BookDetails, String> iloscColumn;

    private DatabaseConnection databaseConnection;
    private Connection connection;

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize() {
        databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();

        ordersButton.setOnMouseClicked(this::otworzZamowienia);
        usersButton.setOnMouseClicked(this::otworzUzytkownikow);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        logoutButton.setOnMouseClicked(this::wyloguj);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("idKsiazki"));
        tytulColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        gatunekColumn.setCellValueFactory(new PropertyValueFactory<>("gatunek"));
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        dataDodaniaColumn.setCellValueFactory(new PropertyValueFactory<>("dataDodania"));
        wydawnictwoColumn.setCellValueFactory(new PropertyValueFactory<>("wydawnictwo"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        iloscColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));

        addButton.setOnAction(event -> handleAddBook());
        editButton.setOnAction(event -> handleEditBook());
        deleteButton.setOnAction(event -> handleDeleteBook());
        refreshTable();

        booksTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tytulField.setText(newSelection.getTytul());
                autorField.setText(newSelection.getAutor());
                gatunekField.setText(newSelection.getGatunek());
                dataDodaniaField.setText(newSelection.getDataDodania());
                wydawnictwoField.setText(newSelection.getWydawnictwo());
                isbnField.setText(newSelection.getIsbn());
                iloscField.setText(newSelection.getIlosc());
            }
        });
    }

    private void refreshTable() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();
        BookDAO bookDAO = new BookDAO(connection);
        ObservableList<BookDetails> bookDetailsList = bookDAO.loadAllBookDetails(connection);
        booksTable.setItems(bookDetailsList);
    }

    @FXML
    private void handleAddBook() {
        errorLabel.setText("");
        try {
            String tytul = tytulField.getText();
            String autor = autorField.getText();
            String gatunek = gatunekField.getText();
            String wydawnictwo = wydawnictwoField.getText();
            String dataDodania = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String isbn = isbnField.getText();
            String ilosc = iloscField.getText();

            String idAutora = getOrCreateAuthorIdByName(autor);
            String idGatunku = getGenreIdByName(gatunek);

            if (idAutora == null || idGatunku == null) {
                errorLabel.setText("Nieprawidłowy autor lub gatunek.");
                return;
            }

            BookDetails book = new BookDetails("0", autor, gatunek, tytul, wydawnictwo, dataDodania, isbn, ilosc);

            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connection = connectNow.getConnection();

            BookDAO bookDAO = new BookDAO(connection);
            bookDAO.addBook(book, idAutora, idGatunku);
            refreshTable();

        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException
                    && e.getMessage().contains("unique_tytul")) {
                errorLabel.setText("Książka już istnieje.");
            } else {
                errorLabel.setText("Błąd bazy danych: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEditBook() {
        BookDetails selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            // Możesz wyświetlić alert, że nic nie wybrano
            System.out.println("Nie wybrano książki do edycji!");
            return;
        }

        String tytul = tytulField.getText();
        String autor = autorField.getText();
        String gatunek = gatunekField.getText();
        String rokWydania = dataDodaniaField.getText();
        String wydawnictwo = wydawnictwoField.getText();
        String isbn = isbnField.getText();
        String ilosc = iloscField.getText();

        String idAutora = getAuthorIdByName(autor);
        String idGatunku = getGenreIdByName(gatunek);

        BookDetails updatedBook = new BookDetails(
                selectedBook.getIdKsiazki(),
                autor,
                gatunek,
                tytul,
                wydawnictwo,
                rokWydania,
                isbn,
                ilosc
        );

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();

        BookDAO dao = new BookDAO(connection);
        dao.updateBook(updatedBook, idAutora, idGatunku);

        refreshTable();
        clearFields();
    }

    @FXML
    private void handleDeleteBook() {
        BookDetails selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            System.out.println("Nie wybrano książki do usunięcia!");
            return;
        }

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();

        BookDAO dao = new BookDAO(connection);
        dao.deleteBook(selectedBook.getIdKsiazki());

        refreshTable();
        clearFields();
    }

    private void clearFields() {
        tytulField.clear();
        autorField.clear();
        gatunekField.clear();
        dataDodaniaField.clear();
        wydawnictwoField.clear();
        isbnField.clear();
        iloscField.clear();
    }

    private String getOrCreateAuthorIdByName(String name) {
        String idAutora = getAuthorIdByName(name);
        if (idAutora != null) {
            return idAutora;
        }

        // Autor nie istnieje — dodaj nowego
        String insert = "INSERT INTO autorzy (nazwa) VALUES (?)";
        try (PreparedStatement statement = databaseConnection.getConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Nie udało się dodać autora");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return String.valueOf(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAuthorIdByName(String name) {
        String query = "SELECT id_autora FROM autorzy WHERE nazwa=?";
        try (PreparedStatement statement = databaseConnection.getConnection().prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("id_autora");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getGenreIdByName(String name) {
        String query = "SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku=?";
        try (PreparedStatement statement = databaseConnection.getConnection().prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("id_gatunku");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void otworzZamowienia(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-zamowienia.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku admin-zamowienia.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            AdminOrdersController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) ordersButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

    private void otworzUzytkownikow(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-users-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku admin-users-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            AdminUsersController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) usersButton.getScene().getWindow();
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-book-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku admin-book-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            AdminBooksController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) booksButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 600);
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
