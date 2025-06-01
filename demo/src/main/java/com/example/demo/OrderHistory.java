package com.example.demo;

import javafx.beans.property.SimpleStringProperty;

public class OrderHistory {
    private final SimpleStringProperty id;
    private final SimpleStringProperty tytul;
    private final SimpleStringProperty autor;
    private final SimpleStringProperty rokWydania;
    private final SimpleStringProperty wydawnictwo;
    private final SimpleStringProperty isbn;
    private final SimpleStringProperty ilosc;
    private final SimpleStringProperty dostawca;
    private final SimpleStringProperty adres;

    public OrderHistory(String id, String tytul, String autor, String rokWydania, String wydawnictwo, String isbn, String ilosc, String dostawca, String adres) {
        this.id = new SimpleStringProperty(id);
        this.tytul = new SimpleStringProperty(tytul);
        this.autor = new SimpleStringProperty(autor);
        this.rokWydania = new SimpleStringProperty(rokWydania);
        this.wydawnictwo = new SimpleStringProperty(wydawnictwo);
        this.isbn = new SimpleStringProperty(isbn);
        this.ilosc = new SimpleStringProperty(ilosc);
        this.dostawca = new SimpleStringProperty(dostawca);
        this.adres = new SimpleStringProperty(adres);
    }

    public String getId() {
        return id.get();
    }


    public String getTytul() {
        return tytul.get();
    }


    public String getAutor() {
        return autor.get();
    }


    public String getRokWydania() {
        return rokWydania.get();
    }


    public String getWydawnictwo() {
        return wydawnictwo.get();
    }


    public String getIsbn() {
        return isbn.get();
    }


    public String getIlosc() {
        return ilosc.get();
    }


    public String getDostawca() {
        return dostawca.get();
    }


    public String getAdres() {
        return adres.get();
    }

}
