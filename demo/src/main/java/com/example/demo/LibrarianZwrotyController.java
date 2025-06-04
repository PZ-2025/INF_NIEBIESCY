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

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.*;

public class LibrarianZwrotyController {
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

    @FXML private Button zwrotButton;

    @FXML private TableView<Zwroty> tableBooks;
    @FXML private TableColumn<Zwroty, String> tytul;
    @FXML private TableColumn<Zwroty, String> autor;
    @FXML private TableColumn<Zwroty, String> data_wyp;
    @FXML private TableColumn<Zwroty, String> ISBN;
    @FXML private TableColumn<Zwroty, Boolean> checkbox;

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize() {
        reservationsButton.setOnMouseClicked(this::otworzRezerwacje);
        zwrotyButton.setOnMouseClicked(this::otworzZwroty);
        loansButton.setOnMouseClicked(this::otworzWypozyczenia);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        logoutButton.setOnMouseClicked(this::wyloguj);
        // Kolumny tekstowe
        tytul.setCellValueFactory(cellData -> cellData.getValue().tytulProperty());
        autor.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        data_wyp.setCellValueFactory(cellData -> cellData.getValue().data_wypozyczeniaProperty());
        ISBN.setCellValueFactory(cellData -> cellData.getValue().ISBNProperty());

        // Kolumna checkbox
        checkbox.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        checkbox.setCellFactory(tc -> new CheckBoxTableCell<>(index ->
                tableBooks.getItems().get(index).selectedProperty()
        ));

        // Połączenie z bazą
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getConnection();

        zwrotButton.setOnAction(event -> zwroc(event, conn));

        zaladujCzytelnikowDoComboBox();
        czytelnikBox.setOnAction(event -> {
            Czytelnik wybrany = (Czytelnik) czytelnikBox.getSelectionModel().getSelectedItem();
            if (wybrany != null) {
                // Odśwież listę książek dla wybranego czytelnika
                ObservableList<Zwroty> dane = getKsiazkiZBazyDlaCzytelnika(wybrany.getId());
                tableBooks.setItems(dane);
            }
        });

        tableBooks.setEditable(true);
        checkbox.setEditable(true);
    }

    private ObservableList<Zwroty> getKsiazkiZBazyDlaCzytelnika(int idCzytelnika) {
        ObservableList<Zwroty> lista = FXCollections.observableArrayList();

        String sql = """
        SELECT w.id_ksiazki, k.tytul, a.nazwa AS autor_nazwa,
               w.data_wypozyczenia, k.ISBN
        FROM wypozyczenia w
        JOIN ksiazki k ON w.id_ksiazki = k.id_ksiazki
        JOIN autorzy a ON k.id_autora = a.id_autora
        WHERE w.data_oddania IS NULL AND w.id_czytelnika = ?
    """;

        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCzytelnika);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String idKsiazki = rs.getString("id_ksiazki");
                String tytul = rs.getString("tytul");
                String autor = rs.getString("autor_nazwa");
                String dataWyp = rs.getString("data_wypozyczenia");
                String isbn = rs.getString("ISBN");

                Zwroty z = new Zwroty(idKsiazki, autor, dataWyp, isbn, tytul, false);
                lista.add(z);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
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

            czytelnikBox.setItems(listaCzytelnikow);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void zwroc(ActionEvent actionEvent, Connection conn) {
        Czytelnik zaznaczonyCzytelnik = (Czytelnik) czytelnikBox.getSelectionModel().getSelectedItem();
        if (zaznaczonyCzytelnik == null) {
            System.out.println("Nie wybrano czytelnika!");
            return;
        }

        StringBuilder zwroconeKsiazki = new StringBuilder("Zwrócono: ");
        boolean anySelected = false;

        String znajdzIdKsiazkiSql = "SELECT id_ksiazki FROM ksiazki WHERE tytul = ?";
        String znajdzWypozyczenieSql = """
        SELECT id_wypozyczenia FROM wypozyczenia 
        WHERE id_ksiazki = ? AND id_czytelnika = ? AND data_oddania IS NULL
    """;
        String aktualizujZwrotSql = """
        UPDATE wypozyczenia 
        SET data_oddania = CURRENT_DATE() 
        WHERE id_wypozyczenia = ?
    """;
        String aktualizujWypozyczoneSql = """
        UPDATE ksiazki_wypozyczone 
        SET ilosc_wypozyczonych = ilosc_wypozyczonych - 1 
        WHERE id_ksiazki = ?
    """;

        try (
                PreparedStatement getIdStmt = conn.prepareStatement(znajdzIdKsiazkiSql);
                PreparedStatement findWypozyczenieStmt = conn.prepareStatement(znajdzWypozyczenieSql);
                PreparedStatement updateZwrotStmt = conn.prepareStatement(aktualizujZwrotSql);
                PreparedStatement updateWypozyczoneStmt = conn.prepareStatement(aktualizujWypozyczoneSql)
        ) {
            for (Zwroty ksiazka : tableBooks.getItems()) {
                if (ksiazka.isSelected()) {
                    String tytul = ksiazka.getTytul();

                    // 1. Pobierz ID książki
                    getIdStmt.setString(1, tytul);
                    ResultSet rsId = getIdStmt.executeQuery();

                    if (rsId.next()) {
                        int idKsiazki = rsId.getInt("id_ksiazki");

                        // 2. Znajdź aktywne wypożyczenie
                        findWypozyczenieStmt.setInt(1, idKsiazki);
                        findWypozyczenieStmt.setInt(2, zaznaczonyCzytelnik.getId());
                        ResultSet rsWyp = findWypozyczenieStmt.executeQuery();

                        if (rsWyp.next()) {
                            int idWypozyczenia = rsWyp.getInt("id_wypozyczenia");

                            // 3. Zaktualizuj rekord (dodaj datę oddania)
                            updateZwrotStmt.setInt(1, idWypozyczenia);
                            updateZwrotStmt.executeUpdate();

                            // 4. Zmniejsz ilość wypożyczonych egzemplarzy
                            updateWypozyczoneStmt.setInt(1, idKsiazki);
                            updateWypozyczoneStmt.executeUpdate();

                            zwroconeKsiazki.append(tytul).append(", ");
                            anySelected = true;
                        } else {
                            System.out.println("Nie znaleziono aktywnego wypożyczenia tej książki: " + tytul);
                        }
                    } else {
                        System.out.println("Nie znaleziono książki: " + tytul);
                    }
                }
            }

            if (anySelected) {
                System.out.println(zwroconeKsiazki.substring(0, zwroconeKsiazki.length() - 2));
            } else {
                System.out.println("Nie zaznaczono żadnych książek do zwrotu.");
            }

            odswiezListeKsiazek();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void odswiezListeKsiazek() {
        Czytelnik zaznaczonyCzytelnik = (Czytelnik) czytelnikBox.getSelectionModel().getSelectedItem();
        if (zaznaczonyCzytelnik != null) {
            ObservableList<Zwroty> noweDane = getKsiazkiZBazyDlaCzytelnika(zaznaczonyCzytelnik.getId());
            tableBooks.setItems(noweDane);
        }
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
