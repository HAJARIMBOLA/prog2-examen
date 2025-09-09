package test;

import main.Prestataire;
import main.Promotion;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertTrue;

public class PrestataireTest {

    @Test
    public void testAjouterPromotion() {
        Prestataire prestataire = new Prestataire();
        LocalDate date = LocalDate.of(2025, 6, 1);
        double nouveauTJM = 500.0;
        String raison = "Nouvelle promotion";

        prestataire.ajouterPromotion(date, nouveauTJM, raison);

        Promotion promotion = new Promotion(date, nouveauTJM, raison);
        assertTrue(prestataire.getPromotions().contains(promotion));
    }
    @Test
    public void testPointageRouge() {
        Prestataire prestataire = new Prestataire();
        LocalDate date = LocalDate.of(2025, 6, 1);

        // Ajoute des missions pour que le total des quotas soit exactement 1.0
        prestataire.ajouterMission(date, 0.4);
        prestataire.ajouterMission(date, 0.6);

        assertTrue(prestataire.pointageRouge(date));
    }
    @Test
    public void testCalculerSalaire() {
        Prestataire prestataire = new Prestataire(1, "Nom", "Prenom", "email@test.com", "0600000000", 400.0);
        LocalDate debut = LocalDate.of(2025, 6, 1);
        LocalDate fin = LocalDate.of(2025, 6, 3);

        prestataire.ajouterMission(debut, 1.0);
        prestataire.ajouterMission(debut.plusDays(1), 1.0);
        prestataire.ajouterMission(debut.plusDays(2), 1.0);

        double salaire = prestataire.calculerSalaire(debut, fin);
        // 3 jours * 400 = 1200
        assertTrue(Math.abs(salaire - 1200.0) < 1e-6);
    }

    @Test
    public void testCalculerSalaireAvecPromotion() {
        Prestataire prestataire = new Prestataire(2, "Nom", "Prenom", "email@test.com", "0600000000", 400.0);
        LocalDate debut = LocalDate.of(2025, 6, 1);
        LocalDate fin = LocalDate.of(2025, 6, 3);

        prestataire.ajouterPromotion(debut.plusDays(1), 500.0, "Augmentation");

        prestataire.ajouterMission(debut, 1.0); // 400
        prestataire.ajouterMission(debut.plusDays(1), 1.0); // 500
        prestataire.ajouterMission(debut.plusDays(2), 1.0); // 500

        double salaire = prestataire.calculerSalaire(debut, fin);
        assertTrue(Math.abs(salaire - 1400.0) < 1e-6);
    }

}