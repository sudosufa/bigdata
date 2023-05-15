package Filter;
import model.Tracking;
import util.Tools;
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
        LocalDateTime local_tracking_date = Tools.DateTime.dateToLocalDateTime(tracking_date);
        LocalDateTime local_date_now = Tools.DateTime.dateToLocalDateTime(date_now);
//        Console.println(local_tracking_date.toString());
        return ChronoUnit.DAYS.between(local_tracking_date, local_date_now) > 30;
    }
}
