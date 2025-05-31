package com.example.demo;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.FileNotFoundException;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import javafx.collections.ObservableList;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfTest {

    public void createBookListPdf(String dest, ObservableList<Book> books) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Dodanie zdjęcia w lewym górnym rogu
        String imagePath = "C:/Users/lukos/Desktop/Nowy folder/INF_NIEBIESCY/demo/src/main/resources/com/example/demo/foto/logo.jpg";

        // Konwersja ścieżki do pliku na URL
        URI imageUri = Paths.get(imagePath).toUri();
        URL imageUrl = null;
        try {
            imageUrl = imageUri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Tworzenie obrazu z URL
        Image logoImage = new Image(ImageDataFactory.create(imageUrl));
        logoImage.scaleToFit(140, 140);
        // Ustawienie pozycji obrazu na stronie
        logoImage.setFixedPosition(30,700);  // Możesz dostosować te wartości

        document.add(logoImage);


        // Dodanie daty w prawym górnym rogu
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Paragraph dateParagraph = new Paragraph("Data: " + currentDate)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(10);
        document.add(dateParagraph);

        // Nagłówek dokumentu
        Paragraph header = new Paragraph("Lista ksiazek")
                .setFontSize(22)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(50);
        document.add(header);

        // Tabela z 4 kolumnami
        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 3, 3, 1}))
                .useAllAvailableWidth();

        // Nagłówki kolumn
        table.addHeaderCell(new Cell().add(new Paragraph("Tytuł").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Autor").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Wydawnictwo").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Ilosc").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Wiersze z danymi
        for (Book book : books) {
            table.addCell(new Paragraph(book.getTytul()).setFontSize(9));
            table.addCell(new Paragraph(book.getAutor()).setFontSize(9));
            table.addCell(new Paragraph(book.getWydawnictwo()).setFontSize(9));
            table.addCell(new Paragraph(book.getIlosc()).setFontSize(9));
        }

        document.add(table);
        document.close();

        System.out.println("PDF z listą książek wygenerowany: " + dest);
    }

    public static void main(String[] args) {
        // Połączenie z bazą danych
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getConnection();

        // Tworzymy obiekt Book, aby załadować książki
        BookDAO book = new BookDAO(conn);

        ObservableList<Book> books = book.loadBooksFromDatabase(conn); // Pobieranie książek z bazy danych

        // Generowanie PDF
        PdfTest generator = new PdfTest();
        try {
            generator.createBookListPdf("lista_ksiazek.pdf", books);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
