package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Prestataire extends Travailleur {
    private final double tjmInitial;
    private final List<Promotion> promotions = new ArrayList<>();

    public Prestataire(int id, String nom, String prenom, String email, String telephone, double tjmInitial) {
        super(id, nom, prenom, email, telephone);
        this.tjmInitial = tjmInitial;
    }

    public void ajouterPromotion(LocalDate date, double nouveauTJM, String raison) {
        promotions.add(new Promotion(date, nouveauTJM, raison));
        promotions.sort(Comparator.comparing(Promotion::getDate));
    }

    public double getTjmAu(LocalDate date) {
        double valeur = tjmInitial;
        for (Promotion promo : promotions) {
            if (!promo.getDate().isAfter(date)) {
                valeur = promo.getNouvelleValeur();
            } else {
                break;
            }
        }
        return valeur;
    }
    public boolean pointageRouge(LocalDate date) {
        double total = missionsDu(date).stream()
            .mapToDouble(Mission::getQuota)
            .peek(q -> {
                if (q <= 0.0 || q > 1.0) {
                    return;
                }
            })
            .sum();
        return Math.abs(total - 1.0) < 1e-9 && missionsDu(date).size() > 0;
    }
    public double getDaysRed(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) return 0.0;
        double jours = 0.0;
        LocalDate date = debut;
        while (!date.isAfter(fin)) {
            double totalNonAbs = missionsDu(date).stream()
                    .peek(m -> {
                        double q = m.getQuota();
                        // On ignore les quotas invalides au lieu de lancer une exception
                    })
                    .filter(m -> m.getType() != TypeTravail.ABS_PAYEE && m.getType() != TypeTravail.ABS_NON_PAYEE)
                    .mapToDouble(Mission::getQuota)
                    .filter(q -> q > 0.0 && q <= 1.0)
                    .sum();

            if (totalNonAbs <= 1.0) {
                jours += totalNonAbs;
            }
            date = date.plusDays(1);
        }
        return jours;
    }
    public double calculerSalaire(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) throw new IllegalArgumentException("Intervalle invalide");
        double total = 0.0;
        LocalDate date = debut;
        while (!date.isAfter(fin)) {
            LocalDate finalDate = date;
            double joursCeJour = missionsDu(date).stream()
                    .peek(m -> {
                        double q = m.getQuota();
                        if (q <= 0.0 || q > 1.0)
                            throw new IllegalArgumentException("Quota invalide le " + finalDate + " : " + q);
                    })
                    .filter(m -> m.getType() != TypeTravail.ABS_PAYEE && m.getType() != TypeTravail.ABS_NON_PAYEE)
                    .mapToDouble(Mission::getQuota)
                    .sum();

            if (joursCeJour > 1.0)
                throw new IllegalArgumentException("Total des quotas > 1.0 le " + date + " : " + joursCeJour);

            total += joursCeJour * getTjmAu(date);
            date = date.plusDays(1);
        }
        return total;
    }
}

