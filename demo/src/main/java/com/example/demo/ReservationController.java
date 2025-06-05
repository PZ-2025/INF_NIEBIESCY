package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class ReservationController {

    public DatePicker data_picker;
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
        LocalDate selectedDate = data_picker.getValue();
        LocalDate today = LocalDate.now();

        if (!isValidDate(selectedDate, today)) {
            System.out.println("Nieprawidłowa data");
            return;
        }

        try {
            boolean anySelected = false;
            StringBuilder selectedBooks = new StringBuilder("Zarezerwowano: ");

            for (WypozyczNOW ksiazka : tableBooks.getItems()) {
                if (ksiazka.isSelected()) {
                    boolean success = processBookReservation(conn, ksiazka.getTytul(), selectedDate);
                    if (success) {
                        selectedBooks.append(ksiazka.getTytul()).append(", ");
                        anySelected = true;
                    }
                }
            }

            if (anySelected) {
                System.out.println(selectedBooks.substring(0, selectedBooks.length() - 2));
            } else {
                System.out.println("Brak zaznaczonych książek do rezerwacji.");
            }
            odswiezListeKsiazek(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void odswiezListeKsiazek(Connection conn) {
        ObservableList<WypozyczNOW> noweDane = getKsiazkiZBazy(conn);
        tableBooks.setItems(noweDane);
    }

    private boolean isValidDate(LocalDate selectedDate, LocalDate today) {
        return selectedDate != null && !selectedDate.isBefore(today);
    }

    private boolean processBookReservation(Connection conn, String tytul, LocalDate selectedDate) throws SQLException {
        Integer idKsiazki = getBookId(conn, tytul);
        if (idKsiazki == null) {
            System.err.println("Nie znaleziono ID dla książki: " + tytul);
            return false;
        }

        int iloscCalkowita = getBookTotalQuantity(conn, tytul);
        int iloscWypozyczonych = getRentedQuantity(conn, idKsiazki);

        if (iloscWypozyczonych >= iloscCalkowita) {
            System.out.printf("Brak dostępnych egzemplarzy: %s (ID: %d)%n", tytul, idKsiazki);
            return false;
        }

        insertReservation(conn, idKsiazki, aktualnyCzytelnik.getId(), selectedDate);
        updateRentedBooks(conn, idKsiazki, iloscWypozyczonych, iloscCalkowita);

        return true;
    }

    private Integer getBookId(Connection conn, String tytul) throws SQLException {
        String sql = "SELECT id_ksiazki FROM ksiazki WHERE tytul = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tytul);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id_ksiazki");
        }
        return null;
    }

    private int getBookTotalQuantity(Connection conn, String tytul) throws SQLException {
        String sql = "SELECT ilosc FROM ksiazki WHERE tytul = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tytul);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("ilosc");
        }
        return 0;
    }

    private int getRentedQuantity(Connection conn, int idKsiazki) throws SQLException {
        String sql = "SELECT ilosc_wypozyczonych FROM ksiazki_wypozyczone WHERE id_ksiazki = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKsiazki);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("ilosc_wypozyczonych");
        }
        return 0;
    }

    private void insertReservation(Connection conn, int idKsiazki, int idCzytelnika, LocalDate planowanaData) throws SQLException {
        String sql = """
        INSERT INTO rezerwacje (id_ksiazki, id_czytelnika, data_zamowienia, planowana_data, status)
        VALUES (?, ?, CURRENT_DATE(), ?, false)
    """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKsiazki);
            stmt.setInt(2, idCzytelnika);
            stmt.setDate(3, java.sql.Date.valueOf(planowanaData));
            stmt.executeUpdate();
        }
    }

    private void updateRentedBooks(Connection conn, int idKsiazki, int iloscWypozyczonych, int iloscCalkowita) throws SQLException {
        String checkSql = "SELECT 1 FROM ksiazki_wypozyczone WHERE id_ksiazki = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, idKsiazki);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Aktualizacja
                String updateSql = "UPDATE ksiazki_wypozyczone SET ilosc_wypozyczonych = ? WHERE id_ksiazki = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, iloscWypozyczonych + 1);
                    updateStmt.setInt(2, idKsiazki);
                    updateStmt.executeUpdate();
                }
            } else {
                // Wstawianie nowego rekordu
                String insertSql = "INSERT INTO ksiazki_wypozyczone (id_ksiazki, ilosc_wypozyczonych, ilosc_calkowita) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, idKsiazki);
                    insertStmt.setInt(2, 1);
                    insertStmt.setInt(3, iloscCalkowita);
                    insertStmt.executeUpdate();
                }
            }
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
