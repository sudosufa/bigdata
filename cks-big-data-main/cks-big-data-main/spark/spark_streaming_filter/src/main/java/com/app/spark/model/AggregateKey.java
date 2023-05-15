package com.app.spark.model;

import java.io.Serializable;

/**
 * Key class for calculation
 *
 * @author abaghel
 */
public class AggregateKey implements Serializable {

    private Integer year;
    private Integer month ;


    public AggregateKey(int year, int month) {
        this.year = year;
        this.month = month;

    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }



    @Override
    public String toString() {
        return "AggregateKey{" +
                "year=" + year +
                ", month=" + month +

                '}';
    }
}

