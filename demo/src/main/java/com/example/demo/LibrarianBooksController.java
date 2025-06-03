package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    private Label reservationsButton;
    @FXML
    private Label loansButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label logoutButton;
    private Pracownik aktualnyPracownik;

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize() {
        reservationsButton.setOnMouseClicked(this::otworzRezerwacje);
        loansButton.setOnMouseClicked(this::otworzWypozyczenia);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        logoutButton.setOnMouseClicked(this::wyloguj);
        editIloscButton.setOnAction(e -> handleEditIlosc());
        editIloscButton.setOnAction(e -> handleUpdateIloscByTitle());



        // Ładowanie danych książek
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idKsiazki"));
        tytulColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        gatunekColumn.setCellValueFactory(new PropertyValueFactory<>("gatunek"));
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        dataDodaniaColumn.setCellValueFactory(new PropertyValueFactory<>("dataDodania"));
        wydawnictwoColumn.setCellValueFactory(new PropertyValueFactory<>("wydawnictwo"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        iloscColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));

        refreshTable();
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
