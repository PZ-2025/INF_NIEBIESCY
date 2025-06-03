package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LibrarianLoanController {
    @FXML
    private Label reservationsButton;
    @FXML
    private Label loansButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label logoutButton;
    private Pracownik aktualnyPracownik;
    @FXML private TextField emailField;
    @FXML private TextField bookTitleField;
    @FXML private Button borrowButton;
    @FXML private Label statusLabel;
    @FXML private TableView<UserViewModel> usersTable;
    @FXML private TableColumn<UserViewModel, String> imieColumn;
    @FXML private TableColumn<UserViewModel, String> nazwiskoColumn;
    @FXML private TableColumn<UserViewModel, String> emailColumn;
    @FXML private TableColumn<UserViewModel, String> wypozyczeniaColumn;
    @FXML private TableColumn<UserViewModel, String> rezerwacjeColumn;

    private LoanDAO loanDAO;

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize() {
        initializeTable();

        reservationsButton.setOnMouseClicked(this::otworzRezerwacje);
        loansButton.setOnMouseClicked(this::otworzWypozyczenia);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        logoutButton.setOnMouseClicked(this::wyloguj);

        loanDAO = new LoanDAO(new DatabaseConnection().getConnection());

        borrowButton.setOnAction(event -> {
            String email = emailField.getText().trim();
            String tytul = bookTitleField.getText().trim();

            if (email.isEmpty() || tytul.isEmpty()) {
                statusLabel.setText(" Uzupełnij wszystkie pola.");
                return;
            }

            boolean success = loanDAO.wypozyczKsiazke(email, tytul);
            if (success) {
                statusLabel.setText(" Wypożyczenie zakończone sukcesem.");
            } else {
                statusLabel.setText(" Wypożyczenie nie powiodło się.");
            }
            usersTable.setItems(UserDAO.getAllUsers());
        });
    }


    private void initializeTable() {
        imieColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getImie()));
        nazwiskoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNazwisko()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        wypozyczeniaColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getLiczbaWypozyczen())));
        rezerwacjeColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getLiczbaRezerwacji())));


        usersTable.setItems(UserDAO.getAllUsers());
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
