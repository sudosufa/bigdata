package com.app.spark.Filter;

import com.app.spark.util.Tool;
import com.app.spark.model.Tracking;

import java.util.Date;

public class SuspectTime {
    private long isSuspect_time;
    public SuspectTime(long isSuspect_time) {
        this.isSuspect_time = isSuspect_time;
    }

    public boolean Suspect_Time(Tracking tracking) {
        Date date_now = new Date();
        Date tracking_date = Tool.DateTime.stringToDate(tracking.getTracking_time());
        long diffDate =date_now.getTime() - tracking_date.getTime();
        //System.out.println("date now :"+date_now.getTime()+"diffDate : "+ diffDate + "tracking_date :"+tracking_date.getTime()+"//"+-isSuspect_time);
        if (diffDate < -isSuspect_time) {

            /*
            int isSuspect = Tools.checkSuspect(tracking.getId_device(), tracking.getId_vehicule());
            if (isSuspect <= 4) {
                if (isSuspect == 0) {
                    Tools.addSuspect(tracking.getId_device(), tracking.getId_vehicule());
                } else {
                    Tools.updateSuspect(tracking.getId_device(), tracking.getId_vehicule(), isSuspect + 1);
               }
            } else {
                if (isSuspect == 5 || (isSuspect + 1) % 50 == 0) {
                    MailOverTimeDTO mailOverTimeDTO = Tools.trackingDAO.getInfo(tracking.getId_device(), tracking.getId_vehicule());
                    mailOverTimeDTO.setNbreTracking(isSuspect + 1);
                    Tools.sendMail(mailOverTimeDTO);
                }
                Tools.trackingDAO.updateSuspect(tracking.getId_device(), tracking.getId_vehicule(), isSuspect + 1);
            }*/
            return true;
        }
        return false;
    }
}
