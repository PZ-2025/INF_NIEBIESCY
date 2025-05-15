package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoanHistoryController {
    @FXML
    private Label historyButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label reservationButton;
    @FXML
    private Label logoutButton;
    @FXML private TableView<Wypozyczenia> tableHistory;
    @FXML private TableColumn<Wypozyczenia, String> tytul;
    @FXML private TableColumn<Wypozyczenia, String> gatunek;
    @FXML private TableColumn<Wypozyczenia, String> autor;
    @FXML private TableColumn<Wypozyczenia, String> data_wyp;
    @FXML private TableColumn<Wypozyczenia, String> data_odda;

    private Czytelnik aktualnyCzytelnik;
    public void setAktualnyCzytelnik(Czytelnik czytelnik) {
        this.aktualnyCzytelnik = czytelnik;
        loadData();
    }

    private void loadData() {
        if (aktualnyCzytelnik == null) return;

        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getConnection();

        ObservableList<Wypozyczenia> dane = getHistoriaZBazy(conn);
        tableHistory.setItems(dane);
    }

    public void initialize() {
        historyButton.setOnMouseClicked(this::otworzHistorie);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        reservationButton.setOnMouseClicked(this::otworzRezerwacje);
        logoutButton.setOnMouseClicked(this::wyloguj);

        tytul.setCellValueFactory(cellData -> cellData.getValue().tytulProperty());
        gatunek.setCellValueFactory(cellData -> cellData.getValue().gatunekProperty());
        autor.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        data_wyp.setCellValueFactory(cellData -> cellData.getValue().data_wypProperty());
        data_odda.setCellValueFactory(cellData -> cellData.getValue().data_oddaProperty());
    }

    public ObservableList<Wypozyczenia> getHistoriaZBazy(Connection conn) {
        ObservableList<Wypozyczenia> lista = FXCollections.observableArrayList();

        String sql = """
    SELECT k.tytul,g.nazwa_gatunku, a.nazwa, w.data_wypozyczenia , w.data_oddania
    FROM ksiazki k
    JOIN autorzy a ON k.id_autora = a.id_autora
    JOIN gatunek g ON k.id_gatunku = g.id_gatunku
    JOIN wypozyczenia w ON k.id_ksiazki = w.id_ksiazki
    JOIN czytelnicy c ON w.id_czytelnika = c.id_czytelnika
    WHERE c.id_czytelnika = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, aktualnyCzytelnik.getId()); // lub inny sposób pobrania ID
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String tytul = rs.getString("tytul");
                String gatunek = rs.getString("nazwa_gatunku");
                String autor = rs.getString("nazwa");
                String data_wypozyczenia = rs.getString("data_wypozyczenia");
                String data_oddania = rs.getString("data_oddania");

                lista.add(new Wypozyczenia(tytul,gatunek, autor, data_wypozyczenia, data_oddania));
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
            LoanHistoryController controller = fxmlLoader.getController();
            controller.setAktualnyCzytelnik(aktualnyCzytelnik);

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
            AvailableBooksController controller = fxmlLoader.getController();
            controller.setAktualnyCzytelnik(aktualnyCzytelnik);

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
            ReservationController controller = fxmlLoader.getController();
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
