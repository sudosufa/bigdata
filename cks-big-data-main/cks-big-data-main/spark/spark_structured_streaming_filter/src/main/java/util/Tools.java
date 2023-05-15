package util;

import model.Tracking;
import model.TrackingCas;
import org.apache.spark.api.java.function.MapFunction;
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

    public static Tracking  FixBrokenTrackingData (Tracking tracking_now , Tracking Previous ) {

            if (tracking_now.getCan_fuel_level() != null && tracking_now.getCan_fuel_level() == 0 && Previous.getCan_fuel_level() > 0) {
                tracking_now.setCan_fuel_level(Previous.getCan_fuel_level());
            }
            if (tracking_now.getFuel_consumed() != null && tracking_now.getFuel_consumed() == 0 && Previous.getFuel_consumed() > 0) {
                tracking_now.setFuel_consumed(Previous.getFuel_consumed()) ;
            }
            if (tracking_now.getTotal_mileage() != null && tracking_now.getTotal_mileage()== 0 && Previous.getTotal_mileage() > 0) {
                tracking_now.setTotal_mileage(Previous.getTotal_mileage()) ;
            }
            if (tracking_now.getReservoir1() != null && tracking_now.getReservoir1() == 0 && Previous.getReservoir1()!=null && Previous.getReservoir1() > 0) {
                tracking_now.setReservoir1(Previous.getReservoir1());
            }
        return tracking_now;

    }
    //-----------------------------------------------------------------------------------------------------------------------
    public static Tracking calcul_km(Tracking tracking_now , Tracking Previous ){

        if (tracking_now.getKm()== null || tracking_now.getDevice_type().equals("coban") || tracking_now.getDevice_type().equals("xexun")|| tracking_now.getDevice_type().equals("ruptela")) {
            double distance = distanceBetween(tracking_now.getLatitude(), tracking_now.getLongitude(), Previous.getLatitude(), Previous.getLongitude());
            System.out.println(tracking_now.getDevice_type() + distance);
            tracking_now.setKm(distance);
        }
        return tracking_now;
    }

    public static double distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        LatLng newT = new LatLng(lat1, lon1);
        LatLng previousT = new LatLng(lat2, lon2);
        return SphericalUtil.computeDistanceBetween(newT, previousT);
    }
    //-----------------------------------------------------------------------------------------------------------------------
    public static Tracking Camouflage_using_state(Tracking tracking_now , Tracking Previous ){
        boolean needCorrectGpsData = false;
        if (tracking_now.getKm() < 10) {
            needCorrectGpsData = true;
            if (camStar(5, tracking_now,Previous)) {
                tracking_now.setMessage(CamouflagedMessageService.createMessage(tracking_now, "camStar", 0));
            } else {
                tracking_now.setMessage(CamouflagedMessageService.createMessage(tracking_now, "10_m", 0));
            }
        }
        if (!needCorrectGpsData) {
            if (!Tools.isDistanceLogique(tracking_now.getKm(), tracking_now, Previous)) {
                int compteur = CamouflagedMessageService.compteur(Previous);
                if (compteur < 2) {
                    compteur++;
                    tracking_now.setMessage(CamouflagedMessageService.createMessage(tracking_now, "isDistanceLogique", compteur));
                    needCorrectGpsData = true;
                } else {
                    CamouflagedMessage camMessage = CamouflagedMessageService.parseMessage(Previous);
                    double distanceC = Tools.distanceBetween(tracking_now.getLatitude(), tracking_now.getLongitude(), camMessage.getLatitude(), camMessage.getLongitude());
                    if (!Tools.isDistanceLogique(distanceC, tracking_now, Previous)) {
                        compteur++;
                        tracking_now.setMessage(CamouflagedMessageService.createMessage(tracking_now, "isDistanceLogique", compteur));
                        needCorrectGpsData = true;
                    }
                }
            }
        }
        if (needCorrectGpsData) {
            tracking_now.setGps_speed(tracking_now.getSpeed().intValue());//todo??
            tracking_now.setSpeed(0.0);
            tracking_now.setAltitude(Previous.getAltitude());
            tracking_now.setLatitude(Previous.getLatitude());
            tracking_now.setLongitude(Previous.getLongitude());
            tracking_now.setKm((double) 0);
        }
        if (tracking_now.getTemperature1() != null) {
            if (tracking_now.getTemperature1() == 85) {
                    tracking_now.setTemperature1(Previous.getTemperature1());
            }
        }
                return tracking_now;
    }

    private static boolean speedLessThanOrEqualsAndAccInactive(Tracking tracking, int minAcceptedSpeed) {
        return tracking.getAcc_status() == 0 && tracking.getSpeed() <= minAcceptedSpeed;
    }

    private static boolean camStar(int minAcceptedSpeed, Tracking tracking, Tracking Previous) {
        return speedLessThanOrEqualsAndAccInactive(tracking, minAcceptedSpeed) && speedLessThanOrEqualsAndAccInactive(Previous, minAcceptedSpeed);
    }

    public static boolean isDistanceLogique(double distance, Tracking current, Tracking previous) {
        double dTime = (current.getTracking_time().getTime() - previous.getTracking_time().getTime()) / DateTime.T.s; // valeur par seconde
        if (distance > getSpeedLogique(dTime) + 500) {
            return false;
        } else {
            if (current.getAltitude() != null)
                return isHDistanceLogique(current, previous);
            return true;
        }
    }

    private static double getSpeedLogique(double dTime) {
        double speedLogique = ((double) 150 * 1000) / ((double) 60 * 60); // metre par seconde  150km/h
        return speedLogique * dTime;
    }

    private static boolean isHDistanceLogique(Tracking current, Tracking previous) {
        int compteur = CamouflagedMessageService.compteur(previous);
        double altitude = (compteur == 0) ? previous.getAltitude() : CamouflagedMessageService.parseMessage(previous).getAltitude();
        return (Math.abs(current.getAltitude() - altitude) < 1000);
    }

    //-----------------------------------------------------------------------------------------------------------------------
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
    //-----------------------------------------------------------------------------------------------------------------------
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
    //-----------------------------------------------------------------------------------------------------------------------
    private static final MapFunction<Tracking,TrackingCas> map_to_Cassandra = (tuple)-> {
        TrackingCas cas = new TrackingCas();
        cas.setYear(tuple.getYear());
        cas.setMonth(tuple.getMonth());
        cas.setAccelerator_pedal_position(tuple.getAccelerator_pedal_position());
        cas.setAcc_status(tuple.getAcc_status());
        cas.setActual_profile(tuple.getActual_profile());
        cas.setAlert_checked(tuple.getAlert_checked());
        cas.setAltitude(tuple.getAltitude());
        cas.setAlv_data(tuple.getAlv_data());
        cas.setAngle(tuple.getAngle());
        cas.setArea_code(tuple.getArea_code());
        cas.setCan_fuel_level(tuple.getCan_fuel_level());
        cas.setCell_id(tuple.getCell_id());
        cas.setCurrent_battery(tuple.getCurrent_battery());
        cas.setCan_speed(tuple.getCan_speed());
        cas.setCrash_detect(tuple.getCrash_detect());
        cas.setDistence_corrected(tuple.getDistence_corrected());
        cas.setDoor_status(tuple.getDoor_status());
        cas.setEcodrive_duration(tuple.getEcodrive_duration());
        cas.setEcodrive_type(tuple.getEcodrive_type());
        cas.setEcodrive_value(tuple.getEcodrive_value());
        cas.setEngine_oil_level(tuple.getEngine_oil_level());
        cas.setEngine_temp(tuple.getEngine_temp());
        cas.setEngine_rpm(tuple.getEngine_rpm());
        cas.setEngine_worktime(tuple.getEngine_worktime());
        cas.setEtat(tuple.getEtat());
        cas.setEtatsignal(tuple.getEtatsignal());
        cas.setEvent(tuple.getEvent());
        cas.setExternal_power(tuple.getExternal_power());
        cas.setFuel_consumed(tuple.getFuel_consumed());
        cas.setFuel_rate(tuple.getFuel_rate());
        cas.setGnss_status(tuple.getGnss_status());
        cas.setGps_hdop(tuple.getGps_hdop());
        cas.setGps_pdop(tuple.getGps_pdop());
        cas.setGps_speed(tuple.getGps_speed());
        cas.setId_device(tuple.getId_device());
        cas.setId_vehicle(tuple.getId_vehicle());
        cas.setInput(tuple.getInput());
        cas.setInternal_battery(tuple.getInternal_battery());
        cas.setJaming(tuple.getJaming());
        cas.setKm(tuple.getKm());
        cas.setLatitude(tuple.getLatitude());
        cas.setLongitude(tuple.getLongitude());
        cas.setMessage(tuple.getMessage());
        cas.setMovement(tuple.getMovement());
        cas.setOperator_code(tuple.getOperator_code());
        cas.setPanic_button_status(tuple.getPanic_button_status());
        cas.setPcb_temp(tuple.getPcb_temp());
        cas.setPriority(tuple.getPriority());
        cas.setReservoir1(tuple.getReservoir1());
        cas.setReservoir2(tuple.getReservoir2());
        cas.setRfid(tuple.getRfid());
        cas.setSatellites(tuple.getSatellites());
        cas.setSleep_mode(tuple.getSleep_mode());
        cas.setSpeed(tuple.getSpeed());
        cas.setTemperature1(tuple.getTemperature1());
        cas.setTemperature2(tuple.getTemperature2());
        cas.setTotal_mileage(tuple.getTotal_mileage());
        cas.setTracking_on_event(tuple.getTracking_on_event());
        cas.setTracking_time(tuple.getTracking_time());
        cas.setReception_timespan( new Timestamp(System.currentTimeMillis()));
        return cas;
    };
}
