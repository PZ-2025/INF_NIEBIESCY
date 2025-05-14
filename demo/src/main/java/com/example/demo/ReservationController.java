package com.example.demo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.*;

public class ReservationController {

    private Czytelnik aktualnyCzytelnik;
    public void setAktualnyCzytelnik(Czytelnik czytelnik) {
        this.aktualnyCzytelnik = czytelnik;
    }

    @FXML
    private Label historyButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label reservationButton;
    @FXML
    private Label logoutButton;
    @FXML private Button rezerwuj;

    @FXML private TableView<WypozyczNOW> tableBooks;
    @FXML private TableColumn<WypozyczNOW, String> tytul;
    @FXML private TableColumn<WypozyczNOW, String> autor;
    @FXML private TableColumn<WypozyczNOW, String> gatunek;
    @FXML private TableColumn<WypozyczNOW, String> dostepne;
    @FXML private TableColumn<WypozyczNOW, Boolean> checkbox;

    public void initialize() {
        historyButton.setOnMouseClicked(this::otworzHistorie);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        reservationButton.setOnMouseClicked(this::otworzRezerwacje);
        logoutButton.setOnMouseClicked(this::wyloguj);

        // Kolumny tekstowe
        tytul.setCellValueFactory(cellData -> cellData.getValue().tytulProperty());
        autor.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        gatunek.setCellValueFactory(cellData -> cellData.getValue().gatunekProperty());
        dostepne.setCellValueFactory(cellData -> cellData.getValue().dostepneProperty());

        // Kolumna checkbox
        checkbox.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        checkbox.setCellFactory(tc -> new CheckBoxTableCell<>(index ->
                tableBooks.getItems().get(index).selectedProperty()
        ));

        // Połączenie z bazą
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getConnection();

        rezerwuj.setOnAction(event -> rezerwuj(event, conn));

        // Załaduj dane
        ObservableList<WypozyczNOW> dane = getKsiazkiZBazy(conn);
        tableBooks.setItems(dane);

        tableBooks.setEditable(true);
        checkbox.setEditable(true);

    }

    private void rezerwuj(javafx.event.ActionEvent actionEvent, Connection conn) {
        StringBuilder selectedBooks = new StringBuilder("Zarezerwowano: ");
        boolean anySelected = false;

        String getIdSql = "SELECT id_ksiazki FROM ksiazki WHERE tytul = ?";
        String insertSql = """
        INSERT INTO wypozyczenia (id_ksiazki, id_czytelnika, data_wypozyczenia, data_oddania)
        VALUES (?, ?, NOW(), NULL)
    """;

        try (
                PreparedStatement getIdStmt = conn.prepareStatement(getIdSql);
                PreparedStatement insertStmt = conn.prepareStatement(insertSql)
        ) {
            for (WypozyczNOW ksiazka : tableBooks.getItems()) {
                if (ksiazka.isSelected()) {
                    String tytul = ksiazka.getTytul();

                    getIdStmt.setString(1, tytul);
                    ResultSet rs = getIdStmt.executeQuery();

                    if (rs.next()) {
                        int idKsiazki = rs.getInt("id_ksiazki");

                        insertStmt.setInt(1, idKsiazki);
                        insertStmt.setInt(2, aktualnyCzytelnik.getId());
                        insertStmt.executeUpdate();

                        selectedBooks.append(tytul).append(", ");
                        anySelected = true;
                    } else {
                        System.err.println("Nie znaleziono ID dla książki: " + tytul);
                    }
                }
            }

            if (anySelected) {
                System.out.println(selectedBooks.substring(0, selectedBooks.length() - 2));
            } else {
                System.out.println("Brak zaznaczonych książek do rezerwacji.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ObservableList<WypozyczNOW> getKsiazkiZBazy(Connection conn) {
        ObservableList<WypozyczNOW> lista = FXCollections.observableArrayList();

        String sql = """
    SELECT k.tytul, a.nazwa, g.nazwa_gatunku, 
           (k.ilosc - COALESCE(kw.ilosc_wypozyczonych, 0)) AS dostepne
    FROM ksiazki k
    JOIN autorzy a ON k.id_autora = a.id_autora
    JOIN gatunek g ON k.id_gatunku = g.id_gatunku
    LEFT JOIN ksiazki_wypozyczone kw ON k.id_ksiazki = kw.id_ksiazki
    """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String tytul = rs.getString("tytul");
                String autor = rs.getString("nazwa");
                String gatunek = rs.getString("nazwa_gatunku");
                String dostepne = rs.getString("dostepne");

                lista.add(new WypozyczNOW(tytul, autor, gatunek, dostepne));
            }

        } catch (SQLException e) {
            System.err.println("Błąd SQL: " + e.getMessage());
        }

        return lista;
    }


    private void otworzHistorie(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("czytelnik-historia-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku czytelnik-historia-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
//            AvailableBooksController controller = fxmlLoader.getController();
//            controller.setAktualnyCzytelnik(aktualnyCzytelnik);

            Stage stage = (Stage) historyButton.getScene().getWindow();
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("czytelnik-dostepne-ksiazki-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku czytelnik-dostepne-ksiazki-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
//            AvailableBooksController controller = fxmlLoader.getController();
//            controller.setAktualnyCzytelnik(aktualnyCzytelnik);

            Stage stage = (Stage) booksButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd wczytywania pliku FXML");
        }
    }

    private void otworzRezerwacje(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("czytelnik-rezerwacja-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku czytelnik-rezerwacja-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            AvailableBooksController controller = fxmlLoader.getController();
            controller.setAktualnyCzytelnik(aktualnyCzytelnik);

            Stage stage = (Stage) reservationButton.getScene().getWindow();
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
