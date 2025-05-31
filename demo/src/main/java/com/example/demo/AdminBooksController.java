package com.example.demo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

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
    private TableColumn<BookDetails, String> rokWydaniaColumn;
    @FXML
    private TableColumn<BookDetails, String> wydawnictwoColumn;
    @FXML
    private TableColumn<BookDetails, String> isbnColumn;
    @FXML
    private TableColumn<BookDetails, String> iloscColumn;

    private final BookDAO bookDAO = new BookDAO();

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize() {
        ordersButton.setOnMouseClicked(this::otworzZamowienia);
        usersButton.setOnMouseClicked(this::otworzUzytkownikow);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        logoutButton.setOnMouseClicked(this::wyloguj);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idKsiazki"));
        tytulColumn.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        gatunekColumn.setCellValueFactory(new PropertyValueFactory<>("gatunek"));
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        rokWydaniaColumn.setCellValueFactory(new PropertyValueFactory<>("dataDodania"));
        wydawnictwoColumn.setCellValueFactory(new PropertyValueFactory<>("wydawnictwo"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        iloscColumn.setCellValueFactory(new PropertyValueFactory<>("ilosc"));

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connection = connectNow.getConnection();

        ObservableList<BookDetails> bookDetailsList = bookDAO.loadAllBookDetails(connection);

        booksTable.setItems(bookDetailsList);
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
