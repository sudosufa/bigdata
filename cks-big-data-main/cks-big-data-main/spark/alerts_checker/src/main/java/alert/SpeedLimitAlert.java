package alert;

import model.ProgramedAlert;
import model.Tracking;
import util.AlertUtils;

public class SpeedLimitAlert {

    private static int PERCENTAGE_ERROR = 15;
    private String observation = " ";
    private int RoadLimitSpeed = 0;

    public void setRoadLimitSpeed(int roadLimitSpeed) {
        RoadLimitSpeed = roadLimitSpeed;
    }

    public void setObservation(String registrationNumber, int speedLimit, int currentSpeed) {
        this.observation ="Le véhicule "
                + registrationNumber
                + " a depassé la limitation de vitesse ["
                + speedLimit
                + " km/h], vitesse enregistré ("
                + currentSpeed + "km/h)";
    }


    public boolean checkSpeedLimitAlert(ProgramedAlert programedAlert, Tracking current, Tracking previous){
        boolean checked = false;
        int roadSpeedLimit = 0;
        util.AlertUtils alertUtils = new AlertUtils();

        try {
            if (alertUtils.checkDateTime(programedAlert , current)) {

               return false;
               /*
               todo get road speed limit (params: latitude & longitude ,  return: speed limit )
               todo compare current speed to previous speed
               */

            }
        } catch (Exception e) {
            System.out.println("Error in Alert limit speed Trigger");
        }
        return checked;
    }
}
