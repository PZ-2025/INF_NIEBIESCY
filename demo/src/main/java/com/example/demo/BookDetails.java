package com.example.demo;

public class BookDetails {
    private String idKsiazki;
    private String autor;
    private String tytul;
    private String wydawnictwo;
    private String dataDodania;
    private String isbn;

    public BookDetails(String idKsiazki, String autor, String tytul, String wydawnictwo, String dataDodania, String isbn) {
        this.idKsiazki = idKsiazki;
        this.autor = autor;
        this.tytul = tytul;
        this.wydawnictwo = wydawnictwo;
        this.dataDodania = dataDodania;
        this.isbn = isbn;
    }

    public String getIdKsiazki() { return idKsiazki; }
    public String getAutor() { return autor; }
    public String getTytul() { return tytul; }
    public String getWydawnictwo() { return wydawnictwo; }
    public String getDataDodania() { return dataDodania; }
    public String getIsbn() { return isbn; }

    public void setIdKsiazki(String idKsiazki) { this.idKsiazki = idKsiazki; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setTytul(String tytul) { this.tytul = tytul; }
    public void setWydawnictwo(String wydawnictwo) { this.wydawnictwo = wydawnictwo; }
    public void setDataDodania(String dataDodania) { this.dataDodania = dataDodania; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}

