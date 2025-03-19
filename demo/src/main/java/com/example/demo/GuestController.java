package com.example.demo;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.ScrollPane;

import java.util.HashMap;
import java.util.Map;

public class GuestController {

    @FXML
    private VBox alphabetBox;  // VBox w FXML, w którym będą wyświetlane litery i autorzy

    // Mapa liter alfabetu i przypisanych autorów
    private Map<String, String[]> authorsMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Wstępnie zdefiniowane przykłady autorów
        authorsMap.put("A", new String[]{"Albert Einstein", "Ambroży"});
        authorsMap.put("B", new String[]{"Bolek", "Bartosz"});
        authorsMap.put("C", new String[]{"Czesław Niemen", "Czesław Kiszczak"});
        // Dodaj więcej liter i autorów, jak chcesz...

        // Generowanie alfabetu A-Z
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            createLetterSection(String.valueOf(letter));
        }
    }


    private void createLetterSection(String letter) {
        // Tworzymy VBox dla każdej litery
        VBox letterSection = new VBox(5);

        // Tworzymy etykietę dla litery (np. "A")
        Label letterLabel = new Label(letter);
        letterLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        letterLabel.setTextFill(Color.WHITE);
        letterSection.setPadding(new javafx.geometry.Insets(0, 0, 0, 20));
        letterLabel.setEffect(new DropShadow(5.0, 2.0, 2.0, Color.BLACK));

        // Tworzymy VBox dla autorów tej litery (na początku ukryty)
        VBox authorsBox = new VBox();
        authorsBox.setVisible(false);  // Początkowo autorzy są ukryci
        authorsBox.setMaxHeight(0);  // Zapewniamy, że nie zajmuje miejsca, gdy jest zwinięty

        // Tworzymy etykiety dla autorów przypisanych do litery
        String[] authors = authorsMap.get(letter);
        if (authors != null) {
            for (String author : authors) {
                Label authorLabel = new Label(author);
                authorLabel.setStyle("-fx-font-size: 18;");
                authorLabel.setTextFill(Color.WHITE);
                authorLabel.setPadding(new javafx.geometry.Insets(0, 0, 0, 50));
                authorLabel.setEffect(new DropShadow(5.0, 2.0, 2.0, Color.BLACK));
                authorsBox.getChildren().add(authorLabel);
            }
        }

        // Po kliknięciu na literę, rozwija/zwija autorów
        letterLabel.setOnMouseClicked(event -> {
            boolean isVisible = authorsBox.isVisible();
            if (isVisible) {
                // Jeśli jest widoczny, zwijamy i ustawiamy maxHeight na 0
                authorsBox.setMaxHeight(0);
                letterSection.setMaxHeight(Double.MAX_VALUE);  // Umożliwiamy innym literom przesunięcie się
            } else {
                // Jeśli jest niewidoczny, rozwijamy
                authorsBox.setMaxHeight(Double.MAX_VALUE);  // Przywracamy normalną wysokość
                letterSection.setMaxHeight(Double.MAX_VALUE);  // Pozwalamy na rozwój tej sekcji
            }
            authorsBox.setVisible(!isVisible);

            // Teraz, po zwinięciu/rozwinięciu, inne litery "przesuwają" się do góry
        });

        // Dodajemy etykietę dla litery do sekcji
        letterSection.getChildren().add(letterLabel);

        // Dodajemy VBox z autorami do sekcji
        letterSection.getChildren().add(authorsBox);

        // Dodajemy sekcję litery do głównego VBox
        alphabetBox.getChildren().add(letterSection);
    }

}



