package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Wypozyczenia {
    private final StringProperty id_ksiazki;
    private final StringProperty id_gatunku;
    private final StringProperty id_autora;
    private final StringProperty data_wypozyczenia;
    private final StringProperty data_oddania;

    public Wypozyczenia(String id_ksiazki, String id_gatunku, String id_autora, String data_wypozyczenia, String data_oddania) {
        this.id_ksiazki = new SimpleStringProperty(id_ksiazki);
        this.id_gatunku = new SimpleStringProperty(id_gatunku);
        this.id_autora = new SimpleStringProperty(id_autora);
        this.data_wypozyczenia = new SimpleStringProperty(data_wypozyczenia);
        this.data_oddania = new SimpleStringProperty(data_oddania);
    }

    public String getTytul() { return id_ksiazki.get(); }
    public String getGatunek() { return id_gatunku.get(); }
    public String getAutor() { return id_autora.get(); }
    public String getData_wyp() { return data_wypozyczenia.get(); }
    public String getData_odda() { return data_oddania.get(); }

    public StringProperty tytulProperty() {
        return id_ksiazki;  // nazwa pola powinna odpowiadać temu, co trzymasz jako tytuł
    }

    public StringProperty gatunekProperty() {
        return id_gatunku;
    }

    public StringProperty autorProperty() {
        return id_autora;
    }

    public StringProperty data_wypProperty() {
        return data_wypozyczenia;
    }

    public StringProperty data_oddaProperty() {
        return data_oddania;
    }

}
