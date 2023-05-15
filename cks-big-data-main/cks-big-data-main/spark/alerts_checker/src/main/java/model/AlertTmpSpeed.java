package model;

import java.sql.Timestamp;

public class AlertTmpSpeed {

    private Integer id_alert;
    private  Integer id_vehicle;
    private Integer id_societe;
    private  Double speed;
    private Timestamp tracking_time;
    private Double km;

    public AlertTmpSpeed() {
    }

    public AlertTmpSpeed(Integer id_vehicle, Integer id_societe, Double speed, Timestamp tracking_time, Double km) {
        this.id_vehicle = id_vehicle;
        this.id_societe = id_societe;
        this.speed = speed;
        this.tracking_time = tracking_time;
        this.km = km;
    }

    public Integer getId_alert() {
        return id_alert;
    }

    public void setId_alert(Integer id_alert) {
        this.id_alert = id_alert;
    }

    public Integer getId_vehicle() {
        return id_vehicle;
    }

    public void setId_vehicle(Integer id_vehicle) {
        this.id_vehicle = id_vehicle;
    }

    public Integer getId_societe() {
        return id_societe;
    }

    public void setId_societe(Integer id_societe) {
        this.id_societe = id_societe;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Timestamp getTracking_time() {
        return tracking_time;
    }

    public void setTracking_time(Timestamp tracking_time) {
        this.tracking_time = tracking_time;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    @Override
    public String toString() {
        return "AlertTmpSpeed{" +
                "id_alert=" + id_alert +
                ", id_vehicle=" + id_vehicle +
                ", id_societe=" + id_societe +
                ", speed=" + speed +
                ", tracking_time=" + tracking_time +
                ", km=" + km +
                '}';
    }
}

