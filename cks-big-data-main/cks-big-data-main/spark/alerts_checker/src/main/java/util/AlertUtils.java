package util;

import model.ProgramedAlert;
import model.Tracking;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlertUtils {

    private final List<ProgramedAlert> programedAlerts = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(AlertUtils.class);

    public List<ProgramedAlert> listProgramedAlerts(int id_societe, int id_vehicle) {
        String sql = "SELECT * FROM alerte_prog alt,alerte_prog_vehicules t" +
                " where alt.id =t.id_alerte_prog  and  alt.id_societe=" + id_societe +
                " and  t.id_vehicule=" + id_vehicle;
        try (Connection con = DriverManager
                .getConnection("jdbc:mysql://51.89.43.61:3306/jptrack?useSSL=false",
                        "root",
                        "jpandco.2020");
             PreparedStatement preparedStatement = con.prepareStatement(sql);) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ProgramedAlert programedAlert = new ProgramedAlert();
                programedAlert.setId(resultSet.getInt("id"));
                programedAlert.setTitle(resultSet.getString("titre"));
                programedAlert.setCategory(resultSet.getString("categorie"));
                programedAlert.setStartHour(resultSet.getString("heureDebut"));
                programedAlert.setEndHour(resultSet.getString("heureFin"));
                programedAlert.setStartDate(resultSet.getString("dateDebut"));
                programedAlert.setEndDate(resultSet.getString("dateFin"));
                programedAlert.setLimitSpeed(resultSet.getInt("limite_speed"));
                programedAlert.setEmails(resultSet.getString("emails"));
                programedAlert.setEnergy(resultSet.getBoolean("energie"));
                programedAlert.setBattery(resultSet.getBoolean("battrie"));
                programedAlert.setEvent(resultSet.getString("event"));
                programedAlert.setIdZones(resultSet.getString("idZones"));
                programedAlert.setIdPois(resultSet.getString("idPois"));
                programedAlert.setRayon(resultSet.getInt("rayon"));
                programedAlert.setStopped(resultSet.getBoolean("enArret"));
                programedAlert.setIdCompany(resultSet.getInt("id_societe"));
                programedAlert.setApprov(resultSet.getInt("approv"));
                programedAlert.setDays(resultSet.getString("days"));
                programedAlerts.add(programedAlert);
            }
        } catch (SQLException e) {
            logger.error("from listProgramedAlerts: " + e.getMessage());
        }
        return programedAlerts;
    }

    public static boolean checkDateTime(ProgramedAlert programedAlert, Tracking tracking) {

        try {
            boolean checked = true;
            if (programedAlert.getStartHour() != null && programedAlert.getEndHour() != null) {

                java.util.Date tracking_time = tracking.getTracking_time();
                java.util.Date debut = convertFromString(programedAlert.getStartHour(), "HH:mm");
                java.util.Date fin = convertFromString(programedAlert.getEndHour(), "HH:mm");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                java.util.Date time = convertFromString(timeFormat.format(tracking_time), "HH:mm");
                checked = isBetween(time, debut, fin);
            }

            if (programedAlert.getStartDate() != null && programedAlert.getEndDate() != null) {

                java.util.Date tracking_time = tracking.getTracking_time();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = convertFromString(dateFormat.format(tracking_time), "yyyy-MM-dd");
                java.util.Date debut = convertFromString(programedAlert.getStartDate(), "yyy-MM-dd");
                java.util.Date fin = convertFromString(programedAlert.getEndDate(), "yyy-MM-dd");
                checked = checked && isBetween(date, debut, fin);
            }

            if (programedAlert.getDays() != null) {
                java.util.Date tracking_time = tracking.getTracking_time();
                int dayN = tracking_time.getDay();
                int[] days = convertString(programedAlert.getDays(), ";");
                int i = 0;
                boolean In = false;
                do {
                    if (days[i] == dayN) {
                        In = true;
                    }
                    i++;
                } while (!In && days.length > i);

                checked = checked && In;
            }
            return checked;
        } catch (Exception e) {
            return false;
        }
    }

    public static int[] convertString(String strings, String split) {
        String[] strArray = strings.split(split);
        int[] intArray = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            intArray[i] = Integer.parseInt(strArray[i]);
        }
        return intArray;
    }

    public static java.util.Date convertFromString(String datetime, String format) {
        try {
            SimpleDateFormat dt = new SimpleDateFormat(format);
            java.util.Date tempDate = dt.parse(datetime);
            return tempDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return null;
        }
    }

    public static boolean isBetween(java.util.Date date, java.util.Date debut, Date fin) {
        try {
            return !date.before(debut) && !date.after(fin);
        } catch (Exception e) {
            return false;
        }
    }
}
