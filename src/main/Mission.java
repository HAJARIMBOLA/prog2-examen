package main;

public class Mission {
    private final TypeTravail type;
    private final double quota;
    private final String description;
    private final String couleur;

    public Mission(TypeTravail type, double quota, String description, String couleur) {
        if (type == null) throw new IllegalArgumentException("type nul");
        this.type = type;
        this.quota = quota;
        this.description = description == null ? "" : description;
        this.couleur = couleur == null ? "" : couleur;
    }

    public TypeTravail getType() {
        return type;
    }
    public double getQuota() {
        return quota;
    }
    public String getDescription() {
        return description;
    }
    public String getCouleur() {
        return couleur;
    }
}

