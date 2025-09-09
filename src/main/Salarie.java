package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Salarie extends Travailleur {
    private final double salaireMensuelInitial;
    private final List<Promotion> promotions = new ArrayList<>();

    public Salarie(int id, String nom, String prenom, String email, String telephone, double salaireMensuelInitial) {
        super(id, nom, prenom, email, telephone);
        this.salaireMensuelInitial = salaireMensuelInitial;
    }

    public void ajouterPromotion(LocalDate date, double nouveauSalaire, String raison) {
        promotions.add(new Promotion(date, nouveauSalaire, raison));
        promotions.sort(Comparator.comparing(Promotion::getDate));
    }

    public double getSalaireMensuelAu(LocalDate date) {
        double valeur = salaireMensuelInitial;
        for (int i = 0; i < promotions.size(); i++) {
            Promotion promotion = promotions.get(i);
            if (!promotion.getDate().isAfter(date)) {
                valeur = promotion.getNouvelleValeur();
            } else {
                break;
            }
        }
        return valeur;
    }
}
