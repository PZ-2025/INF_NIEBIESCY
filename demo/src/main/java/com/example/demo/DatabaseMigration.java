package com.example.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class DatabaseMigration {
    private static final String URL = "jdbc:mysql://localhost:3306/"; // URL do MySQL
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Tworzenie bazy danych
    public static void createDatabase() {
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS bibliotekadb";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createDatabaseSQL); // Tworzenie bazy danych, jeśli nie istnieje
            System.out.println("✅ Baza danych 'bibliotekadb' została utworzona lub już istnieje.");

            // Po utworzeniu bazy, połącz się z nią
            String useDatabaseSQL = "USE bibliotekadb"; // Przełącz na bibliotekadb
            stmt.executeUpdate(useDatabaseSQL);

            // Teraz wykonaj migrację
            executeSQLFile("src/main/resources/com/example/demo/migration/bibliotekadb.sql");
        } catch (Exception e) {
            System.err.println("❌ Błąd podczas tworzenia bazy danych: " + e.getMessage());
        }
    }

    // Wykonywanie operacji na utworzonej bazie danych
    private static void executeSQLFile(String sqlFilePath) {
        try (Connection conn = DriverManager.getConnection(URL + "bibliotekadb", USER, PASSWORD);
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (!line.startsWith("--") && !line.isEmpty()) {
                    sb.append(line);

                    if (line.endsWith(";")) {
                        stmt.execute(sb.toString());
                        sb.setLength(0);
                    }
                }
            }

            System.out.println("✅ Struktura bazy danych utworzona");
        } catch (Exception e) {
            System.err.println("❌ Błąd podczas wykonywania migracji: " + e.getMessage());
        }
    }

    private static boolean isTableEmpty(Connection conn, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) == 0; // true jeśli pusta
            }
        }
        return false;
    }

    private static void importSQLIfEmpty(String sqlFilePath, String dataType) {
        try (Connection conn = DriverManager.getConnection(URL + "bibliotekadb", USER, PASSWORD)) {
            if (isTableEmpty(conn, dataType)) {
                try (Statement stmt = conn.createStatement();
                     BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();

                        if (!line.startsWith("--") && !line.isEmpty()) {
                            sb.append(line);

                            if (line.endsWith(";")) {
                                stmt.execute(sb.toString());
                                sb.setLength(0);
                            }
                        }
                    }
                    System.out.println("✅ Migracja " + dataType + " zakończona!");
                }
            } else {
                System.out.println("⚠️ Tabela " + dataType + " zawiera już dane — pomijam migrację.");
            }
        } catch (Exception e) {
            System.err.println("❌ Błąd podczas migracji " + dataType + ": " + e.getMessage());
        }
    }

    // Importowanie danych
    public static void importAutorzy() {
        String sqlFilePath = "src/main/resources/com/example/demo/migration/import_autorzy.sql";
        importSQLIfEmpty(sqlFilePath, "autorzy");

    }

    public static void importGatunek() {
        String sqlFilePath = "src/main/resources/com/example/demo/migration/import_gatunek.sql";
        importSQLIfEmpty(sqlFilePath, "gatunek");
    }

    public static void importKsiazki() {
        String sqlFilePath = "src/main/resources/com/example/demo/migration/import_ksiazki.sql";
        importSQLIfEmpty(sqlFilePath, "ksiazki");
    }

    public static void importDostawcy() {
        String sqlFilePath = "src/main/resources/com/example/demo/migration/import_dostawcy.sql";
        importSQLIfEmpty(sqlFilePath, "dostawcy");
    }

    public static void importCzytelnicy() {
        String sqlFilePath = "src/main/resources/com/example/demo/migration/import_czytelnicy.sql";
        importSQLIfEmpty(sqlFilePath, "czytelnicy");
    }

    public static void importWypozyczen() {
        String sqlFilePath = "src/main/resources/com/example/demo/migration/import_wypozyczenia.sql";
        importSQLIfEmpty(sqlFilePath, "wypozyczenia");
    }

    // Ogólna metoda do importu danych
    private static void importSQL(String sqlFilePath, String dataType) {
        try (Connection conn = DriverManager.getConnection(URL + "bibliotekadb", USER, PASSWORD);
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (!line.startsWith("--") && !line.isEmpty()) {
                    sb.append(line);

                    if (line.endsWith(";")) {
                        stmt.execute(sb.toString());
                        sb.setLength(0);
                    }
                }
            }

            System.out.println("✅ Migracja " + dataType + " zakończona!");
        } catch (Exception e) {
            System.err.println("❌ Błąd podczas migracji " + dataType + ": " + e.getMessage());
        }
    }

    // Przykładowa metoda główna do uruchomienia migracji
    public static void main(String[] args) {
        createDatabase();  // Tworzenie bazy danych
        importAutorzy();   // Importowanie autorów
        importGatunek();    // Importowanie gatunkow
        importKsiazki();   // Importowanie książek
        importDostawcy();  // Importowanie dostawców
        importCzytelnicy();  // Importowanie czytelnikow
        importWypozyczen();  // Importowanie wypozyczen
    }
}
