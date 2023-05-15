package alert;

import model.Alert;
import model.ProgramedAlert;
import model.Tracking;

public class EnergyAlert {

    private String observation;

    public Alert alert(ProgramedAlert programedAlert, Tracking current, Tracking previous) {
        Alert alert = null;
        if (checkEnergyAlert(programedAlert, current, previous)) {
            alert = new Alert(programedAlert.getCategory(),
                    current.getTracking_time(),
                    0,
                    programedAlert.getId(),
                    observation + current.getTracking_time(),
                    current.getId_vehicle(),
                    current.getId_societe(),
                    current,
                    programedAlert.getEmails(),
                    current.getSpeed(),
                    programedAlert.getLimitSpeed());
        }
        System.out.println(alert);
        return alert;
    }

    public boolean checkEnergyAlert(ProgramedAlert programedAlert, Tracking current, Tracking previous) {
        try {
            boolean checked = false;
            if (programedAlert.isEnergy() || programedAlert.isBattery()) {

                boolean isBatteryAlert = programedAlert.isBattery()
                        && (current.getCurrent_battery() <= 3.65 || current.getCurrent_battery() == 10)
                        && ((current.getMessage().equalsIgnoreCase("Low battery")
                        && (current.getExternal_power() == 0 || current.getExternal_power() == 2))
                        || (current.getMessage().equalsIgnoreCase("battery"))
                        || (current.getMessage().equalsIgnoreCase("Low batt")));

                boolean isBatteryAlertPrevious = (previous.getCurrent_battery() <= 3.65
                        || previous.getCurrent_battery() == 10)
                        && ((previous.getMessage().equalsIgnoreCase("Low battery")
                        && (previous.getExternal_power() == 0 || current.getExternal_power() == 2))
                        || (previous.getMessage().equalsIgnoreCase("battery"))
                        || (previous.getMessage().equalsIgnoreCase("Low batt")));

                if (isBatteryAlert && !isBatteryAlertPrevious) {
                    this.observation = "L'ENERGIE DU TRACKER EST FAIBLE ";
                    checked = true;
                }

                boolean isEnergyAlert = programedAlert.isEnergy()
                        && (current.getMessage().equalsIgnoreCase("battery")
                        && (current.getExternal_power() == 0 || current
                        .getExternal_power() == 2)
                        || (current.getMessage()
                        .equalsIgnoreCase("ac alarm"))
                        || (current.getMessage().equalsIgnoreCase("Low battery")
                        && current.getExternal_power() == 0));

                boolean isEnergyAlertPrevious = programedAlert.isEnergy()
                        && ((previous.getMessage().equalsIgnoreCase("battery")
                        && (previous.getExternal_power() == 0 || previous
                        .getExternal_power() == 2)
                        || (previous.getMessage()
                        .equalsIgnoreCase("ac alarm")) || (previous
                        .getMessage().equalsIgnoreCase("Low battery") && previous
                        .getExternal_power() == 0)));

                if (isEnergyAlert && !isEnergyAlertPrevious) {
                    this.observation = "DEBRANCHEMENT DU TRACKER ";
                    checked = true;
                }
            }
            return checked;
        } catch (Exception e) {
            return false;
        }
    }

}
