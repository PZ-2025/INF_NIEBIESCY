package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationController {

    @FXML
    private Button guestButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label komunikatLabel;
    @FXML
    private TextField imieTextField;
    @FXML
    private TextField nazwiskoTextField;
    @FXML
    private PasswordField hasloPasswordField;
    @FXML
    private PasswordField haslo2PasswordField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField telefonTextField;
    @FXML
    private TextField miastoTextField;
    @FXML
    private TextField adresTextField;
    @FXML
    private Label mamKontoLabel;

    public void initialize() {
        mamKontoLabel.setOnMouseClicked(this::otworzRejestracje);
    }

    public void registerButtonOnAction(ActionEvent event) {
        String imie = imieTextField.getText();
        String nazwisko = nazwiskoTextField.getText();
        String email = emailTextField.getText();
        String password = hasloPasswordField.getText();
        String password2 = haslo2PasswordField.getText();
        String telefon = telefonTextField.getText();
        String miasto = (miastoTextField.getText());
        String adres = adresTextField.getText();
        komunikatLabel.setTextFill(Color.valueOf("#ff6363"));
        if (imie.isEmpty() || nazwisko.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty() || miasto.isEmpty() || adres.isEmpty()) {
            komunikatLabel.setText("Wszystkie pola muszą być wypełnione");
        } else if (!password.equals(password2)) {
            komunikatLabel.setText("Hasła się różnią");
        } else if (email.equals("admin")) {
            komunikatLabel.setText("Email jest już zajęty");
        } else {
            komunikatLabel.setTextFill(Color.GREEN);
            komunikatLabel.setText("Pomyslnie zarejestrowano");

            DatabaseConnection conectNow = new DatabaseConnection();
            Connection connectDB = conectNow.getConnection();

            String query = "INSERT INTO czytelnicy (imie, nazwisko, tel, email, haslo, ulica, miasto) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connectDB.prepareStatement(query)) {
                // Zabezpieczamy hasło przez haszowanie (np. za pomocą BCrypt)
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                // Ustawiamy wartości parametrów zapytania
                preparedStatement.setString(1, imie);       // Ustawiamy imię
                preparedStatement.setString(2, nazwisko);   // Ustawiamy nazwisko
                preparedStatement.setLong(3, Long.parseLong(telefon));      // Ustawiamy telefon (bigint)
                preparedStatement.setString(4, email);      // Ustawiamy email
                preparedStatement.setString(5, hashedPassword);  // Ustawiamy hasło (zaszyfrowane)
                preparedStatement.setString(6, adres);     // Ustawiamy ulicę (adres)
                preparedStatement.setString(7, miasto);    // Ustawiamy miasto

                // Wykonujemy zapytanie do bazy danych
                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    komunikatLabel.setText("Zarejestrowano pomyślnie");
                } else {
                    komunikatLabel.setText("Wystąpił problem z rejestracją");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                komunikatLabel.setText("Błąd połączenia z bazą danych");
            }
        }
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku login-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            Stage stage = (Stage) mamKontoLabel.getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public void setRegisterButton(Button registerButton) {
        this.registerButton = registerButton;
    }
}
