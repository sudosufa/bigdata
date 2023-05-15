package com.app.spark.model;

import java.io.Serializable;

public class minTracking implements Serializable {

    private int year;
    private int month ;
    private Integer id_vehicule;
    private Integer id_tracking;
    private int can_speed ;






    private int accelerator_pedal_position;






    private String reception_timespan;



    public minTracking(int year, int month, Integer id_vehicule, Integer id_tracking) {
        this.year = year;
        this.month = month;
        this.id_vehicule = id_vehicule;
        this.id_tracking = id_tracking;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Integer getId_vehicule() {
        return id_vehicule;
    }

    public void setId_vehicule(Integer id_vehicule) {
        this.id_vehicule = id_vehicule;
    }

    public Integer getId_tracking() {
        return id_tracking;
    }

    public void setId_tracking(Integer id_tracking) {
        this.id_tracking = id_tracking;
    }

    @Override
    public String toString() {
        return "minTracking{" +
                "year=" + year +
                ", month=" + month +
                ", id_vehicule=" + id_vehicule +
                ", id_tracking=" + id_tracking +
                '}';
    }
}
