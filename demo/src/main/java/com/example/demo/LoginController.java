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


import java.io.IOException;

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

    public void initialize() {
        rejestracjaLabel.setOnMouseClicked(this::otworzRejestracje);
    }

    public void loginButtonOnAction(ActionEvent event) {
        String username = loginTextField.getText();
        String password = hasloPasswordField.getText();
        komunikatLabel.setTextFill(Color.valueOf("#ff6363"));

        if (username.isEmpty() || password.isEmpty()) {
            komunikatLabel.setText("Wszystkie pola muszą być wypełnione");
        } else if (username.equals("admin") && password.equals("admin123")) {
            komunikatLabel.setTextFill(Color.GREEN);
            komunikatLabel.setText("Zostales zalogowany jako " + username);
        } else {
            komunikatLabel.setText("Niepoprawny login lub hasło");
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