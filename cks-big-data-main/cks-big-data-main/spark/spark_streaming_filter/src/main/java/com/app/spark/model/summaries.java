package com.app.spark.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class summaries implements Serializable {
    private Integer id_vehicule;
    private Timestamp updated_at;
    private Timestamp depuis;
    private Double km;
    private int acc_status ;
    private  String  device_type ;
    private Double longitude;
    private Double latitude;

    public Integer getId_vehicule() {
        return id_vehicule;
    }

    public void setId_vehicule(Integer id_vehicule) {
        this.id_vehicule = id_vehicule;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public int getAcc_status() {
        return acc_status;
    }

    public void setAcc_status(int acc_status) {
        this.acc_status = acc_status;
    }

    public Timestamp getDepuis() {
        return depuis;
    }

    public void setDepuis(Timestamp depuis) {
        this.depuis = depuis;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "summeries{" +
                "id_vehicule=" + id_vehicule +
                ", updated_at='" + updated_at + '\'' +
                ", depuis='" + depuis + '\'' +
                ", km=" + km +
                ", acc_status=" + acc_status +
                ", device_type='" + device_type + '\'' +
                '}';
    }
}
