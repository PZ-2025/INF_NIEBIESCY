package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AdminOrdersController {
    @FXML
    private Label ordersButton;
    @FXML
    private Label usersButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label logoutButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button orderFormButton;
    private Pracownik aktualnyPracownik;
    @FXML
    private TableView<OrderBook> ordersTable;
    @FXML
    private TableColumn<OrderBook, String> idColumn;
    @FXML
    private TableColumn<OrderBook, String> tytulColumn;
    @FXML
    private TableColumn<OrderBook, String> autorColumn;
    @FXML
    private TableColumn<OrderBook, String> iloscColumn;

    private ObservableList<OrderBook> orderList = FXCollections.observableArrayList();

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize() {
        ordersButton.setOnMouseClicked(this::otworzZamowienia);
        usersButton.setOnMouseClicked(this::otworzUzytkownikow);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        logoutButton.setOnMouseClicked(this::wyloguj);
        historyButton.setOnMouseClicked(this::otworzHistorie);
        orderFormButton.setOnMouseClicked(this::zamow);
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        tytulColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTytul()));
        autorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor()));
        iloscColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIlosc()));

        loadOrderBooks();
    }

    private void loadOrderBooks() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        // Wyświetlanie książek, których ilość nie przekracza 2 egzemplarzy
        String query = "SELECT k.id_ksiazki, k.tytul, a.nazwa AS autor, k.ilosc " +
                "FROM ksiazki k " +
                "JOIN autorzy a ON k.id_autora = a.id_autora " +
                "WHERE k.ilosc <= 2";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String id = resultSet.getString("id_ksiazki");
                String tytul = resultSet.getString("tytul");
                String autor = resultSet.getString("autor");
                String ilosc = resultSet.getString("ilosc");

                orderList.add(new OrderBook(id, tytul, autor, ilosc));
            }

            ordersTable.setItems(orderList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void otworzHistorie(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-historia-zamowien.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku admin-historia-zamowien.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            AdminOrderHistoryController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) historyButton.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

    public void zamow(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-zamow-form.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku admin-zamow-form.fxml");
                return;
            }
            AnchorPane root = fxmlLoader.load();
            AdminOrderFormController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) orderFormButton.getScene().getWindow();
            Scene scene = new Scene(root, 530, 320);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
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
