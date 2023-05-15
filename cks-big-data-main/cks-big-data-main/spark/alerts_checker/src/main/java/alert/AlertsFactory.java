package alert;

import model.Alert;
import model.ProgramedAlert;
import model.Tracking;
import util.AlertUtils;
import java.util.*;

public class AlertsFactory {
    public List<Alert> getAlert(Tracking current, Tracking previous) {
        List<Alert> alertList = new ArrayList<>();
        int id_societe = current.getId_societe(); //48;
        int id_vehicle = current.getId_vehicle(); //998916;
        AlertUtils alertUtils = new AlertUtils();
        List<ProgramedAlert> listProgramedAlerts = alertUtils.listProgramedAlerts(id_societe, id_vehicle);
//        Set<ProgramedAlert> set = listProgramedAlerts.stream()
//                .collect(Collectors.toCollection(() ->
//                        new TreeSet<>(Comparator.comparing(ProgramedAlert::getCategory))));
//        listProgramedAlerts.clear();
//        listProgramedAlerts.addAll(set);
        if (!listProgramedAlerts.isEmpty()) {
            for (ProgramedAlert programedAlert : listProgramedAlerts) {
                System.out.println(programedAlert.getCategory());
                switch (programedAlert.getCategory().toUpperCase()) {
                    case "ARRETS":
                        Alert stopAlert = new StopAlert().alert(programedAlert, current, previous);
                        if (stopAlert != null) alertList.add(stopAlert);
                        break;
                    case "DEMARRAGE":
                        Alert startAlert = new StartAlert().alert(programedAlert, current, previous);
                        if (startAlert != null) alertList.add(startAlert);
                        break;
                    case "ALIMENTATION":
                        Alert energyAlert = new EnergyAlert().alert(programedAlert, current, previous);
                        if (energyAlert != null) alertList.add(energyAlert);
                        break;
                    case "GEOFENCING":
                        List<Alert> geofencingAlerts = new GeofencingAlert().alert(programedAlert, current, previous);
                        if (!geofencingAlerts.isEmpty()) alertList.addAll(geofencingAlerts);
                        break;
                    case "POI":
                        List<Alert> poiAlerts = new PoiAlert().alert(programedAlert, current, previous);
                        if (!poiAlerts.isEmpty()) alertList.addAll(poiAlerts);
                        break;
                    case "VITESSE":
                        Alert speedAlert = new SpeedAlert().alert(programedAlert, current);
                        if (speedAlert != null) alertList.add(speedAlert);
                        break;
                    case "LIMIT VITESSE":
                        break;
                }
            }
        }
        return alertList;
    }
}
