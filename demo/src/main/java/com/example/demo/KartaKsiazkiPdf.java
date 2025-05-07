package com.example.demo;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class KartaKsiazkiPdf {

    public void createCardBookPdf(String dest, String idKsiazki, String autor, String tytul, String wydawnictwo, String dataWlaczenia, String isbn) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Data
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Paragraph dateParagraph = new Paragraph("Data: " + currentDate)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(10);
        document.add(dateParagraph);

        float[] colWidths = {200f, 300f};
        Table tableInfo = new Table(colWidths);
        tableInfo.setWidth(500);

        tableInfo.addCell(new Cell().add(new Paragraph("Id ksiazki")));
        tableInfo.addCell(new Cell().add(new Paragraph(idKsiazki)));

        tableInfo.addCell(new Cell().add(new Paragraph("Autor")));
        tableInfo.addCell(new Cell().add(new Paragraph(autor)));

        tableInfo.addCell(new Cell().add(new Paragraph("Tytuł")));
        tableInfo.addCell(new Cell().add(new Paragraph(tytul)));

        tableInfo.addCell(new Cell().add(new Paragraph("Wydawnictwo")));
        tableInfo.addCell(new Cell().add(new Paragraph(wydawnictwo)));

        tableInfo.addCell(new Cell().add(new Paragraph("Data włączenia do biblioteki")));
        tableInfo.addCell(new Cell().add(new Paragraph(dataWlaczenia)));

        document.add(tableInfo);


        document.add(new Paragraph("Uwagi:").setMarginTop(20).setHeight(100));

        // Tabela wypożyczeń z nagłówkami
        float[] tableCols = {50f, 100f, 100f, 30f, 50f, 100f, 100f};
        Table tableEntries = new Table(tableCols);
        tableEntries.setWidth(100);

        tableEntries.addCell(new Cell().add(new Paragraph("Id czytelnika")).setMinWidth(60f));
        tableEntries.addCell(new Cell().add(new Paragraph("Data wypoz.")).setMinWidth(80f));
        tableEntries.addCell(new Cell().add(new Paragraph("Data zwrotu")).setMinWidth(80f));

        // Pusta komórka jako przerwa
        tableEntries.addCell(new Cell().add(new Paragraph("")).setMinWidth(50f).setBorder(Border.NO_BORDER));

        tableEntries.addCell(new Cell().add(new Paragraph("Id czytelnika")).setMinWidth(60f));
        tableEntries.addCell(new Cell().add(new Paragraph("Data wypoz.")).setMinWidth(80f));
        tableEntries.addCell(new Cell().add(new Paragraph("Data zwrotu")).setMinWidth(80f));

        // 10 przykładowych wierszy
        for (int i = 0; i < 5; i++) {
            tableEntries.addCell(new Cell().add(new Paragraph(""+(1 + i))));
            tableEntries.addCell(new Cell().add(new Paragraph("02.04.2024")));
            tableEntries.addCell(new Cell().add(new Paragraph("12.04.2024")));

            tableEntries.addCell(new Cell().add(new Paragraph("")).setMinWidth(30f).setBorder(Border.NO_BORDER)); // przerwa

            tableEntries.addCell(new Cell().add(new Paragraph("" + (2 + i))));
            tableEntries.addCell(new Cell().add(new Paragraph("05.04.2024")));
            tableEntries.addCell(new Cell().add(new Paragraph("15.04.2024")));
        }

//        // 5 pustych wierszy
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 6; j++) {
//                tableEntries.addCell(new Cell().add(new Paragraph(" ")));
//            }
//        }

        document.add(tableEntries);

        document.add(new Paragraph("ISBN: "+ isbn)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginTop(20));

        document.close();
    }

    public static void main(String[] args) {
        // ID książki, którą chcesz wygenerować do PDF
        String bookId = "2";  // <- Możesz to pobierać dynamicznie

        // Połączenie z bazą danych
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();

        if (conn == null) {
            System.out.println("Błąd połączenia z bazą danych.");
            return;
        }

        // Pobierz dane książki
        BookDAO dao = new BookDAO();
        ObservableList<BookDetails> bookList = dao.loadBookDetails(conn, bookId);

        if (bookList.isEmpty()) {
            System.out.println("Brak danych książki o ID: " + bookId);
            return;
        }

        BookDetails book = bookList.get(0); // zakładamy, że ID jest unikalne

        // Generowanie PDF
        KartaKsiazkiPdf generator = new KartaKsiazkiPdf();
        String outputFileName = "karta_ksiazki_" + book.getIdKsiazki() + ".pdf";

        try {
            generator.createCardBookPdf(
                    outputFileName,
                    book.getIdKsiazki(),
                    book.getAutor(),
                    book.getTytul(),
                    book.getWydawnictwo(),
                    book.getDataDodania(),
                    book.getIsbn()
            );
            System.out.println("PDF wygenerowany: " + outputFileName);
        } catch (FileNotFoundException e) {
            System.out.println("Błąd: Nie można zapisać pliku PDF.");
            e.printStackTrace();
        }
    }

}
