package com.app.spark.model;



import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

public class Tracking implements Serializable {

    private  String  device_type ;
    private int year;
    private int month ;
    //--------------------------------------
    private int data_length ;
    private int codec_id;
    private int number_of_data;

    public int getData_length() {
        return data_length;
    }

    public void setData_length(int data_length) {
        this.data_length = data_length;
    }

    public int getCodec_id() {
        return codec_id;
    }

    public void setCodec_id(int codec_id) {
        this.codec_id = codec_id;
    }

    public int getNumber_of_data() {
        return number_of_data;
    }

    public void setNumber_of_data(int number_of_data) {
        this.number_of_data = number_of_data;
    }

    //------------------
    private Integer id_vehicule;
    private String imei;
    private Timestamp tracking_time;
    private Double longitude;
    private Double latitude;
    private Double altitude;
    private int acc_status ;
    private Double alert_checked;
    private int distence_corrected ;
    private int id_device;
    public Map<String, String> avlData ;
    private Integer id_tracking;
    private Double angle;
    private String message;
    private Double speed;
    private Integer gps_speed;
    private Integer etatSignal;
    private Integer satellites;
    private Double reservoir1;
    private Double reservoir2;
    private Double temperature;
    private Double temperature2;
    private Double km;
    private String input;
    private Double gps_hdop;
    private Double gps_pdop;
    private Double internal_battery;
    private Double external_power;
    private Double current_battery;
    private Integer priority;
    private Integer tracking_on_event;
    private Integer event;
    private Integer panic_button_status;
    private Integer etat;
    private Integer movement;
    private Integer actual_profile;
    private Double pcb_temp;
    private Integer sleep_mode;
    private Integer operator_code;
    private Integer area_code;
    private Integer cell_id;
    private Integer gnss_status;
    private String rfid;
    public Timestamp reception_timespan = new Timestamp(System.currentTimeMillis()) ;
    //--------------------LVCAN_DATA------------------------------
    private double lVCAN_VEHICLE_SPEED;
    private double lVCAN_ACCELERATOR_PEDAL_POSITION;
    private double lVCAN_FUEL_CONSUMED;
    private double lVCAN_FUEL_LEVEL;
    private int    lVCAN_ENGINE_RPM;
    private double lVCAN_TOTAL_MILEAGE;
    private int    lVCAN_DOOR_STATUS;
    private double lVCAN_FUEL_RATE;
    private double lVCAN_ENGINE_TEMPERATURE;
    private double lVCAN_ENGINE_OIL_LEVEL;
    private double lVCAN_GREEN_DRIVING_TYPE;
    private double lVCAN_CRASH_DETECTION;
    private double lVCAN_GREEN_DRIVING_VALUE;
    private double lVCAN_JAMMING;
    private double lVCAN_GREEN_DRIVING_EVENT_DURATION;
    private int    lVCAN_ENGINE_WORKTIME;
    //--------------------------------------------------

    public Tracking(){
    }


    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    public void setlVCAN_JAMMING(double lVCAN_JAMMING) {
        this.lVCAN_JAMMING = lVCAN_JAMMING;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getId_device() {
        return id_device;
    }
    public void setId_device(int id_device) {
        this.id_device = id_device;
    }

    public Integer getId_vehicule() {
        return id_vehicule;
    }
    public void setId_vehicule(Integer id_vehicule) {
        this.id_vehicule = id_vehicule;
    }

    public Double getAlert_checked() {
        return alert_checked;
    }

    public void setAlert_checked(Double alert_checked) {
        this.alert_checked = alert_checked;
    }

    public int getDistence_corrected() {
        return distence_corrected;
    }

    public void setDistence_corrected(int distence_corrected) {
        this.distence_corrected = distence_corrected;
    }

    public Map<String, String> getAvlData() {
        return avlData;
    }

    public void setAvlData(Map <String,String> avlData) {
        this.avlData = avlData  ;
    }


    public Integer getId_tracking() {
        return id_tracking;
    }
    public void setId_tracking(Integer id_tracking) {
        this.id_tracking = id_tracking;
    }

    public int getMonth() {
        return month;
    }
    public void setAcc_status(int acc_status) {
        this.acc_status = acc_status;
    }
    public String getImei() {
        return imei;
    }
    public void setImei(String imei) {
        this.imei = imei;
    }

    public Timestamp getTracking_time() {
        return tracking_time;
    }
    public void setTracking_time(Timestamp tracking_time) {
        this.tracking_time = tracking_time;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getAltitude() {
        return altitude;
    }
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getAngle() {
        return angle;
    }
    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Double getSpeed() {
        return speed;
    }
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getGps_speed() {
        return gps_speed;
    }
    public void setGps_speed(Integer gps_speed) {
        this.gps_speed = gps_speed;
    }

    public Integer getEtatSignal() {
        return etatSignal;
    }
    public void setEtatSignal(Integer etatSignal) {
        this.etatSignal = etatSignal;
    }

    public Integer getSatellites() {
        return satellites;
    }
    public void setSatellites(Integer satellites) {
        this.satellites = satellites;
    }

    public Double getReservoir1() {
        return reservoir1;
    }
    public void setReservoir1(Double reservoir1) {
        this.reservoir1 = reservoir1;
    }

    public Double getReservoir2() {
        return reservoir2;
    }
    public void setReservoir2(Double reservoir2) {
        this.reservoir2 = reservoir2;
    }

    public Double getTemperature() {
        return temperature;
    }
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTemperature2() {
        return temperature2;
    }
    public void setTemperature2(Double temperature2) {
        this.temperature2 = temperature2;
    }

    public Double getKm() {
        return km;
    }
    public void setKm(Double km) {
        this.km = km;
    }

    public String getInput() {
        return input;
    }
    public void setInput(String input) {
        this.input = input;
    }

    public Double getGps_hdop() {
        return gps_hdop;
    }
    public void setGps_hdop(Double gps_hdop) {
        this.gps_hdop = gps_hdop;
    }

    public Double getGps_pdop() {
        return gps_pdop;
    }
    public void setGps_pdop(Double gps_pdop) {
        this.gps_pdop = gps_pdop;
    }

    public Double getInternal_battery() {
        return internal_battery;
    }
    public void setInternal_battery(Double internal_battery) {
        this.internal_battery = internal_battery;
    }

    public Double getExternal_power() {
        return external_power;
    }
    public void setExternal_power(Double external_power) {
        this.external_power = external_power;
    }

    public Double getCurrent_battery() {
        return current_battery;
    }
    public void setCurrent_battery(Double current_battery) {
        this.current_battery = current_battery;
    }

    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getTracking_on_event() {
        return tracking_on_event;
    }
    public void setTracking_on_event(Integer tracking_on_event) {
        this.tracking_on_event = tracking_on_event;
    }

    public Integer getEvent() {
        return event;
    }
    public void setEvent(Integer event) {
        this.event = event;
    }

    public Integer getPanic_button_status() {
        return panic_button_status;
    }
    public void setPanic_button_status(Integer panic_button_status) {
        this.panic_button_status = panic_button_status;
    }

    public Integer getEtat() {
        return etat;
    }
    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public Integer getMovement() {
        return movement;
    }
    public void setMovement(Integer movement) {
        this.movement = movement;
    }

    public Integer getActual_profile() {
        return actual_profile;
    }
    public void setActual_profile(Integer actual_profile) {
        this.actual_profile = actual_profile;
    }

    public Double getPcb_temp() {
        return pcb_temp;
    }
    public void setPcb_temp(Double pcb_temp) {
        this.pcb_temp = pcb_temp;
    }

    public Integer getSleep_mode() {
        return sleep_mode;
    }
    public void setSleep_mode(Integer sleep_mode) {
        this.sleep_mode = sleep_mode;
    }

    public Integer getOperator_code() {
        return operator_code;
    }
    public void setOperator_code(Integer operator_code) {
        this.operator_code = operator_code;
    }

    public Integer getArea_code() {
        return area_code;
    }
    public void setArea_code(Integer area_code) {
        this.area_code = area_code;
    }

    public Integer getCell_id() {
        return cell_id;
    }
    public void setCell_id(Integer cell_id) {
        this.cell_id = cell_id;
    }

    public Integer getGnss_status() {
        return gnss_status;
    }
    public void setGnss_status(Integer gnss_status) {
        this.gnss_status = gnss_status;
    }

    public String getRfid() {
        return rfid;
    }
    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public double getlVCAN_VEHICLE_SPEED() {
        return lVCAN_VEHICLE_SPEED;
    }
    public void setlVCAN_VEHICLE_SPEED(double lVCAN_VEHICLE_SPEED) {
        this.lVCAN_VEHICLE_SPEED = lVCAN_VEHICLE_SPEED;
    }

    public double getlVCAN_ACCELERATOR_PEDAL_POSITION() {
        return lVCAN_ACCELERATOR_PEDAL_POSITION;
    }
    public void setlVCAN_ACCELERATOR_PEDAL_POSITION(double lVCAN_ACCELERATOR_PEDAL_POSITION) {
        this.lVCAN_ACCELERATOR_PEDAL_POSITION = lVCAN_ACCELERATOR_PEDAL_POSITION;
    }

    public double getlVCAN_FUEL_CONSUMED() {
        return lVCAN_FUEL_CONSUMED;
    }
    public void setlVCAN_FUEL_CONSUMED(double lVCAN_FUEL_CONSUMED) {
        this.lVCAN_FUEL_CONSUMED = lVCAN_FUEL_CONSUMED;
    }

    public double getlVCAN_FUEL_LEVEL() {
        return lVCAN_FUEL_LEVEL;
    }
    public void setlVCAN_FUEL_LEVEL(double lVCAN_FUEL_LEVEL) {
        this.lVCAN_FUEL_LEVEL = lVCAN_FUEL_LEVEL;
    }

    public int getlVCAN_ENGINE_RPM() {
        return lVCAN_ENGINE_RPM;
    }
    public void setlVCAN_ENGINE_RPM(int lVCAN_ENGINE_RPM) {
        this.lVCAN_ENGINE_RPM = lVCAN_ENGINE_RPM;
    }

    public double getlVCAN_TOTAL_MILEAGE() {
        return lVCAN_TOTAL_MILEAGE;
    }
    public void setlVCAN_TOTAL_MILEAGE(double lVCAN_TOTAL_MILEAGE) {
        this.lVCAN_TOTAL_MILEAGE = lVCAN_TOTAL_MILEAGE;
    }

    public int getlVCAN_DOOR_STATUS() {
        return lVCAN_DOOR_STATUS;
    }
    public void setlVCAN_DOOR_STATUS(int lVCAN_DOOR_STATUS) {
        this.lVCAN_DOOR_STATUS = lVCAN_DOOR_STATUS;
    }

    public double getlVCAN_FUEL_RATE() {
        return lVCAN_FUEL_RATE;
    }
    public void setlVCAN_FUEL_RATE(double lVCAN_FUEL_RATE) {
        this.lVCAN_FUEL_RATE = lVCAN_FUEL_RATE;
    }

    public double getlVCAN_ENGINE_TEMPERATURE() {
        return lVCAN_ENGINE_TEMPERATURE;
    }
    public void setlVCAN_ENGINE_TEMPERATURE(double lVCAN_ENGINE_TEMPERATURE) {
        this.lVCAN_ENGINE_TEMPERATURE = lVCAN_ENGINE_TEMPERATURE; }

    public int getAcc_status() {
        return acc_status;
    }

    public double getlVCAN_ENGINE_OIL_LEVEL() {
        return lVCAN_ENGINE_OIL_LEVEL;
    }
    public void setlVCAN_ENGINE_OIL_LEVEL(double lVCAN_ENGINE_OIL_LEVEL) {
        this.lVCAN_ENGINE_OIL_LEVEL = lVCAN_ENGINE_OIL_LEVEL;
    }

    public double getlVCAN_GREEN_DRIVING_TYPE() {
        return lVCAN_GREEN_DRIVING_TYPE;
    }
    public void setlVCAN_GREEN_DRIVING_TYPE(double lVCAN_GREEN_DRIVING_TYPE) {
        this.lVCAN_GREEN_DRIVING_TYPE = lVCAN_GREEN_DRIVING_TYPE;
    }

    public double getlVCAN_CRASH_DETECTION() {
        return lVCAN_CRASH_DETECTION;
    }
    public void setlVCAN_CRASH_DETECTION(double lVCAN_CRASH_DETECTION) {
        this.lVCAN_CRASH_DETECTION = lVCAN_CRASH_DETECTION;
    }

    public double getlVCAN_GREEN_DRIVING_VALUE() {
        return lVCAN_GREEN_DRIVING_VALUE;
    }
    public void setlVCAN_GREEN_DRIVING_VALUE(double lVCAN_GREEN_DRIVING_VALUE) {
        this.lVCAN_GREEN_DRIVING_VALUE = lVCAN_GREEN_DRIVING_VALUE;
    }

    public double getlVCAN_JAMMING() {
        return lVCAN_JAMMING;
    }


    public double getlVCAN_GREEN_DRIVING_EVENT_DURATION() {
        return lVCAN_GREEN_DRIVING_EVENT_DURATION;
    }
    public void setlVCAN_GREEN_DRIVING_EVENT_DURATION(double lVCAN_GREEN_DRIVING_EVENT_DURATION) {
        this.lVCAN_GREEN_DRIVING_EVENT_DURATION = lVCAN_GREEN_DRIVING_EVENT_DURATION; }

    public int getlVCAN_ENGINE_WORKTIME() {
        return lVCAN_ENGINE_WORKTIME;
    }
    public void setlVCAN_ENGINE_WORKTIME(int lVCAN_ENGINE_WORKTIME) {
        this.lVCAN_ENGINE_WORKTIME = lVCAN_ENGINE_WORKTIME; }

    public Timestamp getReception_timespan() {
        return reception_timespan;
    }

    public void setReception_timespan(Timestamp reception_timespan) {
        this.reception_timespan = reception_timespan;
    }

    @Override
    public String toString() {
        return "Tracking{" +
                "device_type='" + device_type + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", id_vehicule=" + id_vehicule +
                ", imei='" + imei + '\'' +
                ", tracking_time=" + tracking_time +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                ", acc_status=" + acc_status +
                ", alert_checked=" + alert_checked +
                ", distence_corrected=" + distence_corrected +
                ", id_device=" + id_device +
                ", avlData=" + avlData +
                ", id_tracking=" + id_tracking +
                ", angle=" + angle +
                ", message='" + message + '\'' +
                ", speed=" + speed +
                ", gps_speed=" + gps_speed +
                ", etatSignal=" + etatSignal +
                ", satellites=" + satellites +
                ", reservoir1=" + reservoir1 +
                ", reservoir2=" + reservoir2 +
                ", temperature=" + temperature +
                ", temperature2=" + temperature2 +
                ", km=" + km +
                ", input='" + input + '\'' +
                ", gps_hdop=" + gps_hdop +
                ", gps_pdop=" + gps_pdop +
                ", internal_battery=" + internal_battery +
                ", external_power=" + external_power +
                ", current_battery=" + current_battery +
                ", priority=" + priority +
                ", tracking_on_event=" + tracking_on_event +
                ", event=" + event +
                ", panic_button_status=" + panic_button_status +
                ", etat=" + etat +
                ", movement=" + movement +
                ", actual_profile=" + actual_profile +
                ", pcb_temp=" + pcb_temp +
                ", sleep_mode=" + sleep_mode +
                ", operator_code=" + operator_code +
                ", area_code=" + area_code +
                ", cell_id=" + cell_id +
                ", gnss_status=" + gnss_status +
                ", rfid='" + rfid + '\'' +
                ", reception_timespan=" + reception_timespan +
                ", lVCAN_VEHICLE_SPEED=" + lVCAN_VEHICLE_SPEED +
                ", lVCAN_ACCELERATOR_PEDAL_POSITION=" + lVCAN_ACCELERATOR_PEDAL_POSITION +
                ", lVCAN_FUEL_CONSUMED=" + lVCAN_FUEL_CONSUMED +
                ", lVCAN_FUEL_LEVEL=" + lVCAN_FUEL_LEVEL +
                ", lVCAN_ENGINE_RPM=" + lVCAN_ENGINE_RPM +
                ", lVCAN_TOTAL_MILEAGE=" + lVCAN_TOTAL_MILEAGE +
                ", lVCAN_DOOR_STATUS=" + lVCAN_DOOR_STATUS +
                ", lVCAN_FUEL_RATE=" + lVCAN_FUEL_RATE +
                ", lVCAN_ENGINE_TEMPERATURE=" + lVCAN_ENGINE_TEMPERATURE +
                ", lVCAN_ENGINE_OIL_LEVEL=" + lVCAN_ENGINE_OIL_LEVEL +
                ", lVCAN_GREEN_DRIVING_TYPE=" + lVCAN_GREEN_DRIVING_TYPE +
                ", lVCAN_CRASH_DETECTION=" + lVCAN_CRASH_DETECTION +
                ", lVCAN_GREEN_DRIVING_VALUE=" + lVCAN_GREEN_DRIVING_VALUE +
                ", lVCAN_JAMMING=" + lVCAN_JAMMING +
                ", lVCAN_GREEN_DRIVING_EVENT_DURATION=" + lVCAN_GREEN_DRIVING_EVENT_DURATION +
                ", lVCAN_ENGINE_WORKTIME=" + lVCAN_ENGINE_WORKTIME +
                '}';
    }
}
