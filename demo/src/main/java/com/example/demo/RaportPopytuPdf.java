package com.example.demo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

public class RaportPopytuPdf {
    public void createPopytPdf(Connection conn) {
        List<Popyt> popyt = pobierzWypozyczeniaZBazy(conn);
        String outputPath = "popytNa.pdf";

        try {
            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // === Nagłówek z logo i datą ===
            float[] headerColumnWidths = {1, 4};
            Table headerTable = new Table(headerColumnWidths);
            headerTable.setWidth(UnitValue.createPercentValue(100));

            // Załaduj i dodaj logo
            String imagePath = "C:/Users/lukos/Desktop/INF_NIEBIESCY-main/2/INF_NIEBIESCY/demo/src/main/resources/com/example/demo/foto/logo.jpg";
            Image logoImage = new Image(ImageDataFactory.create(imagePath));
            logoImage.scaleToFit(80, 80);
            Cell imageCell = new Cell().add(logoImage).setBorder(Border.NO_BORDER);
            headerTable.addCell(imageCell);

            // Dodaj datę
            String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            Paragraph dateParagraph = new Paragraph("Data: " + currentDate)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(10);

            Cell dateCell = new Cell().add(dateParagraph)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER);
            headerTable.addCell(dateCell);

            // Dodanie nagłówka
            document.add(headerTable);
            document.add(new Paragraph("Raport wypożyczen").setBold().setFontSize(16).setMarginTop(10).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n")); // odstęp

            // === Tabela z danymi ===
            float[] columnWidths = {100f, 80f, 80f, 40f, 60f, 60f};
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));

            // Nagłówki tabeli
            table.addHeaderCell(new Cell().add(new Paragraph("Tytul").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Gatunek").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Autor").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Ilosc").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("W obiegu").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Wypozyczenia").setBold()));

            // Wiersze z danymi
            for (Popyt pop : popyt) {
                table.addCell(new Cell().add(new Paragraph(getSafeText(pop.getTytul()))));
                table.addCell(new Cell().add(new Paragraph(getSafeText(pop.getGatunek()))));
                table.addCell(new Cell().add(new Paragraph(getSafeText(pop.getAutor()))));
                table.addCell(new Cell().add(new Paragraph(getSafeText(pop.getIlosc()))));
                table.addCell(new Cell().add(new Paragraph(getSafeText(pop.getIlosc_wypozyczonych()))));
                table.addCell(new Cell().add(new Paragraph(getSafeText(pop.getIlosc_wypozyczen()))));
            }

            document.add(table);
            document.close();
            System.out.println("Plik PDF wygenerowany: " + outputPath);

        } catch (Exception e) {
            System.err.println("Błąd podczas generowania PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Pomocnicza metoda do obsługi nulli
    private String getSafeText(String text) {
        return (text == null) ? "" : text;
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

                if (ilosc==null) ilosc="0";
                if (iloscWypozyczonych==null) iloscWypozyczonych="0";
                if (iloscWypozyczen==null) iloscWypozyczen="0";

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
