package model;

import java.awt.image.ImageProducer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Alert {
    private Integer id_alert;
    private String type;
    private Timestamp date_alert;
    private Integer vue;
    private Integer id_programed_alert;
    private String observation;
    private Integer id_vehicle;
    private Integer id_societe;
    private Tracking tracking_object;
    private String emails;
    private Double current_speed;
    private  Integer limit_speed;

    public Tracking getTracking_object() {
        return tracking_object;
    }

    public void setTracking_object(Tracking tracking_object) {
        this.tracking_object = tracking_object;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public Double getCurrent_speed() {
        return current_speed;
    }

    public void setCurrent_speed(Double current_speed) {
        this.current_speed = current_speed;
    }

    public Integer getLimit_speed() {
        return limit_speed;
    }

    public void setLimit_speed(Integer limit_speed) {
        this.limit_speed = limit_speed;
    }

    public Alert(String type, Timestamp date_alert, Integer vue, Integer id_programed_alert, String observation, Integer id_vehicle, Integer id_societe, Tracking tracking_object, String emails, Double current_speed, Integer limit_speed) {
        this.type = type;
        this.date_alert = date_alert;
        this.vue = vue;
        this.id_programed_alert = id_programed_alert;
        this.observation = observation;
        this.id_vehicle = id_vehicle;
        this.id_societe = id_societe;
        this.tracking_object = tracking_object;
        this.emails = emails;
        this.current_speed = current_speed;
        this.limit_speed = limit_speed;
    }

    public Alert(){
    }
    public Integer getId_alert() {
        return id_alert;
    }

    public void setId_alert(Integer id_alert) {
        this.id_alert = id_alert;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getDate_alert() {
        return date_alert;
    }

    public void setDate_alert(Timestamp date_alert) {
        this.date_alert = date_alert;
    }

    public Integer getId_societe() {
        return id_societe;
    }

    public void setId_societe(Integer id_societe) {
        this.id_societe = id_societe;
    }

    public Integer getVue() {
        return vue;
    }

    public void setVue(Integer vue) {
        this.vue = vue;
    }

    public Integer getId_programed_alert() {
        return id_programed_alert;
    }

    public void setId_programed_alert(Integer id_programed_alert) {
        this.id_programed_alert = id_programed_alert;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Integer getId_vehicle() {
        return id_vehicle;
    }

    public void setId_vehicle(Integer id_vehicle) {
        this.id_vehicle = id_vehicle;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id_alert=" + id_alert +
                ", type='" + type + '\'' +
                ", date_alert=" + date_alert +
                ", vue=" + vue +
                ", id_programed_alert=" + id_programed_alert +
                ", observation='" + observation + '\'' +
                ", id_vehicle=" + id_vehicle +
                ", id_societe=" + id_societe +
                ", tracking_object=" + tracking_object +
                ", emails=" + emails +
                ", current_speed=" + current_speed +
                ", limit_speed=" + limit_speed +
                '}';
    }
}

