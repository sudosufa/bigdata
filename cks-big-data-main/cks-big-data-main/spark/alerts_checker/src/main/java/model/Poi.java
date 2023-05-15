package model;

public class Poi {

    private int id;
    private String label;
    private int rayon;
    private double longitude;
    private double latitude;
    private int id_societe;
    private String rayons;

    public Poi(int id, String label, int rayon, double longitude, double latitude, int id_societe) {
        this.id = id;
        this.label = label;
        this.rayon = rayon;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id_societe = id_societe;
    }

    public Poi() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getRayon() {
        return rayon;
    }

    public void setRayon(int rayon) {
        this.rayon = rayon;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getId_societe() {
        return id_societe;
    }

    public void setId_societe(int id_societe) {
        this.id_societe = id_societe;
    }

    public String getRayons() {
        return rayons;
    }

    public void setRayons(String rayons) {
        this.rayons = rayons;
    }

    @Override
    public String toString() {
        return "Poi{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", rayon=" + rayon +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", id_societe=" + id_societe +
                ", rayons='" + rayons + '\'' +
                '}';
    }
}
