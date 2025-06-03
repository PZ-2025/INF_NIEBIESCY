package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminOrderHistoryController {
    @FXML
    private Label ordersButton;
    @FXML
    private Label usersButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label logoutButton;
    @FXML
    private Button returnButton;
    private Pracownik aktualnyPracownik;
    @FXML
    private TableView<OrderHistory> ordersTable;
    @FXML
    private TableColumn<OrderHistory, String> idColumn;
    @FXML
    private TableColumn<OrderHistory, String> tytulColumn;
    @FXML
    private TableColumn<OrderHistory, String> autorColumn;
    @FXML
    private TableColumn<OrderHistory, String> dataDodaniaColumn;
    @FXML
    private TableColumn<OrderHistory, String> wydawnictwoColumn;
    @FXML
    private TableColumn<OrderHistory, String> isbnColumn;
    @FXML
    private TableColumn<OrderHistory, String> iloscColumn;
    @FXML
    private TableColumn<OrderHistory, String> dostawcaColumn;
    @FXML
    private TableColumn<OrderHistory, String> adresColumn;

    private ObservableList<OrderHistory> orderHistoryList = FXCollections.observableArrayList();

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize() {
        ordersButton.setOnMouseClicked(this::otworzZamowienia);
        usersButton.setOnMouseClicked(this::otworzUzytkownikow);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        logoutButton.setOnMouseClicked(this::wyloguj);
        returnButton.setOnMouseClicked(this::otworzZamowienia);
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        tytulColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTytul()));
        autorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor()));
        dataDodaniaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataDodania()));
        wydawnictwoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWydawnictwo()));
        isbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsbn()));
        iloscColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIlosc()));
        dostawcaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDostawca()));
        adresColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdres()));
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        // Wyświetlanie książek, których ilość nie przekracza 2 egzemplarzy
        String query = "SELECT z.id_zamowienia, k.tytul, a.nazwa, " +
                "YEAR(k.data_dodania) AS data_dodania, k.wydawnictwo, k.ISBN, z.ilosc, " +
                "d.nazwa_dostawcy, d.adres FROM zamowienia z JOIN ksiazki k " +
                "ON z.id_ksiazki = k.id_ksiazki JOIN autorzy a " +
                "ON k.id_autora = a.id_autora JOIN dostawcy d ON z.id_dostawcy = d.id_dostawcy;";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String id = resultSet.getString("id_zamowienia");
                String tytul = resultSet.getString("tytul");
                String autor = resultSet.getString("nazwa");
                String data_dodania = resultSet.getString("data_dodania");
                String wydawnictwo = resultSet.getString("wydawnictwo");
                String isbn = resultSet.getString("ISBN");
                String ilosc = resultSet.getString("ilosc");
                String dostawca = resultSet.getString("nazwa_dostawcy");
                String adres = resultSet.getString("adres");
                orderHistoryList.add(new OrderHistory(id, tytul, autor, data_dodania, wydawnictwo, isbn, ilosc, dostawca, adres));
            }

            ordersTable.setItems(orderHistoryList);

        } catch (Exception e) {
            e.printStackTrace();
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
