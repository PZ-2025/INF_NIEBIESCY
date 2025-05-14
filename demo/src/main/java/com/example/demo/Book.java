package com.example.demo;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Book {
    private final SimpleStringProperty autor;
    private final SimpleStringProperty tytul;
    private final SimpleStringProperty gatunek;
    private final SimpleStringProperty wydawnictwo;
    private final SimpleStringProperty ilosc;

    public Book(String autor, String tytul,String gatunek , String wydawnictwo, String ilosc) {
        this.autor = new SimpleStringProperty(autor);
        this.tytul = new SimpleStringProperty(tytul);
        this.gatunek = new SimpleStringProperty(gatunek);
        this.wydawnictwo = new SimpleStringProperty(wydawnictwo);
        this.ilosc = new SimpleStringProperty(ilosc);
    }

    public String getAutor() { return autor.get(); }
    public String getTytul() { return tytul.get(); }
    public String getGatunek() { return gatunek.get(); }
    public String getWydawnictwo() { return wydawnictwo.get(); }
    public String getIlosc() { return ilosc.get(); }

    public void setAutor(String value) { autor.set(value); }
    public void setTytul(String value) { tytul.set(value); }
    public void setGatunek(String value) { gatunek.set(value); }
    public void setWydawnictwo(String value) { wydawnictwo.set(value); }
    public void setIlosc(String value) { ilosc.set(value); }
    
}

