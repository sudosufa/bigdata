package model;

public class Zone {
    private int id;
    private String label;
    private String geomtype;
    private int id_societe;

    public Zone(int id, String label, String geomtype, int id_societe) {
        this.id = id;
        this.label = label;
        this.geomtype = geomtype;
        this.id_societe = id_societe;
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

    public String getGeomtype() {
        return geomtype;
    }

    public void setGeomtype(String geomtype) {
        this.geomtype = geomtype;
    }

    public int getId_societe() {
        return id_societe;
    }

    public void setId_societe(int id_societe) {
        this.id_societe = id_societe;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", geomtype='" + geomtype + '\'' +
                ", id_societe=" + id_societe +
                '}';
    }
}
