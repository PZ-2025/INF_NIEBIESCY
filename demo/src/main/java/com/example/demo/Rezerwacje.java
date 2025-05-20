package com.example.demo;

import javafx.beans.property.*;

public class Rezerwacje {
    private final IntegerProperty id;
    private final StringProperty imie;
    private final StringProperty nazwisko;
    private final StringProperty email;
    private final StringProperty tytul;
    private final StringProperty wyp;
    private final StringProperty data_zamowienia;
    private final StringProperty planowana_data;
    private final BooleanProperty status;

    public Rezerwacje(int id, String imie, String nazwisko, String email, String tytul, String wyp, String data_zamowienia, String planowana_data, Boolean status) {
        this.id = new SimpleIntegerProperty(id);
        this.imie = new SimpleStringProperty(imie);
        this.nazwisko = new SimpleStringProperty(nazwisko);
        this.email = new SimpleStringProperty(email);
        this.tytul = new SimpleStringProperty(tytul);
        this.wyp = new SimpleStringProperty(wyp);
        this.data_zamowienia = new SimpleStringProperty(data_zamowienia);
        this.planowana_data = new SimpleStringProperty(planowana_data);
        this.status = new SimpleBooleanProperty(status);
    }


    public String getImie() {
        return imie.get();
    }

    public StringProperty imieProperty() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko.get();
    }

    public StringProperty nazwiskoProperty() {
        return nazwisko;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getTytul() {
        return tytul.get();
    }

    public StringProperty tytulProperty() {
        return tytul;
    }

    public String getWyp() {
        return wyp.get();
    }

    public StringProperty wypProperty() {
        return wyp;
    }

    public String getData_zamowienia() {
        return data_zamowienia.get();
    }

    public StringProperty data_zamowieniaProperty() {
        return data_zamowienia;
    }

    public String getPlanowana_data() {
        return planowana_data.get();
    }

    public StringProperty planowana_dataProperty() {
        return planowana_data;
    }

    public boolean isStatus() {
        return status.get();
    }

    public BooleanProperty statusProperty() {
        return status;
    }

    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean value) {
        selected.set(value);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }
}
