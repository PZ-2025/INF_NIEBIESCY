package com.example.demo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Zwroty {
    private final StringProperty id_ksiazki;
    private final StringProperty autor;
    private final StringProperty data_wypozyczenia;
    private final StringProperty ISBN;
    private final StringProperty tytul;
    private final BooleanProperty selected;

    public Zwroty(String id_ksiazki, String autor, String data_wypozyczenia, String isbn, String tytul, boolean selected) {
        this.id_ksiazki = new SimpleStringProperty(id_ksiazki);
        this.autor = new SimpleStringProperty(autor);
        this.data_wypozyczenia = new SimpleStringProperty(data_wypozyczenia);
        this.ISBN = new SimpleStringProperty(isbn);
        this.tytul = new SimpleStringProperty(tytul);
        this.selected = new SimpleBooleanProperty(selected);
    }


    public String getId_ksiazki() {
        return id_ksiazki.get();
    }

    public StringProperty id_ksiazkiProperty() {
        return id_ksiazki;
    }


    public String getData_wypozyczenia() {
        return data_wypozyczenia.get();
    }

    public StringProperty data_wypozyczeniaProperty() {
        return data_wypozyczenia;
    }

    public String getISBN() {
        return ISBN.get();
    }

    public StringProperty ISBNProperty() {
        return ISBN;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public String getTytul() {
        return tytul.get();
    }

    public StringProperty tytulProperty() {
        return tytul;
    }

    public String getAutor() {
        return autor.get();
    }

    public StringProperty autorProperty() {
        return autor;
    }
}
