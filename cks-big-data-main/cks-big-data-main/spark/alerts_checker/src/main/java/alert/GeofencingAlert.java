package alert;

import model.Alert;
import model.ProgramedAlert;
import model.Tracking;
import model.Zone;
import util.AlertUtils;
import util.ZoneUtils;

import java.util.ArrayList;
import java.util.List;


public class GeofencingAlert {

    private List<String> obs = new ArrayList<>();

    public List<Alert> alert(ProgramedAlert programedAlert, Tracking current, Tracking previous) {
        List<Alert> alertList = new ArrayList<>();
        Alert alert ;
        if (checkGeofencingAlert(programedAlert, current, previous)) {
            for (String ob : obs) {
                alert = new Alert(programedAlert.getCategory(),
                        current.getTracking_time(),0,programedAlert.getId(),
                        ob + current.getTracking_time(),
                        current.getId_vehicle(),
                        current.getId_societe(),current,programedAlert.getEmails(),current.getSpeed()
                        ,programedAlert.getLimitSpeed());
                alertList.add(alert);
            }
        }
        for (Alert al: alertList) {
            System.out.println(al);
        }
        return alertList;
    }

    public boolean checkGeofencingAlert(ProgramedAlert programedAlert, Tracking current, Tracking previous) {
        try {
            boolean checked = false;
            if (AlertUtils.checkDateTime(programedAlert, current) && programedAlert.getIdZones() != null) {
                if (programedAlert.getEvent().equalsIgnoreCase("sortie")
                        || programedAlert.getEvent().equalsIgnoreCase("entre/sortie")) {
                    List<Zone> lastZones = ZoneUtils.getLastZone(previous, programedAlert.getIdZones());
                    if (!lastZones.isEmpty()) {
                        for (Zone zone : lastZones) {
                            if (!ZoneUtils.isInside(current, zone)) {
                                obs.add("SORTIE DE " + zone.getLabel());
                                checked = true;
                            }
                        }
                    }
                }

                if (programedAlert.getEvent().equalsIgnoreCase("entre")
                        || programedAlert.getEvent().equalsIgnoreCase("entre/sortie")) {

                    List<Zone> lastZones = ZoneUtils.getLastZone(previous, programedAlert.getIdZones());
                    List<Zone> currentZones = ZoneUtils.getCurrentZone(current, programedAlert.getIdZones());

                    if (!currentZones.isEmpty()) {
                        for (Zone zone : currentZones) {
                            if (!lastZones.isEmpty()) {
                                if (!ZoneUtils.contains(lastZones, zone)) {
                                    obs.add("ENTREE A " + zone.getLabel());
                                    checked = true;
                                }
                            } else {
                                obs.add("ENTREE A " + zone.getLabel());
                                checked = true;
                            }
                        }
                    }

                }

                if (programedAlert.isStopped()) {

                    List<Zone> currentZones = ZoneUtils.getCurrentZone(current, programedAlert.getIdZones());
                    if (!currentZones.isEmpty()) {
                        if (current.getMessage() != null) {
                            if (current.getMessage().equals("acc off") || current.getMessage().equals("ACCStop")) {
                                for (Zone zone : currentZones) {
                                    obs.add("LE VEHICULE S'EST ARRETE A :" + zone.getLabel());
                                    checked = true;
                                }
                            }
                        }
                    }

                }
                if (programedAlert.getLimitSpeed() != 0) {
                    List<Zone> currentZones = ZoneUtils.getCurrentZone(current, programedAlert.getIdZones());
                    if (!currentZones.isEmpty()) {
                        if (current.getSpeed() > programedAlert.getLimitSpeed()) {
                            for (Zone zone : currentZones) {
                                obs.add("DEPASSEMENT DE VITESSE LIMITE : " + current.getSpeed() + " DANS LA ZONE : "
                                        + zone.getLabel());
                                checked = true;
                            }
                        }
                    }
                }
            }
            return checked;
        } catch (Exception e) {
            System.out.println(" Error occurred in  Geofencing  : " + programedAlert.getIdZones());
            e.printStackTrace();
            return false;
        }
    }
}
