package alert;
import model.Alert;
import model.AlertTmpSpeed;
import model.ProgramedAlert;
import model.Tracking;
import util.AlertUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeedAlert {
    private String observation = "";

    public Alert alert(ProgramedAlert programedAlert, Tracking current) {
        Alert alert = null;
        if (checkSpeedAlert(programedAlert, current)) {
            alert = new Alert(programedAlert.getCategory(),
                    current.getTracking_time(),0,programedAlert.getId(),
                    observation,
                    current.getId_vehicle(),
                    current.getId_societe(),current,programedAlert.getEmails(),current.getSpeed()
                    ,programedAlert.getLimitSpeed());
        }
        System.out.println(alert);
        return alert;
    }

    private boolean checkSpeedAlert(ProgramedAlert programedAlert, Tracking current) {
        boolean checked = false;
        if (AlertUtils.checkDateTime(programedAlert, current)) {
            if (current.getSpeed() > programedAlert.getLimitSpeed()) {
                addSuspect(current);
                deleteOldSuspect();  // keep only the last two record (suspects)
                if (checkSpeedAndTrackingTime(current, programedAlert) /* && checkDistance*/) {
                    checked = true;
                    this.observation = "La vitesse est : " + current.getSpeed();

                   /* if (!isAlertsLessThanFiveMinutes(current)) {
                        checked = true;
                        this.observation = "La vitesse est : " + current.getSpeed();
                    } else {
                        System.out.println("alert sent less than 5 minus ago");
                    }*/
                }
            }
        }
        return checked;
    }


    private boolean checkSpeedAndTrackingTime(Tracking tracking, ProgramedAlert programedAlert) {
        boolean checked = false;
        String query = "Select * from test.alert_tmp_speed where id_vehicle="
                + tracking.getId_vehicle()
                + " and id_societe="
                + tracking.getId_societe()
                + " order by tracking_time desc limit 2";

        List<AlertTmpSpeed> tmpSpeedList = new ArrayList<>();

        try (Connection con = DriverManager
                .getConnection("jdbc:mysql://51.89.43.61:3306/test?useSSL=false", "root", "jpandco.2020");

             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                AlertTmpSpeed alertTmpSpeed = new AlertTmpSpeed();
                alertTmpSpeed.setId_societe(resultSet.getInt("id_societe"));
                alertTmpSpeed.setId_vehicle(resultSet.getInt("id_vehicle"));
                alertTmpSpeed.setTracking_time(resultSet.getTimestamp("tracking_time"));
                alertTmpSpeed.setSpeed(resultSet.getDouble("speed"));
                alertTmpSpeed.setKm(resultSet.getDouble("km"));
                tmpSpeedList.add(alertTmpSpeed);
            }
            if (!tmpSpeedList.isEmpty() && tmpSpeedList.size() >= 2) {
                if ((tmpSpeedList.get(0).getSpeed() > programedAlert.getLimitSpeed()) &&
                        (tmpSpeedList.get(1).getSpeed() > programedAlert.getLimitSpeed())) {
                    if (((tmpSpeedList.get(0).getTracking_time().getTime()/*previous*/
                            - tmpSpeedList.get(1).getTracking_time().getTime()/*before previous*/) < 60000)
                            &&
                            ((tracking.getTracking_time().getTime()
                                    - tmpSpeedList.get(0).getTracking_time().getTime()) < 60000) &&
                            ((tracking.getTracking_time().getTime()
                                    - tmpSpeedList.get(1).getTracking_time().getTime()) < 2 * 60000)
                    ) {
                        checked = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return checked;
    }

    private void addSuspect(Tracking tracking) {
        String insertQuery = "INSERT INTO alert_tmp_speed (id_societe, id_vehicle,speed,km,tracking_time) VALUES(?,?,?,?,?)";
        try (Connection con = DriverManager
                .getConnection("jdbc:mysql://51.89.43.61:3306/test?useSSL=false", "root", "jpandco.2020");
             PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, tracking.getId_societe());
            preparedStatement.setInt(2, tracking.getId_vehicle());
            preparedStatement.setDouble(3, tracking.getSpeed());
            preparedStatement.setDouble(4, tracking.getKm());
            preparedStatement.setTimestamp(5, tracking.getTracking_time());
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    private void deleteOldSuspect() {
        String deleteQuery = "DELETE FROM alert_tmp_speed WHERE id_alert <= (SELECT id_alert FROM ( SELECT id_alert FROM alert_tmp_speed ORDER BY id_alert DESC LIMIT 1 OFFSET 5)foo);";
        try (Connection con = DriverManager
                .getConnection("jdbc:mysql://51.89.43.61:3306/test?useSSL=false", "root", "jpandco.2020");
             PreparedStatement preparedStatement = con.prepareStatement(deleteQuery)) {
            preparedStatement.execute();

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    private boolean isAlertsLessThanFiveMinutes(Tracking tracking) {
        boolean isAlert = false;
        String selectQuery = "Select * from alerts where id_vehicle="
                + tracking.getId_vehicle()
                + " and id_societe="
                + tracking.getId_societe()
                + " order by date_alert desc limit 1";
        try (Connection con = DriverManager
                .getConnection("jdbc:mysql://51.89.43.61:3306/test?useSSL=false", "root", "jpandco.2020");
             PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                if ((tracking.getTracking_time().getTime() - resultSet.getTimestamp("date_alert").getTime()) < 300000) {
                    isAlert = true;
                }
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return isAlert;
    }


    private boolean checkDistance() {
        return false;
    }

    public boolean compareSpeed(Double distance,
                                java.sql.Timestamp currentTrackingTime,
                                java.sql.Timestamp previousTrackingTime, Double avgSpeed, Double constant) {
        try {
            long diff = Math.abs(currentTrackingTime.getTime() - previousTrackingTime.getTime());
            long diffHours = diff / 1000;
            return distance / diffHours >= constant * avgSpeed * 1000 / 3600;
        } catch (Exception e) {
            return false;
        }
    }
}
