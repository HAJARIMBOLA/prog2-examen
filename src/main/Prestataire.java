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
        List<Mission> ms = missionsDu(date);
        if (ms.isEmpty()) return false;

        double total = 0.0;
        for (Mission m : ms) {
            double q = m.getQuota();
            if (q <= 0.0 || q > 1.0) {
                throw new IllegalArgumentException("Quota invalide: " + q + " (doit être dans (0,1])");
            }
            total += q;
        }
        return Math.abs(total - 1.0) < 1e-9;
    }
    public double getDaysRed(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) throw new IllegalArgumentException("Intervalle invalide");
        double jours = 0.0;

        for (LocalDate d = debut; !d.isAfter(fin); d = d.plusDays(1)) {
            LocalDate finalD = d;
            double totalNonAbs = missionsDu(d).stream()
                    .peek(m -> {
                        double q = m.getQuota();
                        if (q <= 0.0 || q > 1.0)
                            throw new IllegalArgumentException("Quota invalide le " + finalD + " : " + q);
                    })
                    .filter(m -> m.getType() != TypeTravail.ABS_PAYEE && m.getType() != TypeTravail.ABS_NON_PAYEE)
                    .mapToDouble(Mission::getQuota)
                    .sum();

            if (totalNonAbs > 1.0)
                throw new IllegalArgumentException("Somme des quotas non-absences > 1 le " + d);

            jours += totalNonAbs; // 1.0 => 1 jour, 0.5 => 0.5 jour
        }
        return jours;
    }
    public double calculerSalaire(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) throw new IllegalArgumentException("Intervalle invalide");
        double total = 0.0;
        for (LocalDate d = debut; !d.isAfter(fin); d = d.plusDays(1)) {
            LocalDate finalD = d;
            double joursCeJour = missionsDu(d).stream()
                    .peek(m -> {
                        double q = m.getQuota();
                        if (q <= 0.0 || q > 1.0)
                            throw new IllegalArgumentException("Quota invalide le " + finalD + " : " + q);
                    })
                    .filter(m -> m.getType() != TypeTravail.ABS_PAYEE && m.getType() != TypeTravail.ABS_NON_PAYEE)
                    .mapToDouble(Mission::getQuota)
                    .sum();

            if (joursCeJour > 1.0)
                throw new IllegalArgumentException("Somme des quotas non-absences > 1 le " + d);

            total += joursCeJour * getTjmAu(d);
        }
        return total;
    }
}

