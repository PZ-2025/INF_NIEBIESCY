package com.example.demo;

import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class WypozyczNOW {
    private final StringProperty tytul;
    private final StringProperty autor;
    private final StringProperty gatunek;
    private final StringProperty dostepne;
    private final BooleanProperty selected;

    public WypozyczNOW(String tytul, String autor, String gatunek, String dostepne) {
        this.tytul = new SimpleStringProperty(tytul);
        this.autor = new SimpleStringProperty(autor);
        this.gatunek = new SimpleStringProperty(gatunek);
        this.dostepne = new SimpleStringProperty(dostepne);
        this.selected = new SimpleBooleanProperty(false);
    }

    public StringProperty tytulProperty() { return tytul; }
    public StringProperty autorProperty() { return autor; }
    public StringProperty gatunekProperty() { return gatunek; }
    public StringProperty dostepneProperty() { return dostepne; }
    public BooleanProperty selectedProperty() { return selected; }

    public String getTytul() { return tytul.get(); }
    public boolean isSelected() { return selected.get(); }

    public String toString() {
        return String.format("WypozyczNOW{tytul='%s', autor='%s', gatunek='%s', dostepne='%s', selected=%s}",
                tytul.get(), autor.get(), gatunek.get(), dostepne.get(), selected.get());
    }
}