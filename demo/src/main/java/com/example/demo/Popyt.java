package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Popyt {
    private final StringProperty tytul;
    private final StringProperty gatunek;
    private final StringProperty autor;
    private final StringProperty ilosc;
    private final StringProperty ilosc_wypozyczonych;
    private final StringProperty ilosc_wypozyczen;


    public Popyt(String tytul, String gatunek, String autor, String ilosc, String ilosc_wypozyczonych, String ilosc_wypozyczen) {
        this.tytul = new SimpleStringProperty(tytul);
        this.gatunek = new SimpleStringProperty(gatunek);
        this.autor = new SimpleStringProperty(autor);
        this.ilosc = new SimpleStringProperty(ilosc);
        this.ilosc_wypozyczonych = new SimpleStringProperty(ilosc_wypozyczonych);
        this.ilosc_wypozyczen = new SimpleStringProperty(ilosc_wypozyczen);
    }

    public String getIlosc() {
        return ilosc.get();
    }

    public StringProperty iloscProperty() {
        return ilosc;
    }

    public String getIlosc_wypozyczonych() {
        return ilosc_wypozyczonych.get();
    }

    public StringProperty ilosc_wypozyczonychProperty() {
        return ilosc_wypozyczonych;
    }

    public String getIlosc_wypozyczen() {
        return ilosc_wypozyczen.get();
    }

    public StringProperty ilosc_wypozyczenProperty() {
        return ilosc_wypozyczen;
    }

    public String getTytul() {
        return tytul.get();
    }

    public StringProperty tytulProperty() {
        return tytul;
    }

    public String getGatunek() {
        return gatunek.get();
    }

    public StringProperty gatunekProperty() {
        return gatunek;
    }

    public String getAutor() {
        return autor.get();
    }

    public StringProperty autorProperty() {
        return autor;
    }
}
