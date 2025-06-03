package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibrarianReservationsController {
    @FXML private TableView<Rezerwacje> tableView;

    @FXML private TableColumn<Rezerwacje, String> Imie;
    @FXML private TableColumn<Rezerwacje, String> Nazwisko;
    @FXML private TableColumn<Rezerwacje, String> Email;
    @FXML private TableColumn<Rezerwacje, String> Tytul;
    @FXML private TableColumn<Rezerwacje, String> Ilosc;
    @FXML private TableColumn<Rezerwacje, String> DataRezerwacji;
    @FXML private TableColumn<Rezerwacje, String> PlanowanaData;
    @FXML private TableColumn<Rezerwacje, Integer> Status;
    @FXML
    private TableColumn<Rezerwacje, Boolean> Radio;


    @FXML
    private Label reservationsButton;
    @FXML
    private Label loansButton;
    @FXML
    private Label booksButton;
    @FXML
    private Label logoutButton;
    @FXML
    private Button acceptButton;
    @FXML
    private Button denyButton;

    private Pracownik aktualnyPracownik;

    public void setAktualnyPracownik(Pracownik pracownik) {
        this.aktualnyPracownik = pracownik;
    }

    public void initialize() {
        reservationsButton.setOnMouseClicked(this::otworzRezerwacje);
        loansButton.setOnMouseClicked(this::otworzWypozyczenia);
        booksButton.setOnMouseClicked(this::otworzKsiegozbior);
        logoutButton.setOnMouseClicked(this::wyloguj);

        Imie.setCellValueFactory(new PropertyValueFactory<>("imie"));
        Nazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        Tytul.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        Ilosc.setCellValueFactory(new PropertyValueFactory<>("wyp"));
        DataRezerwacji.setCellValueFactory(new PropertyValueFactory<>("data_zamowienia"));
        PlanowanaData.setCellValueFactory(new PropertyValueFactory<>("planowana_data"));
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));

        Radio.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        Radio.setCellFactory(tc -> new CheckBoxTableCell<>(index ->
                tableView.getItems().get(index).selectedProperty()
        ));

        Status.setCellFactory(column -> new TableCell<Rezerwacje, Integer>() {
            @Override
            protected void updateItem(Integer status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                } else {
                    switch (status) {
                        case 1 -> setText("Zaakceptowane");
                        case 0 -> setText("Oczekujące");
                        case -1 -> setText("Odrzucone");
                        default -> setText("Nieznany");
                    }
                }
            }
        });

        loadData();

        tableView.setEditable(true);
        Radio.setEditable(true);

        acceptButton.setOnAction(event -> acceptSelectedReservations());
        denyButton.setOnAction(event -> discrespectSelectedReservations());
    }

    private void loadData() {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getConnection();

        ObservableList<Rezerwacje> dane = getAllRezerwacje(conn);
        tableView.setItems(dane);
    }

    public ObservableList<Rezerwacje> getAllRezerwacje(Connection conn) {
        ObservableList<Rezerwacje> lista = FXCollections.observableArrayList();

        String sql = """
            SELECT r.id_rezerwacji ,c.imie, c.nazwisko, c.email, k.tytul, (k.ilosc - IFNULL(kw.ilosc_wypozyczonych, 0)) AS ilosc_dostepna, r.data_zamowienia, r.planowana_data, r.status
            FROM rezerwacje r
            LEFT JOIN czytelnicy c ON r.id_czytelnika = c.id_czytelnika
            LEFT JOIN ksiazki k ON r.id_ksiazki = k.id_ksiazki
            LEFT JOIN ksiazki_wypozyczone kw ON r.id_ksiazki = kw.id_ksiazki;
    
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_rezerwacji");
                String imie = rs.getString("imie");
                String nazwisko = rs.getString("nazwisko");
                String email = rs.getString("email");
                String tytul = rs.getString("tytul");
                String ilosc_dostepna = rs.getString("ilosc_dostepna");
                String data_zamowienia = rs.getString("data_zamowienia");
                String planowana_data = rs.getString("planowana_data");
                int status = rs.getInt("status");

                lista.add(new Rezerwacje(id,imie,nazwisko, email, tytul, ilosc_dostepna,data_zamowienia, planowana_data, status));
            }

        } catch (SQLException e) {
            System.err.println("Błąd SQL: " + e.getMessage());
        }

        return lista;
    }

    private void acceptSelectedReservations() {
        DatabaseConnection connection = new DatabaseConnection();
        try (Connection conn = connection.getConnection()) {
            conn.setAutoCommit(false);

            String updateSql = "UPDATE rezerwacje SET status = 1 WHERE id_rezerwacji = ?";

            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                for (Rezerwacje r : tableView.getItems()) {
                    if (r.isSelected() && r.getStatus() != 1) {
                        ps.setInt(1, r.getId());
                        ps.addBatch();
                        r.setStatus(1); // musisz mieć setter lub zaktualizować obiekt inaczej
                        r.setSelected(false);
                    }
                }

                ps.executeBatch();
            }

            conn.commit();

            tableView.refresh(); // wymuś odświeżenie widoku

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void discrespectSelectedReservations() {
        DatabaseConnection connection = new DatabaseConnection();
        try (Connection conn = connection.getConnection()) {
            conn.setAutoCommit(false);

            String updateSql = "UPDATE rezerwacje SET status = -1 WHERE id_rezerwacji = ?";

            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                for (Rezerwacje r : tableView.getItems()) {
                    if (r.isSelected() && r.getStatus() != 1) {
                        ps.setInt(1, r.getId());
                        ps.addBatch();
                        r.setStatus(-1);
                        r.setSelected(false);
                    }
                }

                ps.executeBatch();
            }

            conn.commit();

            tableView.refresh(); // wymuś odświeżenie widoku

        } catch (SQLException e) {
            e.printStackTrace();
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
