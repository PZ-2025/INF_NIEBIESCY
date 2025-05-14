package com.example.demo;

import javafx.beans.property.SimpleStringProperty;

public class Wypozyczenia {
    private final SimpleStringProperty id_wypozyczenia;
    private final SimpleStringProperty id_ksiazki;
    private final SimpleStringProperty id_czytelnika;
    private final SimpleStringProperty data_wypozyczenia;
    private final SimpleStringProperty data_oddania;

    public Wypozyczenia(String id_wypozyczenia, String id_ksiazki, String id_czytelnika, String data_wypozyczenia, String data_oddania) {
        this.id_wypozyczenia = new SimpleStringProperty(id_wypozyczenia);
        this.id_ksiazki = new SimpleStringProperty(id_ksiazki);
        this.id_czytelnika = new SimpleStringProperty(id_czytelnika);
        this.data_wypozyczenia = new SimpleStringProperty(data_wypozyczenia);
        this.data_oddania = new SimpleStringProperty(data_oddania);
    }

    public String getId_wypozyczenia() {return id_wypozyczenia.get();}
    public String getId_ksiazki() {return id_ksiazki.get();}
    public String getId_czytelnika() {return id_czytelnika.get();}
    public String getData_wypozyczenia() {return data_wypozyczenia.get();}
    public String getData_oddania() {return data_oddania.get();}
}
