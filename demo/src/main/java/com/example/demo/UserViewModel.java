package com.example.demo;

public class UserViewModel {
    private String imie;
    private String nazwisko;
    private String email;
    private String rola;

    public UserViewModel(String imie, String nazwisko, String email, String rola) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.rola = rola;
    }

    public String getImie() { return imie; }
    public String getNazwisko() { return nazwisko; }
    public String getEmail() { return email; }
    public String getRola() { return rola; }

    public void setRola(String rola) { this.rola = rola; }
}