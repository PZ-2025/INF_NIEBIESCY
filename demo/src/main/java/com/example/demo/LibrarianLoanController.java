package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Comparator;

public class LibrarianLoanController {
    public ComboBox czytelnikBox;
    @FXML
    private Label reservationsButton;
    @FXML
    private Label loansButton;
    @FXML
    private Label zwrotyButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label logoutButton;
    private Pracownik aktualnyPracownik;

    @FXML private Button wypButton;

    @FXML private TableView<WypozyczNOW> tableBooks;
    @FXML private TableColumn<WypozyczNOW, String> tytul;
    @FXML private TableColumn<WypozyczNOW, String> autor;
    @FXML private TableColumn<WypozyczNOW, String> gatunek;
    @FXML private TableColumn<WypozyczNOW, String> dostepne;
    @FXML private TableColumn<WypozyczNOW, Boolean> checkbox;

    @FXML private TableView<WypozyczNOW> tableBooksRezerwiren;
    @FXML private TableColumn<WypozyczNOW, String> tytulRezerwiren;
    @FXML private TableColumn<WypozyczNOW, String> autorRezerwiren;
    @FXML private TableColumn<WypozyczNOW, String> gatunekRezerwiren;
    @FXML private TableColumn<WypozyczNOW, String> dostepneRezerwiren;
    @FXML private TableColumn<WypozyczNOW, Boolean> checkboxRezerwiren;

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize() {
        // Połączenie z bazą
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getConnection();

        reservationsButton.setOnMouseClicked(this::otworzRezerwacje);
        zwrotyButton.setOnMouseClicked(this::otworzZwroty);
        loansButton.setOnMouseClicked(this::otworzWypozyczenia);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        logoutButton.setOnMouseClicked(this::wyloguj);
        // Kolumny tekstowe
        tytul.setCellValueFactory(cellData -> cellData.getValue().tytulProperty());
        autor.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        gatunek.setCellValueFactory(cellData -> cellData.getValue().gatunekProperty());
        dostepne.setCellValueFactory(cellData -> cellData.getValue().dostepneProperty());

        tytulRezerwiren.setCellValueFactory(cellData -> cellData.getValue().tytulProperty());
        autorRezerwiren.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        gatunekRezerwiren.setCellValueFactory(cellData -> cellData.getValue().gatunekProperty());
        dostepneRezerwiren.setCellValueFactory(cellData -> cellData.getValue().dostepneProperty());

        czytelnikBox.setOnAction(event -> {
            Czytelnik wybrany = (Czytelnik) czytelnikBox.getSelectionModel().getSelectedItem();
            if (wybrany != null) {
                ObservableList<WypozyczNOW> daneRezerwacji = getKsiazkiZarezerwowaneZBazy(conn, wybrany.getId());
                tableBooksRezerwiren.setItems(daneRezerwacji);
            }
        });
        // Kolumna checkbox
        checkbox.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        checkbox.setCellFactory(tc -> new CheckBoxTableCell<>(index ->
                tableBooks.getItems().get(index).selectedProperty()
        ));

        checkboxRezerwiren.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkboxRezerwiren.setCellFactory(tc -> new CheckBoxTableCell<>(index ->
                tableBooksRezerwiren.getItems().get(index).selectedProperty()));

        wypButton.setOnAction(event -> wypozycz(event, conn));

        // Załaduj dane
        ObservableList<WypozyczNOW> dane = getKsiazkiZBazy(conn);
        tableBooks.setItems(dane);

        zaladujCzytelnikowDoComboBox();

        tableBooks.setEditable(true);
        checkbox.setEditable(true);
        tableBooksRezerwiren.setEditable(true);
        checkboxRezerwiren.setEditable(true);
    }

    public void zaladujCzytelnikowDoComboBox() {
        DatabaseConnection connection = new DatabaseConnection();

        String sql = "SELECT id_czytelnika, email, haslo, imie, nazwisko FROM czytelnicy ORDER BY nazwisko, imie";

        try (Connection conn = connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ObservableList<Czytelnik> listaCzytelnikow = FXCollections.observableArrayList();

            while (rs.next()) {
                int id = rs.getInt("id_czytelnika");
                String email = rs.getString("email");
                String haslo = rs.getString("haslo");
                String imie = rs.getString("imie");
                String nazwisko = rs.getString("nazwisko");

                Czytelnik czytelnik = new Czytelnik(id, email, haslo, imie, nazwisko);
                listaCzytelnikow.add(czytelnik);
            }
            listaCzytelnikow.sort(Comparator.comparing(Czytelnik::getNazwisko, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Czytelnik::getImie, String.CASE_INSENSITIVE_ORDER));

            czytelnikBox.setItems(listaCzytelnikow);

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

    public ObservableList<WypozyczNOW> getKsiazkiZarezerwowaneZBazy(Connection conn, int idCzytelnika) {
        ObservableList<WypozyczNOW> listaRezerwiren = FXCollections.observableArrayList();

        String sql = """
        SELECT k.tytul, a.nazwa, g.nazwa_gatunku, (k.ilosc - COALESCE(kw.ilosc_wypozyczonych, 0)) AS dostepne
                FROM rezerwacje r
                JOIN ksiazki k ON r.id_ksiazki = k.id_ksiazki
                JOIN autorzy a ON k.id_autora = a.id_autora
                JOIN gatunek g ON k.id_gatunku = g.id_gatunku
                LEFT JOIN ksiazki_wypozyczone kw ON k.id_ksiazki = kw.id_ksiazki
                WHERE r.id_czytelnika = ? AND r.status = 1;
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCzytelnika);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tytul = rs.getString("tytul");
                String autor = rs.getString("nazwa");
                String gatunek = rs.getString("nazwa_gatunku");
                String dostepne = rs.getString("dostepne");

                listaRezerwiren.add(new WypozyczNOW(tytul, autor, gatunek, dostepne));
            }
        } catch (SQLException e) {
            System.err.println("Błąd SQL: " + e.getMessage());
        }

        return listaRezerwiren;
    }



    private void wypozycz(ActionEvent actionEvent, Connection conn) {
        Czytelnik zaznaczonyCzytelnik = (Czytelnik) czytelnikBox.getSelectionModel().getSelectedItem();
        if (zaznaczonyCzytelnik == null) {
            System.out.println("Nie wybrano czytelnika!");
            return;
        }

        StringBuilder selectedBooks = new StringBuilder("Wypozyczono: ");
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

        String updateStatusRezerwacjiSql = "UPDATE rezerwacje SET status = 2 WHERE id_ksiazki = ? AND id_czytelnika = ?";

        try (
                PreparedStatement getIdStmt = conn.prepareStatement(getIdSql);
                PreparedStatement insertWypozyczenieStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement getIloscStmt = conn.prepareStatement(getIloscSql);
                PreparedStatement getWypozyczoneKsiazkiStmt = conn.prepareStatement(getWypozyczoneSql);
                PreparedStatement insertWypozyczoneKsiazkiStmt = conn.prepareStatement(insertWypozyczoneSql);
                PreparedStatement updateWypozyczoneKsiazkiStmt = conn.prepareStatement(updateWypozyczoneSql);
                PreparedStatement updateStatusRezerwacjiStmt = conn.prepareStatement(updateStatusRezerwacjiSql)
        ) {
            // Wypożyczenie z tableBooks
            for (WypozyczNOW ksiazka : tableBooks.getItems()) {
                if (ksiazka.isSelected()) {
                    boolean success = wypozyczPojedynczaKsiazke(
                            ksiazka.getTytul(),
                            zaznaczonyCzytelnik.getId(),
                            getIdStmt,
                            insertWypozyczenieStmt,
                            getIloscStmt,
                            getWypozyczoneKsiazkiStmt,
                            insertWypozyczoneKsiazkiStmt,
                            updateWypozyczoneKsiazkiStmt,
                            true
                    );
                    if (success) {
                        selectedBooks.append(ksiazka.getTytul()).append(", ");
                        anySelected = true;
                    }
                }
            }

            // Wypożyczenie z tableBooksRezerwiren
            for (WypozyczNOW ksiazka : tableBooksRezerwiren.getItems()) {
                if (ksiazka.isSelected()) {
                    boolean success = wypozyczPojedynczaKsiazke(
                            ksiazka.getTytul(),
                            zaznaczonyCzytelnik.getId(),
                            getIdStmt,
                            insertWypozyczenieStmt,
                            getIloscStmt,
                            getWypozyczoneKsiazkiStmt,
                            insertWypozyczoneKsiazkiStmt,
                            updateWypozyczoneKsiazkiStmt,
                            false
                    );
                    if (success) {
                        selectedBooks.append(ksiazka.getTytul()).append(", ");
                        anySelected = true;

                        // Zmiana statusu rezerwacji na 2
                        getIdStmt.setString(1, ksiazka.getTytul());
                        try (ResultSet rs = getIdStmt.executeQuery()) {
                            if (rs.next()) {
                                int idKsiazki = rs.getInt("id_ksiazki");
                                updateStatusRezerwacjiStmt.setInt(1, idKsiazki);
                                updateStatusRezerwacjiStmt.setInt(2, zaznaczonyCzytelnik.getId());
                                updateStatusRezerwacjiStmt.executeUpdate();
                            } else {
                                System.out.println("Nie znaleziono ID książki do zmiany statusu: " + ksiazka.getTytul());
                            }
                        }
                    }
                }
            }

            if (anySelected) {
                System.out.println(selectedBooks.substring(0, selectedBooks.length() - 2));
            } else {
                System.out.println("Brak zaznaczonych książek do wypożyczenia.");
            }

            odswiezListeKsiazek(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Nowa metoda wypożyczenia pojedynczej książki
    private boolean wypozyczPojedynczaKsiazke(
            String tytul,
            int idCzytelnika,
            PreparedStatement getIdStmt,
            PreparedStatement insertWypozyczenieStmt,
            PreparedStatement getIloscStmt,
            PreparedStatement getWypozyczoneKsiazkiStmt,
            PreparedStatement insertWypozyczoneKsiazkiStmt,
            PreparedStatement updateWypozyczoneKsiazkiStmt,
            boolean zmniejszIloscWypozyczonych // czy aktualizować ilość wypożyczonych egzemplarzy
    ) throws SQLException {
        getIdStmt.setString(1, tytul);
        try (ResultSet rs = getIdStmt.executeQuery()) {
            if (!rs.next()) {
                System.err.println("Nie znaleziono ID dla książki: " + tytul);
                return false;
            }
            int idKsiazki = rs.getInt("id_ksiazki");

            if (zmniejszIloscWypozyczonych) {
                // Pobierz ilość z tabeli ksiazki
                getIloscStmt.setString(1, tytul);
                try (ResultSet rsIlosc = getIloscStmt.executeQuery()) {
                    int iloscCalkowita = rsIlosc.next() ? rsIlosc.getInt("ilosc") : 0;

                    // Sprawdź czy książka już wypożyczona
                    getWypozyczoneKsiazkiStmt.setInt(1, idKsiazki);
                    try (ResultSet rsWyp = getWypozyczoneKsiazkiStmt.executeQuery()) {
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
                            return false;
                        }

                        // Wstaw wypożyczenie
                        insertWypozyczenieStmt.setInt(1, idKsiazki);
                        insertWypozyczenieStmt.setInt(2, idCzytelnika);
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
                    }
                }
            } else {
                // Tylko wypożyczenie bez zmiany ilości wypożyczonych
                insertWypozyczenieStmt.setInt(1, idKsiazki);
                insertWypozyczenieStmt.setInt(2, idCzytelnika);
                insertWypozyczenieStmt.executeUpdate();
            }
            return true;
        }
    }

    private void odswiezListeKsiazek(Connection conn) {
        ObservableList<WypozyczNOW> noweDane = getKsiazkiZBazy(conn);
        tableBooks.setItems(noweDane);
        czytelnikBox.getSelectionModel().clearSelection();
        tableBooksRezerwiren.setItems(FXCollections.observableArrayList());

    }

    private void otworzZwroty(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bibliotekarz-zwroty-view.fxml"));
            if (fxmlLoader.getLocation() == null) {
                System.out.println("Błąd: Nie znaleziono pliku bibliotekarz-zwroty-view.fxml");
                return;
            }
            BorderPane root = fxmlLoader.load();
            LibrarianZwrotyController controller = fxmlLoader.getController();
            controller.setAktualnyPracownik(aktualnyPracownik);
            Stage stage = (Stage) zwrotyButton.getScene().getWindow();
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
