package main;

import java.time.LocalDate;
import java.util.Objects;

public class Promotion {
    private final LocalDate date;
    private final double nouvelleValeur;
    private final String raison;

    public Promotion(LocalDate date, double nouvelleValeur, String raison) {
        if (date == null) throw new IllegalArgumentException("date promotion nulle");
        this.date = date;
        this.nouvelleValeur = nouvelleValeur;
        this.raison = raison == null ? "" : raison;
    }

    public LocalDate getDate() {
        return date;
    }
    public double getNouvelleValeur() {
        return nouvelleValeur;
    }
    public String getRaison() {
        return raison;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Promotion)) return false;
        Promotion that = (Promotion) o;
        return Double.compare(that.nouvelleValeur, nouvelleValeur) == 0
                && date.equals(that.date)
                && raison.equals(that.raison);
    }
    @Override
    public int hashCode() {
        return Objects.hash(date, nouvelleValeur, raison);
    }
}

