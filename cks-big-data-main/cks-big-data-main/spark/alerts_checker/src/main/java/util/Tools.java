package util;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Tools {

    public static StructType getSchema(){
        return DataTypes.createStructType(new StructField[]
                {
                        DataTypes.createStructField("device_type", DataTypes.StringType, true),
                        DataTypes.createStructField("id_societe", DataTypes.IntegerType, true),
                        DataTypes.createStructField("year", DataTypes.IntegerType, true),
                        DataTypes.createStructField("month", DataTypes.IntegerType, true),
                        DataTypes.createStructField("id_vehicle", DataTypes.IntegerType, true),
                        DataTypes.createStructField("imei", DataTypes.StringType, true),
                        DataTypes.createStructField("tracking_time", DataTypes.TimestampType, true),
                        DataTypes.createStructField("longitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("latitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("altitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("acc_status", DataTypes.IntegerType, true),
                        DataTypes.createStructField("km", DataTypes.DoubleType, true),
                        DataTypes.createStructField("angle", DataTypes.IntegerType,true),
                        DataTypes.createStructField("speed", DataTypes.DoubleType, true),
                        DataTypes.createStructField("etatsignal", DataTypes.IntegerType, true),
                        DataTypes.createStructField("internal_battery", DataTypes.DoubleType, true),
                        DataTypes.createStructField("external_power", DataTypes.DoubleType, true),
                        DataTypes.createStructField("current_battery", DataTypes.DoubleType, true),
                        DataTypes.createStructField("id_device", DataTypes.IntegerType, true),
                        DataTypes.createStructField("alv_data", DataTypes.StringType, true),
                        DataTypes.createStructField("message", DataTypes.StringType, true),
                        DataTypes.createStructField("gps_speed", DataTypes.IntegerType, true),
                        DataTypes.createStructField("satellites", DataTypes.IntegerType, true),
                        DataTypes.createStructField("reservoir1", DataTypes.DoubleType, true),
                        DataTypes.createStructField("reservoir2", DataTypes.DoubleType, true),
                        DataTypes.createStructField("temperature1", DataTypes.DoubleType, true),
                        DataTypes.createStructField("temperature2", DataTypes.DoubleType, true),
                        DataTypes.createStructField("input", DataTypes.StringType, true),
                        DataTypes.createStructField("gps_hdop", DataTypes.DoubleType, true),
                        DataTypes.createStructField("gps_pdop", DataTypes.DoubleType, true),
                        DataTypes.createStructField("alert_checked", DataTypes.DoubleType, true),
                        DataTypes.createStructField("distence_corrected", DataTypes.IntegerType, true),
                        DataTypes.createStructField("priority", DataTypes.IntegerType, true),
                        DataTypes.createStructField("tracking_on_event", DataTypes.IntegerType, true),
                        DataTypes.createStructField("event", DataTypes.IntegerType, true),
                        DataTypes.createStructField("panic_button_status", DataTypes.IntegerType, true),
                        DataTypes.createStructField("etat", DataTypes.IntegerType, true),
                        DataTypes.createStructField("movement", DataTypes.IntegerType, true),
                        DataTypes.createStructField("actual_profile", DataTypes.IntegerType, true),
                        DataTypes.createStructField("pcb_temp", DataTypes.DoubleType, true),
                        DataTypes.createStructField("sleep_mode", DataTypes.IntegerType, true),
                        DataTypes.createStructField("operator_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("area_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("cell_id", DataTypes.IntegerType, true),
                        DataTypes.createStructField("gnss_status", DataTypes.IntegerType, true),
                        DataTypes.createStructField("rfid", DataTypes.StringType, true),
                        DataTypes.createStructField("can_speed", DataTypes.DoubleType, true),
                        DataTypes.createStructField("accelerator_pedal_position", DataTypes.DoubleType, true),
                        DataTypes.createStructField("fuel_consumed", DataTypes.DoubleType, true),
                        DataTypes.createStructField("can_fuel_level", DataTypes.DoubleType, true),
                        DataTypes.createStructField("engine_rpm", DataTypes.IntegerType, true),
                        DataTypes.createStructField("total_mileage", DataTypes.DoubleType, true),
                        DataTypes.createStructField("door_status", DataTypes.IntegerType, true),
                        DataTypes.createStructField("fuel_rate", DataTypes.DoubleType, true),
                        DataTypes.createStructField("engine_temp", DataTypes.DoubleType, true),
                        DataTypes.createStructField("engine_oil_level", DataTypes.DoubleType, true),
                        DataTypes.createStructField("ecodrive_type", DataTypes.DoubleType, true),
                        DataTypes.createStructField("crash_detect", DataTypes.DoubleType, true),
                        DataTypes.createStructField("ecodrive_value", DataTypes.DoubleType, true),
                        DataTypes.createStructField("jaming", DataTypes.DoubleType, true),
                        DataTypes.createStructField("ecodrive_duration", DataTypes.DoubleType, true),
                        DataTypes.createStructField("engine_worktime", DataTypes.IntegerType, true),


                });
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
