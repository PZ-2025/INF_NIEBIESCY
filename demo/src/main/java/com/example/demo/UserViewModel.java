package com.example.demo;

import javafx.beans.property.SimpleStringProperty;

public class UserViewModel {
    private String imie;
    private String nazwisko;
    private String email;
    private String rola;

    private int liczbaWypozyczen;
    private int liczbaRezerwacji;

    public UserViewModel(String imie, String nazwisko, String email, String rola, int wypozyczen, int rezerwacji) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.rola = rola;
        this.liczbaWypozyczen = wypozyczen;
        this.liczbaRezerwacji = rezerwacji;
    }

    public String getImie() { return imie; }
    public String getNazwisko() { return nazwisko; }
    public String getEmail() { return email; }
    public String getRola() { return rola; }

    public int getLiczbaWypozyczen() { return liczbaWypozyczen; }
    public int getLiczbaRezerwacji() { return liczbaRezerwacji; }

    public void setRola(String rola) { this.rola = rola; }



//    public SimpleStringProperty imieProperty() { return imie; }
//    public SimpleStringProperty nazwiskoProperty() { return nazwisko; }
//    public SimpleStringProperty emailProperty() { return email; }
//    public SimpleStringProperty rolaProperty() { return rola; }
//    public SimpleStringProperty liczbaWypozyczenProperty() { return liczbaWypozyczen; }
//    public SimpleStringProperty liczbaRezerwacjiProperty() { return liczbaRezerwacji; }
}

