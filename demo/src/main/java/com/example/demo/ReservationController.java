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
        VALUES (?, ?, CURRENT_DATE(), NULL)
        """;

        String getIloscSql = "SELECT ilosc FROM ksiazki WHERE tytul = ?";
        String getWypozyczoneSql = "SELECT ilosc_wypozyczonych FROM ksiazki_wypozyczone WHERE id_ksiazki = ?";
        String insertWypozyczoneSql = """
        INSERT INTO ksiazki_wypozyczone (id_ksiazki, ilosc_wypozyczonych, ilosc_calkowita)
        VALUES (?, ?, ?)
        """;
        String updateWypozyczoneSql = """
        UPDATE ksiazki_wypozyczone
        SET ilosc_wypozyczonych = ?
        WHERE id_ksiazki = ?
        """;
        try (
                // Wykonanie zapytan
                PreparedStatement getIdStmt = conn.prepareStatement(getIdSql);
                PreparedStatement insertWypozyczenieStmt = conn.prepareStatement(insertSql,  Statement.RETURN_GENERATED_KEYS);

                PreparedStatement getIloscStmt = conn.prepareStatement(getIloscSql);
                PreparedStatement getWypozyczoneKsiazkiStmt = conn.prepareStatement(getWypozyczoneSql);
                PreparedStatement insertWypozyczoneKsiazkiStmt = conn.prepareStatement(insertWypozyczoneSql);
                PreparedStatement updateWypozyczoneKsiazkiStmt = conn.prepareStatement(updateWypozyczoneSql)
        ) {
            for (WypozyczNOW ksiazka : tableBooks.getItems()) {
                if (ksiazka.isSelected()) {
                    String tytul = ksiazka.getTytul();

                    getIdStmt.setString(1, tytul);
                    ResultSet rs = getIdStmt.executeQuery();

                    if (rs.next()) {
                        int idKsiazki = rs.getInt("id_ksiazki");

                        // Pobierz ilość z tabeli ksiazki
                        getIloscStmt.setString(1, tytul);
                        ResultSet rsIlosc = getIloscStmt.executeQuery();
                        int iloscCalkowita = rsIlosc.next() ? rsIlosc.getInt("ilosc") : 0;

                        // Sprawdź czy książka już wypożyczona
                        getWypozyczoneKsiazkiStmt.setInt(1, idKsiazki);
                        ResultSet rsWyp = getWypozyczoneKsiazkiStmt.executeQuery();

                        int iloscWypozyczonych = 0;
                        boolean istniejeRekordWKsiazkiWypozyczone = false;
                        if (rsWyp.next()) {
                            iloscWypozyczonych = rsWyp.getInt("ilosc_wypozyczonych");
                            istniejeRekordWKsiazkiWypozyczone = true;
                        }

                        // SPRAWDZENIE LIMITU
                        if (iloscWypozyczonych >= iloscCalkowita) {
                            System.out.printf("Brak dostępnych egzemplarzy: [id_ksiazki=%d, ilosc=%d, wypozyczonych=%d]%n",
                                    idKsiazki, iloscCalkowita, iloscWypozyczonych);
                            continue; // Pomija tę książkę
                        }

                        // Wstaw wypożyczenie
                        insertWypozyczenieStmt.setInt(1, idKsiazki);
                        insertWypozyczenieStmt.setInt(2, aktualnyCzytelnik.getId());
                        insertWypozyczenieStmt.executeUpdate();

                        if (istniejeRekordWKsiazkiWypozyczone) {
                            // UPDATE
                            iloscWypozyczonych += 1;
                            updateWypozyczoneKsiazkiStmt.setInt(1, iloscWypozyczonych);
                            updateWypozyczoneKsiazkiStmt.setInt(2, idKsiazki);
                            updateWypozyczoneKsiazkiStmt.executeUpdate();
                        } else {
                            // INSERT
                            insertWypozyczoneKsiazkiStmt.setInt(1, idKsiazki);
                            insertWypozyczoneKsiazkiStmt.setInt(2, 1); // pierwsze wypożyczenie
                            insertWypozyczoneKsiazkiStmt.setInt(3, iloscCalkowita);
                            insertWypozyczoneKsiazkiStmt.executeUpdate();
                        }

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
