package com.example.demo;

public class Pracownik {
    private int id_pracownika;
    private String email;
    private String haslo;
    private String imie;
    private String nazwisko;
    private String rola;

    // Konstruktor
    public Pracownik(int id_pracownika, String email, String haslo, String imie, String nazwisko, String rola) {
        this.id_pracownika = id_pracownika;
        this.email = email;
        this.haslo = haslo;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.rola = rola;
    }

    // Gettery i settery
    public int getId() {
        return id_pracownika;
    }

    public void setId(int id_pracownika) {
        this.id_pracownika = id_pracownika;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = email;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) { this.rola = rola; }
}
