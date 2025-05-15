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
        List<Popyt> popyt = pobierzWypozyczeniaZBazy(conn);

        String outputPath = "popytNa.pdf";

        try {
            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Tytuł
            document.add(new Paragraph("Raport wypożyczeń").setBold().setFontSize(16));

            // Tworzenie tabeli z 5 kolumnami
            float[] columnWidths = {80f, 60f, 60f, 20f, 20f, 20f};
            Table table = new Table(columnWidths);

            // Nagłówki
            table.addCell(new Cell().add(new Paragraph("Tytul")).setMinWidth(80f));
            table.addCell(new Cell().add(new Paragraph("Gatunek")).setMinWidth(60f));
            table.addCell(new Cell().add(new Paragraph("Autor")).setMinWidth(60f));
            table.addCell(new Cell().add(new Paragraph("Ilosc")).setMinWidth(20f));
            table.addCell(new Cell().add(new Paragraph("Ilosc w obiegu")).setMinWidth(20f));
            table.addCell(new Cell().add(new Paragraph("Ilosc wypozyczen")).setMinWidth(20f));

            for (Popyt pop : popyt) {
                table.addCell(new Cell().add(new Paragraph(pop.getTytul() == null ? "" : pop.getTytul())));
                table.addCell(new Cell().add(new Paragraph(pop.getGatunek() == null ? "" : pop.getGatunek())));
                table.addCell(new Cell().add(new Paragraph(pop.getAutor() == null ? "" : pop.getAutor())));
                table.addCell(new Cell().add(new Paragraph(pop.getIlosc() == null ? "" : pop.getIlosc())));

                String iloscWypozyczonych = pop.getIlosc_wypozyczonych();
                if (iloscWypozyczonych == null) iloscWypozyczonych = "0";
                table.addCell(new Cell().add(new Paragraph(iloscWypozyczonych)));

                String iloscWypozyczen = pop.getIlosc_wypozyczen();
                if (iloscWypozyczen == null) iloscWypozyczen = "0";
                table.addCell(new Cell().add(new Paragraph(iloscWypozyczen)));
            }

            document.add(table);
            document.close();

            System.out.println("Plik PDF wygenerowany: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Popyt> pobierzWypozyczeniaZBazy(Connection conn) {
        List<Popyt> popytList = new ArrayList<>();
        String sql = "SELECT k.tytul, g.nazwa_gatunku, a.nazwa AS nazwa_autora, k.ilosc, kw.ilosc_wypozyczonych, " +
                "( SELECT COUNT(*) " +
                    "FROM wypozyczenia w2 " +
                    "WHERE w2.id_ksiazki = k.id_ksiazki " +
                    "AND w2.data_wypozyczenia BETWEEN '2025-05-09' AND '2025-05-16' ) " +
                "AS ilosc_wypozyczen_w_okresie " +
                "FROM ksiazki k " +
                "JOIN gatunek g ON k.id_gatunku = g.id_gatunku " +
                "JOIN autorzy a ON k.id_autora = a.id_autora " +
                "LEFT JOIN ksiazki_wypozyczone kw ON k.id_ksiazki = kw.id_ksiazki " +
                "WHERE (SELECT COUNT(*) FROM wypozyczenia w2" +
                     " WHERE w2.id_ksiazki = k.id_ksiazki " +
                     "AND w2.data_wypozyczenia BETWEEN '2025-05-09' AND '2025-05-16') " +
                "> 0;;";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tytul = rs.getString("tytul");
                String gatunek = rs.getString("nazwa_gatunku");
                String autor = rs.getString("nazwa_autora");
                String ilosc = rs.getString("ilosc");
                String iloscWypozyczonych = rs.getString("ilosc_wypozyczonych");
                String iloscWypozyczen = rs.getString("ilosc_wypozyczen_w_okresie");

                Popyt pop = new Popyt(tytul, gatunek, autor, ilosc, iloscWypozyczonych, iloscWypozyczen);
                popytList.add(pop);
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania danych z bazy: " + e.getMessage());
        }

        return popytList;
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
