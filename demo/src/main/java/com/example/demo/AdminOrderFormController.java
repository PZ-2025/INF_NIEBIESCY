package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminOrderFormController {
    @FXML
    private Button orderButton;
    @FXML
    private Button returnButton;
    private Pracownik aktualnyPracownik;
    @FXML
    private TextField bookTitleField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField supplierField;

    private Connection connection;

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize(){
        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();

        orderButton.setOnAction(e -> placeOrder());
        returnButton.setOnMouseClicked(this::wroc);
    }

    private void placeOrder() {
        String bookTitle = bookTitleField.getText();
        String quantityText = quantityField.getText();
        String supplierName = supplierField.getText();

        if (bookTitle.isEmpty() || quantityText.isEmpty() || supplierName.isEmpty()) {
            showAlert("Brak danych", "Uzupełnij wszystkie pola formularza.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert("Niepoprawna ilość", "Podaj prawidłową liczbę w polu 'Ilość'.");
            return;
        }

        try {
            int bookId = getBookId(bookTitle);
            int supplierId = getSupplierId(supplierName);

            if (bookId == -1) {
                showAlert("Nie znaleziono książki", "Brak książki o tytule: " + bookTitle);
                return;
            }

            if (supplierId == -1) {
                showAlert("Nie znaleziono dostawcy", "Brak dostawcy o nazwie: " + supplierName);
                return;
            }

            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String insertQuery = "INSERT INTO zamowienia (id_ksiazki, id_dostawcy, data_dodania, ilosc) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(insertQuery);
            ps.setInt(1, bookId);
            ps.setInt(2, supplierId);
            ps.setString(3, currentDateTime);
            ps.setInt(4, quantity);

            ps.executeUpdate();
            showAlert("Sukces", "Zamówienie zostało dodane.");

            // opcjonalnie wyczyść formularz
            bookTitleField.clear();
            quantityField.clear();
            supplierField.clear();

        } catch (SQLException e) {
            showAlert("Błąd SQL", e.getMessage());
        }
    }

    private int getBookId(String title) throws SQLException {
        String query = "SELECT id_ksiazki FROM ksiazki WHERE tytul = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, title);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_ksiazki");
        }
        return -1;
    }

    private int getSupplierId(String name) throws SQLException {
        String query = "SELECT id_dostawcy FROM dostawcy WHERE nazwa_dostawcy = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_dostawcy");
        }
        return -1;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void wroc(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-zamowienia.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku admin-zamowienia.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            AdminOrdersController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) returnButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }
}
