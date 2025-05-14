/*
 * Kontroler dla panelu rejestracji
 */
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

    // Funkcja do dodawnia wielu uzytkownikow
//    public void registerMultipleUsers() {
//        // Definicja użytkowników w tablicy (25 użytkowników)
//        String[][] users = {
//                {"Ewa", "Kowalska", "ewa@wp.pl", "654321987", "Zielona 5", "Sanok", "ewa", "ewa"},
//                {"Marek", "Zielinski", "marek@wp.pl", "112233445", "Brzozowa 9", "Przemysl", "marek", "marek"},
//                {"Anna", "Wojcik", "anna@wp.pl", "667788990", "Sosnowa 12", "Mielec", "anna", "anna"},
//                {"Piotr", "Szymanski", "piotr@wp.pl", "223344556", "Jasna 42", "Tarnobrzeg", "piotr", "piotr"},
//                {"Monika", "Kwiatkowska", "monika@wp.pl", "556677889", "Pilsudskiego 7", "Debica", "monika", "monika"},
//                {"Jan", "Kowalczyk", "jan@wp.pl", "776655443", "Lesna 13", "Zamosc", "jan", "jan"},
//                {"Iwona", "Pawlak", "iwona@wp.pl", "998877665", "Laskowa 25", "Lublin", "iwona", "iwona"},
//                {"Tomasz", "Kaczmarek", "tomasz@wp.pl", "334455667", "Gorzyska 12", "Kielce", "tomasz", "tomasz"},
//                {"Barbara", "Wozniak", "barbara@wp.pl", "445566778", "Dabrowskiego 8", "Nowy Sącz", "barbara", "barbara"},
//                {"Zbigniew", "Borkowski", "zbigniew@wp.pl", "556677889", "Zielona 3", "Lubin", "zbigniew", "zbigniew"},
//                {"Katarzyna", "Wojciechowska", "katarzyna@wp.pl", "667788990", "Olsztynska 16", "Wroclaw", "katarzyna", "katarzyna"},
//                {"Wojciech", "Kwiatkowski", "wojciech@wp.pl", "778899112", "Miejska 45", "Gorzów Wlkp.", "wojciech", "wojciech"},
//                {"Jolanta", "Mazur", "jolanta@wp.pl", "889900223", "Nowa 29", "Opole", "jolanta", "jolanta"},
//                {"Paweł", "Czarnecki", "pawel@wp.pl", "998811334", "Mickiewicza 51", "Katowice", "pawel", "pawel"},
//                {"Beata", "Zielinska", "beata@wp.pl", "223344556", "Wiosny Ludow 19", "Wloclawek", "beata", "beata"},
//                {"Rafał", "Kamiński", "rafal@wp.pl", "667788990", "Szeroka 3", "Bialystok", "rafal", "rafal"},
//                {"Małgorzata", "Michałowska", "malgorzata@wp.pl", "334455667", "Polna 8", "Gdynia", "malgorzata", "malgorzata"},
//                {"Waldemar", "Kaczmarek", "waldemar@wp.pl", "112233445", "Rynkowska 45", "Olsztyn", "waldemar", "waldemar"},
//                {"Lena", "Jasińska", "lena@wp.pl", "223344556", "Sosnowa 12", "Zabrze", "lena", "lena"},
//                {"Piotr", "Nowakowski", "piotr2@wp.pl", "332211445", "Jagiellonska 34", "Gdansk", "piotr", "piotr"},
//                {"Agnieszka", "Sikora", "agnieszka@wp.pl", "667788990", "Kwiatowa 6", "Bielsko-Biala", "agnieszka", "agnieszka"},
//                {"Grzegorz", "Jankowski", "grzegorz@wp.pl", "998877665", "Wolności 22", "Lodz", "grzegorz", "grzegorz"},
//                {"Aleksandra", "Kalinowska", "aleksandra@wp.pl", "223344556", "Wysoka 17", "Szczecin", "aleksandra", "aleksandra"},
//                {"Tadeusz", "Cieślak", "tadeusz@wp.pl", "112233445", "Dębowa 20", "Kraków", "tadeusz", "tadeusz"}
//        };
//
//        DatabaseConnection conectNow = new DatabaseConnection();
//        Connection connectDB = conectNow.getConnection();
//        String query = "INSERT INTO czytelnicy (imie, nazwisko, tel, email, haslo, ulica, miasto) VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//        try (PreparedStatement preparedStatement = connectDB.prepareStatement(query)) {
//            for (String[] user : users) {
//                // Haszowanie hasła (BCrypt)
//                String hashedPassword = BCrypt.hashpw(user[6], BCrypt.gensalt());
//
//                // Ustawianie wartości parametrów zapytania
//                preparedStatement.setString(1, user[0]);       // imie
//                preparedStatement.setString(2, user[1]);       // nazwisko
//                preparedStatement.setLong(3, Long.parseLong(user[3]));  // telefon
//                preparedStatement.setString(4, user[2]);       // email
//                preparedStatement.setString(5, hashedPassword); // hasło (zaszyfrowane)
//                preparedStatement.setString(6, user[4]);       // adres
//                preparedStatement.setString(7, user[5]);       // miasto
//
//                // Wykonanie zapytania
//                preparedStatement.executeUpdate();
//            }
//            komunikatLabel.setText("Wszyscy użytkownicy zostali zarejestrowani.");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            komunikatLabel.setText("Błąd połączenia z bazą danych");
//        }
//    }


    public void initialize() {
        mamKontoLabel.setOnMouseClicked(this::otworzRejestracje);
//        registerMultipleUsers();

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
