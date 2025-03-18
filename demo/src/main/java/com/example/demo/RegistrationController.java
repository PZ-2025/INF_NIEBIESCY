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
import javafx.scene.paint.Color;

import java.io.IOException;

public class RegistrationController {

    @FXML
    private Button guestButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label komunikatLabel;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField hasloPasswordField;
    @FXML
    private PasswordField haslo2PasswordField;
    @FXML
    private TextField emailTextField;
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
        String login = loginTextField.getText();
        String password = hasloPasswordField.getText();
        String password2 = haslo2PasswordField.getText();
        String email = emailTextField.getText();
        String miasto = (miastoTextField.getText());
        String adres = adresTextField.getText();
        komunikatLabel.setTextFill(Color.valueOf("#ff6363"));
        if (login.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty() || miasto.isEmpty() || adres.isEmpty()) {
            komunikatLabel.setText("Wszystkie pola muszą być wypełnione");
        } else if (!password.equals(password2)) {
            komunikatLabel.setText("Hasła się różnią");
        } else if (login.equals("admin")){
            komunikatLabel.setText("Login jest już zajęty");
        } else if (email.equals("admin")) {
            komunikatLabel.setText("Email jest już zajęty");
        } else {
            komunikatLabel.setTextFill(Color.GREEN);
            komunikatLabel.setText("Pomyslnie zarejestrowano");
        }
    }

    public void guestButtonOnAction(ActionEvent event)  {
        Stage stage = (Stage) guestButton.getScene().getWindow();
        stage.close();
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
}
