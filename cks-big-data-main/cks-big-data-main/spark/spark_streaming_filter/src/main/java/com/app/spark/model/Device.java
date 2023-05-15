package com.app.spark.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Device implements Serializable {

    private Integer ID_Device ;
    private String IMEI;
    private Timestamp installed_date   ;
    private  String codec   ;
    private String brand ;
    private  Double voltage_minimal ;
    private  Double volReservoir1Max ;
    private  Double volReservoir1Min ;
    private  Double volReservoir2Max ;
    private  Double volReservoir2Min;
    private Integer id_vehicule   ;
    private Integer fk_id_societe   ;
    private  Integer id_marque   ;
    private  String  installer    ;

    public Device() {
    }

    public Integer getID_Device() {
        return ID_Device;
    }

    public void setID_Device(Integer ID_Device) {
        this.ID_Device = ID_Device;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public Timestamp getInstalled_date() {
        return installed_date;
    }

    public void setInstalled_date(Timestamp installed_date) {
        this.installed_date = installed_date;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getVoltage_minimal() {
        return voltage_minimal;
    }

    public void setVoltage_minimal(Double voltage_minimal) {
        this.voltage_minimal = voltage_minimal;
    }

    public Double getVolReservoir1Max() {
        return volReservoir1Max;
    }

    public void setVolReservoir1Max(Double volReservoir1Max) {
        this.volReservoir1Max = volReservoir1Max;
    }

    public Double getVolReservoir1Min() {
        return volReservoir1Min;
    }

    public void setVolReservoir1Min(Double volReservoir1Min) {
        this.volReservoir1Min = volReservoir1Min;
    }

    public Double getVolReservoir2Max() {
        return volReservoir2Max;
    }

    public void setVolReservoir2Max(Double volReservoir2Max) {
        this.volReservoir2Max = volReservoir2Max;
    }

    public Double getVolReservoir2Min() {
        return volReservoir2Min;
    }

    public void setVolReservoir2Min(Double volReservoir2Min) {
        this.volReservoir2Min = volReservoir2Min;
    }

    public Integer getId_vehicule() {
        return id_vehicule;
    }

    public void setId_vehicule(Integer id_vehicule) {
        this.id_vehicule = id_vehicule;
    }

    public Integer getFk_id_societe() {
        return fk_id_societe;
    }

    public void setFk_id_societe(Integer fk_id_societe) {
        this.fk_id_societe = fk_id_societe;
    }

    public Integer getId_marque() {
        return id_marque;
    }

    public void setId_marque(Integer id_marque) {
        this.id_marque = id_marque;
    }

    public String getInstaller() {
        return installer;
    }

    public void setInstaller(String installer) {
        this.installer = installer;
    }

    @Override
    public String toString() {
        return "Device{" +
                "ID_Device=" + ID_Device +
                ", IMEI='" + IMEI + '\'' +
                ", installed_date=" + installed_date +
                ", codec='" + codec + '\'' +
                ", brand='" + brand + '\'' +
                ", voltage_minimal=" + voltage_minimal +
                ", volReservoir1Max=" + volReservoir1Max +
                ", volReservoir1Min=" + volReservoir1Min +
                ", volReservoir2Max=" + volReservoir2Max +
                ", volReservoir2Min=" + volReservoir2Min +
                ", id_vehicule=" + id_vehicule +
                ", fk_id_societe=" + fk_id_societe +
                ", id_marque=" + id_marque +
                ", installer='" + installer + '\'' +
                '}';
    }
}
