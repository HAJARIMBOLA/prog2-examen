package main;

import java.time.LocalDate;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Travailleur {
    private final int id;
    private final String nom;
    private final String prenom;
    private final String email;
    private final String telephone;

    protected final Map<LocalDate, List<Mission>> pointages = new HashMap<>();

    protected Travailleur(int id, String nom, String prenom, String email, String telephone) {
        this.id = id;
        this.nom = nom == null ? "" : nom;
        this.prenom = prenom == null ? "" : prenom;
        this.email = email == null ? "" : email;
        this.telephone = telephone == null ? "" : telephone;
    }

    public void ajouterMission(LocalDate date, Mission mission) {
        if (date == null || mission == null) throw new IllegalArgumentException("date/mission null");
        pointages.computeIfAbsent(date, d -> new ArrayList<>()).add(mission);
    }

    public List<Mission> missionsDu(LocalDate date) {

        return pointages.getOrDefault(date, Collections.emptyList());
    }

    public int getId() {

        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public String getEmail() {
        return email;
    }
    public String getTelephone() {
        return telephone;
    }
}
