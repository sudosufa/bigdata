package alert;

import model.Alert;
import model.Poi;
import model.ProgramedAlert;
import model.Tracking;
import util.AlertUtils;
import util.PoiUtils;

import java.util.ArrayList;
import java.util.List;

public class PoiAlert {

    private List<String> obs = new ArrayList<>();

    public List<Alert> alert(ProgramedAlert programedAlert, Tracking current, Tracking previous) {
        List<Alert> alertList = new ArrayList<>();
        Alert alert;
        if (checkPoiAlert(programedAlert, current, previous)) {
            for (String ob : obs) {
                alert = new Alert(programedAlert.getCategory(),
                        current.getTracking_time(), 0, programedAlert.getId(),
                        ob,
                        current.getId_vehicle(),
                        current.getId_societe(), current, programedAlert.getEmails(), current.getSpeed()
                        , programedAlert.getLimitSpeed());
                alertList.add(alert);
            }
        }
        System.out.println(alertList);
        return alertList;
    }

    private boolean checkPoiAlert(ProgramedAlert programedAlert, Tracking current, Tracking previous) {
        String rayon = "p_poi.p_rayon";
        boolean checked = false;
        try {
            if (AlertUtils.checkDateTime(programedAlert, current)) {
                if (programedAlert.getEvent().equalsIgnoreCase("sortie")
                        || programedAlert.getEvent().equalsIgnoreCase("entre/sortie")) {
                    if (programedAlert.getRayon() != 0) {
                        rayon = Integer.toString(programedAlert.getRayon());
                    }

                    List<Poi> lastPois = PoiUtils.getLastPoi(previous, rayon, programedAlert.getIdPois());

                    if (!lastPois.isEmpty()) {
                        for (Poi poi : lastPois) {
                            if (!PoiUtils.isInside(current, poi, rayon)) {
                                obs.add("SORTIE DE " + poi.getLabel());
                                checked = true;
                            }
                        }
                    }
                }

                if (programedAlert.getEvent().equalsIgnoreCase("entre")
                        || programedAlert.getEvent().equalsIgnoreCase("entre/sortie")) {

                    if (programedAlert.getRayon() != 0) {
                        rayon = Integer.toString(programedAlert.getRayon());
                    }
                    List<Poi> lastPois = PoiUtils.getLastPoi(previous, rayon, programedAlert.getIdPois());
                    List<Poi> currentPois = PoiUtils.getCurrentPoi(current, rayon, programedAlert.getIdPois());
                    if (!currentPois.isEmpty()) {

                        for (Poi currentPoi : currentPois) {
                            if (!lastPois.isEmpty()) {
                                if (!contains(lastPois, currentPoi)) {
                                    obs.add("ENTREE A " + currentPoi.getLabel());
                                    checked = true;
                                }
                            } else {
                                obs.add("ENTREE A " + currentPoi.getLabel());
                                checked = true;
                            }
                        }
                    }
                }
            }
            return checked;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean contains(List<Poi> pois, Poi poi) {
        for (Poi p : pois) {
            if (p.getId() == poi.getId()) {
                return true;
            }
        }
        return false;
    }
}
