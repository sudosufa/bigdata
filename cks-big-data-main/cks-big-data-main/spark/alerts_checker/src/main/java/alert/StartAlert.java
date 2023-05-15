package alert;

import model.Alert;
import model.ProgramedAlert;
import model.Tracking;
import util.AlertUtils;

public class StartAlert {

    public Alert alert(ProgramedAlert programedAlert, Tracking current, Tracking previous) {
        Alert alert = null;
        if (checkStartAlert(programedAlert, current, previous)) {
            alert = new Alert(programedAlert.getCategory(),
                    current.getTracking_time(),0,programedAlert.getId(),
                    "Le véhicule est démarré " + current.getTracking_time(),
                    current.getId_vehicle(),
                    current.getId_societe(),current,programedAlert.getEmails(),current.getSpeed()
                    ,programedAlert.getLimitSpeed());
        }
        System.out.println(alert);
        return alert;
    }

    public static boolean checkStartAlert(ProgramedAlert programedAlert, Tracking current, Tracking previous) {
        boolean checked = false;
        if (AlertUtils.checkDateTime(programedAlert, current) && previous.getAcc_status() != null) {
            if (current.getAcc_status() == 1 && previous.getAcc_status() == 0) {
                checked = true;
            }
        }
        return checked;
    }
}
