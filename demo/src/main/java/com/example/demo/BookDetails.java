package com.example.demo;

public class BookDetails {
    private String idKsiazki;
    private String autor;
    private String gatunek;
    private String tytul;
    private String wydawnictwo;
    private String dataDodania;
    private String isbn;
    private String ilosc;

    public BookDetails(String idKsiazki, String autor, String gatunek, String tytul, String wydawnictwo, String dataDodania, String isbn, String ilosc) {
        this.idKsiazki = idKsiazki;
        this.autor = autor;
        this.gatunek = gatunek;
        this.tytul = tytul;
        this.wydawnictwo = wydawnictwo;
        this.dataDodania = dataDodania;
        this.isbn = isbn;
        this.ilosc = ilosc;
    }

    public String getIdKsiazki() { return idKsiazki; }
    public String getAutor() { return autor; }
    public String getGatunek() {return gatunek; }
    public String getTytul() { return tytul; }
    public String getWydawnictwo() { return wydawnictwo; }
    public String getDataDodania() { return dataDodania; }
    public String getIsbn() { return isbn; }
    public String getIlosc() { return ilosc; }

    public void setIdKsiazki(String idKsiazki) { this.idKsiazki = idKsiazki; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setGatunek() {this.gatunek = gatunek; }
    public void setTytul(String tytul) { this.tytul = tytul; }
    public void setWydawnictwo(String wydawnictwo) { this.wydawnictwo = wydawnictwo; }
    public void setDataDodania(String dataDodania) { this.dataDodania = dataDodania; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setIlosc(String ilosc) { this.ilosc = ilosc; }
}

