package com.app.spark.Filter;

import com.app.spark.util.Tool;
import com.app.spark.model.Tracking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TrackingIsOldDebugger {
    public TrackingIsOldDebugger() {
    }
    public boolean Isold(Tracking tracking) {
        Date tracking_date = tracking.getTracking_time();
        if (tracking_date == null)
            return true;
        Date date_now = new Date();
        LocalDateTime local_tracking_date = Tool.DateTime.dateToLocalDateTime(tracking_date);
        LocalDateTime local_date_now = Tool.DateTime.dateToLocalDateTime(date_now);
//        Console.println(local_tracking_date.toString());
        return ChronoUnit.DAYS.between(local_tracking_date, local_date_now) > 30;
    }
}
