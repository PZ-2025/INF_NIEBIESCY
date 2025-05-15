package com.example.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;

public class RaportPopytuPdf {
    public void createPopytPdf(Connection conn) {
        List<Wypozyczenia> wypozyczenia = pobierzWypozyczeniaZBazy(conn);

        String outputPath = "popytNa.pdf";

        try {
            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Tytuł
            document.add(new Paragraph("Raport wypożyczeń").setBold().setFontSize(16));

            // Tworzenie tabeli z 5 kolumnami
            float[] columnWidths = {100f, 100f, 100f, 100f, 100f};
            Table table = new Table(columnWidths);

            // Nagłówki
            table.addCell(new Cell().add(new Paragraph("ID Wypożyczenia")).setMinWidth(60f));
            table.addCell(new Cell().add(new Paragraph("ID Książki")).setMinWidth(60f));
            table.addCell(new Cell().add(new Paragraph("ID Czytelnika")).setMinWidth(60f));
            table.addCell(new Cell().add(new Paragraph("Data wypożyczenia")).setMinWidth(60f));
            table.addCell(new Cell().add(new Paragraph("Data oddania")).setMinWidth(60f));

            // Dane
//            for (Wypozyczenia w : wypozyczenia) {
//                table.addCell(new Cell().add(new Paragraph(w.getId_wypozyczenia())).setMinWidth(60f));
//                table.addCell(new Cell().add(new Paragraph(w.getId_ksiazki())).setMinWidth(60f));
//                table.addCell(new Cell().add(new Paragraph(w.getId_czytelnika())).setMinWidth(60f));
//                table.addCell(new Cell().add(new Paragraph(w.getData_wypozyczenia())).setMinWidth(60f));
//                table.addCell(new Cell().add(new Paragraph(w.getData_oddania())).setMinWidth(60f));
//            }

            document.add(table);
            document.close();

            System.out.println("Plik PDF wygenerowany: " + outputPath);
        } catch (Exception e) {
            System.err.println("Błąd podczas generowania PDF: " + e.getMessage());
        }
    }

    public List<Wypozyczenia> pobierzWypozyczeniaZBazy(Connection conn) {
        List<Wypozyczenia> wypozyczeniaList = new ArrayList<>();
        String sql = "SELECT id_wypozyczenia, id_ksiazki, id_czytelnika, data_wypozyczenia, data_oddania FROM wypozyczenia";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String idWypozyczenia = rs.getString("id_wypozyczenia");
                String idKsiazki = rs.getString("id_ksiazki");
                String idCzytelnika = rs.getString("id_czytelnika");
                String dataWypozyczenia = rs.getString("data_wypozyczenia");
                String dataOddania = rs.getString("data_oddania");

                Wypozyczenia wyp = new Wypozyczenia(idWypozyczenia, idKsiazki, idCzytelnika, dataWypozyczenia, dataOddania);
                wypozyczeniaList.add(wyp);
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania danych z bazy: " + e.getMessage());
        }

        return wypozyczeniaList;
    }

    public static void main(String[] args) {
        // Połączenie z bazą danych
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();

        if (conn == null) {
            System.out.println("Błąd połączenia z bazą danych.");
            return;
        }

        // Generowanie PDF
        RaportPopytuPdf raport = new RaportPopytuPdf();
        raport.createPopytPdf(conn);
    }


}
