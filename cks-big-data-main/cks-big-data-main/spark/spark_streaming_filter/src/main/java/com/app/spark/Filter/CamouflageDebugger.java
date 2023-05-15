package com.app.spark.Filter;

import com.app.spark.util.Tool;
import com.app.spark.model.Tracking;

public class CamouflageDebugger {

    private boolean handelTemperature;
    public CamouflageDebugger(boolean handelTemperature) {
        this.handelTemperature = handelTemperature;
    }
    private static boolean speedLessThanOrEqualsAndAccInactive(Tracking tracking, int minAcceptedSpeed) {
        return tracking.getAcc_status() == 0 && tracking.getSpeed() <= minAcceptedSpeed;
    }
    public Tracking debugging(Tracking tracking, Tracking trackingPrevious) {
    try {
        boolean needCorrectData = false;
        Tracking previous = trackingPrevious ;
        if (previous != null) {
            if (tracking.getKm() == null) {
                tracking.setKm(Tool.distanceBetween(previous.getLatitude(), previous.getLongitude(), tracking.getLatitude(), tracking.getLongitude()));
            }


        }






    }
    catch (Exception e){
        e.printStackTrace();
    }


return tracking ;
    }


}
