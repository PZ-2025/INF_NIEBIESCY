/*
 * Kontroler dla panelu logowania
 */
package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML
    private Button guestButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label komunikatLabel;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField hasloPasswordField;
    @FXML
    private Label rejestracjaLabel;

    private DatabaseConnection databaseConnection;

    public LoginController() {
        databaseConnection = new DatabaseConnection();
    }

    public void initialize() {
        rejestracjaLabel.setOnMouseClicked(this::otworzRejestracje);
    }

    public void loginButtonOnAction(ActionEvent event) {
        String username = loginTextField.getText();
        String password = hasloPasswordField.getText();
        komunikatLabel.setTextFill(Color.valueOf("#ff6363"));

        if (username.isEmpty() || password.isEmpty()) {
            komunikatLabel.setText("Wszystkie pola muszą być wypełnione");
        }

        Czytelnik czytelnik = authenticateUser(username, password);

        if (czytelnik != null) {
            komunikatLabel.setTextFill(Color.GREEN);
            komunikatLabel.setText("Zostales zalogowany jako " + czytelnik.getImie() + " " + czytelnik.getNazwisko());

            try {
                // Ładowanie widoku czytelnik-dostepne-ksiazki-view.fxml
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("czytelnik-dostepne-ksiazki-view.fxml"));
                if (fxmlLoader.getLocation() == null) {
                    System.out.println("Błąd: Nie znaleziono pliku czytelnik-dostepne-ksiazki-view.fxml");
                    return;
                }
                // Załaduj widok do nowej sceny
                BorderPane root = fxmlLoader.load();
                AvailableBooksController controller = fxmlLoader.getController();
                controller.setAktualnyCzytelnik(czytelnik);

                // Pobranie bieżącego okna i ustawienie nowej sceny
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root, 1000, 600);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Błąd wczytywania pliku FXML");
            }
            return;
        } else {
            // Próba logowania pracownika
            Pracownik pracownik = authenticateEmployee(username, password);

            if (pracownik != null) {
                komunikatLabel.setTextFill(Color.GREEN);
                komunikatLabel.setText("Zostałeś zalogowany jako pracownik: " + pracownik.getImie() + " " + pracownik.getNazwisko() + " (" + pracownik.getRola() + ")");

                try {
                    String rola = pracownik.getRola();
                    if("admin".equalsIgnoreCase(rola)){
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-zamowienia.fxml"));
                        BorderPane root = fxmlLoader.load();
                        AdminOrdersController controller = fxmlLoader.getController();
                        controller.setAktualnyPracownik(pracownik);

                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        Scene scene = new Scene(root, 1000, 600);
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bibliotekarz-rezerwacje-view.fxml"));
                        BorderPane root = fxmlLoader.load();
                        LibrarianReservationsController controller = fxmlLoader.getController();
                        controller.setAktualnyPracownik(pracownik);

                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        Scene scene = new Scene(root, 1000, 600);
                        stage.setScene(scene);
                        stage.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Błąd wczytywania pliku FXML dla pracownika");
                }
                return;
            } else {
                // Jeśli nikt nie pasuje
                komunikatLabel.setText("Niepoprawny email lub hasło");
            }
        }
    }

    private Czytelnik authenticateUser(String login, String haslo) {
        String query = "SELECT * FROM czytelnicy WHERE email = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("haslo");  // Pobieramy zahaszowane hasło z bazy

                // Sprawdzamy, czy hasło pasuje do zahaszowanego hasła
                if (BCrypt.checkpw(haslo, storedHashedPassword)) {
                    int id = resultSet.getInt("id_czytelnika");
                    String imie = resultSet.getString("imie");
                    String nazwisko = resultSet.getString("nazwisko");
                    // Tworzymy obiekt Czytelnik
                    return new Czytelnik(id, login, storedHashedPassword, imie, nazwisko);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Pracownik authenticateEmployee(String login, String haslo) {
        System.out.println("Wywołano authenticateEmployee dla: " + login);
        String query = "SELECT * FROM pracownicy WHERE email = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("haslo");
                if (BCrypt.checkpw(haslo, storedHashedPassword)) {
                    int id = resultSet.getInt("id_pracownika");
                    String imie = resultSet.getString("imie");
                    String nazwisko = resultSet.getString("nazwisko");
                    String rola = resultSet.getString("rola");

                    return new Pracownik(id, login, storedHashedPassword, imie, nazwisko, rola);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void guestButtonOnAction(ActionEvent event)  {
        try {
            // Ładowanie widoku guest-view.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("guest-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku guest-view.fxml");
                return;
            }
            // Załaduj widok do nowej sceny
            BorderPane root = fxmlLoader.load();

            // Pobranie bieżącego okna i ustawienie nowej sceny
            Stage stage = (Stage) guestButton.getScene().getWindow();
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
            Stage stage = (Stage) rejestracjaLabel.getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

}