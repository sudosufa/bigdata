package alert;

import model.Alert;
import model.ProgramedAlert;
import model.Tracking;
import util.AlertUtils;


public class StopAlert {

    public Alert alert(ProgramedAlert programedAlert, Tracking current, Tracking previous) {
        Alert alert = null;
        if (checkStopAlert(programedAlert, current, previous)) {
            if (current!= null) {
                alert = new Alert(programedAlert.getCategory(),
                        current.getTracking_time(),
                        0,
                        programedAlert.getId(),
                        "LE VEHICLUE A ARRTE A " + current.getTracking_time(),
                        current.getId_vehicle(),
                        current.getId_societe(),
                        current,
                        programedAlert.getEmails(),
                        current.getSpeed(),
                        programedAlert.getLimitSpeed());
            }
        }
        System.out.println(alert);
        return alert;
    }

    public static boolean checkStopAlert(ProgramedAlert programedAlert, Tracking current, Tracking previous) {
        boolean checked = false;
        if (AlertUtils.checkDateTime(programedAlert, current) && previous.getAcc_status() != null) {
            if (current.getAcc_status() == 0 && previous.getAcc_status() == 1) {
                checked = true;
            }
        }
        return checked;
    }
}
