package com.example.demo;

public class Czytelnik {
    private int id_czytelnika;
    private String login;
    private String haslo;
    private String imie;
    private String nazwisko;

    // Konstruktor
    public Czytelnik(int id_czytelnika, String login, String haslo, String imie, String nazwisko) {
        this.id_czytelnika = id_czytelnika;
        this.login = login;
        this.haslo = haslo;
        this.imie = imie;
        this.nazwisko = nazwisko;
    }

    // Gettery i settery
    public int getId() {
        return id_czytelnika;
    }

    public void setId(int id_czytelnika) {
        this.id_czytelnika = id_czytelnika;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    @Override
    public String toString() {
        return imie + " " + nazwisko;
    }

}
