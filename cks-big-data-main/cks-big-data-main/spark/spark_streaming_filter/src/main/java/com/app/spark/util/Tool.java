package com.app.spark.util;

import com.app.spark.model.Tracking;
import com.app.spark.model.TrackingMysql;
import com.app.spark.util.Tools.LatLng;
import com.app.spark.util.Tools.SphericalUtil;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Tool {

    public static double distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        LatLng newT = new LatLng(lat1, lon1);
        LatLng previousT = new LatLng(lat2, lon2);
        return SphericalUtil.computeDistanceBetween(newT, previousT);
    }
    public static class DbInsert {
        private final ArrayList<String> columns = new ArrayList<>();
        private final ArrayList<String> values = new ArrayList<>();

        public static DbInsert prepareQuery(TrackingMysql tracking) {
            DbInsert q = new DbInsert();
            q.addStr("tracking_time", tracking.getTracking_time().toString());
            q.add("priority", tracking.getPriority());
            q.add("longitude", tracking.getLongitude());
            q.add("latitude", tracking.getLatitude());
            q.add("altitude", tracking.getAltitude());
            q.add("angle", tracking.getAngle());
//            System.out.println("imei:" + tracking.getImei() + " angle:" + tracking.getAngle());
            q.add("satellites", tracking.getSatellites());
            q.add("gps_speed", tracking.getGps_speed());
            q.add("acc_status", tracking.getAcc_status());
            q.add("speed", tracking.getSpeed());
            q.add("tracking_on_event", tracking.getTracking_on_event());
            q.add("event", tracking.getEvent());
            q.add("panic_button_status", tracking.getPanic_button_status());
            q.add("etat", tracking.getEtat());
            q.add("etatSignal", tracking.getEtatSignal());
//            if (!Double.isInfinite(tracking.getReservoir1()))
            q.add("reservoir1", tracking.getReservoir1());
//            if (!Double.isInfinite(tracking.getReservoir2()))
            q.add("reservoir2", tracking.getReservoir2());
            q.add("temperature1", tracking.getTemperature1());
            q.add("temperature2", tracking.getTemperature2());
            q.add("km", tracking.getKm());
            q.add("movement", tracking.getMovement());
            q.add("input", tracking.getInput());
            q.add("id_device", tracking.getId_device());
            q.add("id_vehicule", tracking.getId_vehicule());
            q.add("actual_profile", tracking.getActual_profile());
            q.add("external_power", tracking.getExternal_power());
            q.add("internal_battery", tracking.getInternal_battery());
            q.add("current_battery", tracking.getCurrent_battery());
            q.add("pcb_temp", tracking.getPcb_temp());
            q.add("gps_pdop", tracking.getGps_pdop());
            q.add("gps_hdop", tracking.getGps_hdop());
            q.add("sleep_mode", tracking.getSleep_mode());
            q.add("operator_code", tracking.getOperator_code());
            q.add("area_code", tracking.getArea_code());
            q.add("cell_id", tracking.getCell_id());
            q.add("gnss_status", tracking.getGnss_status());
            q.add("rfid", tracking.getRfid());
            //
            q.add("can_speed", tracking.getCan_speed());
            q.add("accelerator_pedal_position", tracking.getAccelerator_pedal_position());
            q.add("can_fuel_level", tracking.getCan_fuel_level());
            q.add("fuel_consumed", tracking.getFuel_consumed());
            q.add("fuel_rate", tracking.getFuel_rate());
            q.add("engine_rpm", tracking.getEngine_rpm());
            q.add("total_mileage", tracking.getTotal_mileage());
            q.add("door_status", tracking.getDoor_status());
            q.add("engine_temp", tracking.getEngine_temp());
            q.add("engine_oil_level", tracking.getEngine_oil_level());
            q.add("engine_worktime", tracking.getEngine_worktime());
            q.add("ecodrive_type", tracking.getEcodrive_type());
            q.add("ecodrive_value", tracking.getEcodrive_value());
            q.add("ecodrive_duration", tracking.getEcodrive_duration());
            q.add("crash_detect", tracking.getCrash_detect());
            q.add("jaming", tracking.getJaming());
            //
            q.add("alert_checked", tracking.getAlert_checked());
            q.add("distence_corrected", tracking.getDistence_corrected());
            //
            q.addStr("alv_data", tracking.getAlv_data());
            q.addStr("message", tracking.getMessage());
            return q;
        }


        private DbInsert addColumn(String column) {
            columns.add("`" + column + "`");
            return this;
        }

        private DbInsert addValue(String value, boolean isString) {

            if (!isString)
                values.add(value);
            else
                values.add("'" + value + "'");
            return this;
        }

        private DbInsert add(String column, String value) {
            return addColumn(column).addValue(value, false);
        }

        private void addStr(String column, String value) {
            addColumn(column).addValue(value, true);
        }


        private void add(String column, Boolean value) {
            add(column, value ? 1 : 0);
        }

        private void add(String column, Integer value) {
            add(column, String.valueOf(value));
        }

        private void add(String column, Double value) {
            add(column, String.valueOf(value));
        }

        public DbInsert add(String column, long value) {
            return add(column, String.valueOf(value));
        }

        public String getSQL() {
            return "INSERT INTO `{dbtTableName}`( " + String.join(",", columns) + " )  VALUES ( " + String.join(",", values) + " )";
        }


    }

    public static class DateTime {

        public static Date stringToDate(Timestamp datetime, String format) {
            try {
                return (new SimpleDateFormat(format)).parse(String.valueOf(datetime));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static Date stringToDate(Timestamp datetime) {
            return stringToDate(datetime, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        }

        private static LocalDateTime stringToLocalDate(Timestamp datetime, String format) {
            Date date = stringToDate(datetime, format);
            if (date != null)
                return dateToLocalDateTime(date);
            return null;
        }

        public static LocalDateTime stringToLocalDate(Timestamp datetime) {
            return stringToLocalDate(datetime, "yyyy-MM-dd HH:mm:ss");
        }

        public static LocalDateTime dateToLocalDateTime(Date datetime) {
            return datetime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        public static String getTimestampFromDate(Timestamp date) {
            Date tempDate = stringToDate(date, "yyMMddHHmmss");
            assert tempDate != null;
            return new Timestamp(tempDate.getTime()).toString();

        }


        public static String dateNow(String format) {

            if (format == null)
                format = "yyyy-MM-dd HH:mm:ss";
            return DateTimeFormatter.ofPattern(format, Locale.ENGLISH).format(LocalDateTime.now());
        }

        public class T {
            public static final int ms = 1;
            public static final int s = 1000;
            public static final int m = 60 * s;
            public static final int h = 60 * m;
            public static final int d = 24 * h;
        }


    }
}
