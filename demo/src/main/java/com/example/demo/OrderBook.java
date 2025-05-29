package com.example.demo;

import javafx.beans.property.SimpleStringProperty;

public class OrderBook {
    private final SimpleStringProperty id;
    private final SimpleStringProperty tytul;
    private final SimpleStringProperty autor;
    private final SimpleStringProperty ilosc;

    public OrderBook(String id, String tytul, String autor, String ilosc) {
        this.id = new SimpleStringProperty(id);
        this.tytul = new SimpleStringProperty(tytul);
        this.autor = new SimpleStringProperty(autor);
        this.ilosc = new SimpleStringProperty(ilosc);
    }

    public String getId() { return id.get(); }
    public String getTytul() { return tytul.get(); }
    public String getAutor() { return autor.get(); }
    public String getIlosc() { return ilosc.get(); }
}
