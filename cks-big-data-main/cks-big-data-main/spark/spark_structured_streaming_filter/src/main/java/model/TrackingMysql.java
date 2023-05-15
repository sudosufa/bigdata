package model;

import java.io.Serializable;

public class TrackingMysql implements Serializable {
    private Integer year;
    private Integer month ;
    private String tracking_time;
    private Integer priority;
    private Double longitude;
    private Double latitude;
    private Double altitude;
    private Double angle;
    private Integer satellites;
    private Integer gps_speed;
    private Integer acc_status ;
    private Double speed;
    private Integer tracking_on_event;
    private Integer event;
    private Integer panic_button_status;
    private Integer etat;
    private Integer etatSignal;
    private Double reservoir1;
    private Double reservoir2;
    private Double temperature1;
    private Double temperature2;
    private Double km;
    private Integer movement;
    private String input;
    private Integer id_device;
    private Integer id_vehicule;
    private Integer actual_profile;
    private Double external_power;
    private Double internal_battery;
    private Double current_battery;
    private Double pcb_temp;
    private Double gps_hdop;
    private Double gps_pdop;
    private Integer sleep_mode;
    private Integer operator_code;
    private Integer area_code;
    private Integer cell_id;
    private Integer gnss_status;
    private String rfid;
    private String message;
    private Double can_speed ;
    private Double accelerator_pedal_position;
    private Double fuel_consumed;
    private Double can_fuel_level;
    private Double engine_rpm;
    private Double total_mileage;
    private Integer door_status;
    private Double fuel_rate;
    private Double engine_temp;
    private Double engine_oil_level;
    private Double ecodrive_type;
    private Double ecodrive_value;
    private Double ecodrive_duration;
    private Double crash_detect;
    private Double jaming;
    private Integer engine_worktime;
    private Double alert_checked;
    private Integer distence_corrected ;
    private String alv_data;

    public TrackingMysql(){
    }

    public String getTracking_time() {
        return tracking_time;
    }

    public void setTracking_time(String tracking_time) {
        this.tracking_time = tracking_time;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    public Integer getSatellites() {
        return satellites;
    }

    public void setSatellites(Integer satellites) {
        this.satellites = satellites;
    }

    public Integer getGps_speed() {
        return gps_speed;
    }

    public void setGps_speed(Integer gps_speed) {
        this.gps_speed = gps_speed;
    }

    public Integer getAcc_status() {
        return acc_status;
    }

    public void setAcc_status(Integer acc_status) {
        this.acc_status = acc_status;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
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

    public Integer getEtatSignal() {
        return etatSignal;
    }

    public void setEtatSignal(Integer etatSignal) {
        this.etatSignal = etatSignal;
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

    public Double getTemperature1() {
        return temperature1;
    }

    public void setTemperature1(Double temperature1) {
        this.temperature1 = temperature1;
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

    public Integer getMovement() {
        return movement;
    }

    public void setMovement(Integer movement) {
        this.movement = movement;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Integer getId_device() {
        return id_device;
    }

    public void setId_device(Integer id_device) {
        this.id_device = id_device;
    }

    public Integer getId_vehicule() {
        return id_vehicule;
    }

    public void setId_vehicule(Integer id_vehicule) {
        this.id_vehicule = id_vehicule;
    }

    public Integer getActual_profile() {
        return actual_profile;
    }

    public void setActual_profile(Integer actual_profile) {
        this.actual_profile = actual_profile;
    }

    public Double getExternal_power() {
        return external_power;
    }

    public void setExternal_power(Double external_power) {
        this.external_power = external_power;
    }

    public Double getInternal_battery() {
        return internal_battery;
    }

    public void setInternal_battery(Double internal_battery) {
        this.internal_battery = internal_battery;
    }

    public Double getCurrent_battery() {
        return current_battery;
    }

    public void setCurrent_battery(Double current_battery) {
        this.current_battery = current_battery;
    }

    public Double getPcb_temp() {
        return pcb_temp;
    }

    public void setPcb_temp(Double pcb_temp) {
        this.pcb_temp = pcb_temp;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getCan_speed() {
        return can_speed;
    }

    public void setCan_speed(Double can_speed) {
        this.can_speed = can_speed;
    }

    public Double getAccelerator_pedal_position() {
        return accelerator_pedal_position;
    }

    public void setAccelerator_pedal_position(Double accelerator_pedal_position) {
        this.accelerator_pedal_position = accelerator_pedal_position;
    }

    public Double getFuel_consumed() {
        return fuel_consumed;
    }

    public void setFuel_consumed(Double fuel_consumed) {
        this.fuel_consumed = fuel_consumed;
    }

    public Double getCan_fuel_level() {
        return can_fuel_level;
    }

    public void setCan_fuel_level(Double can_fuel_level) {
        this.can_fuel_level = can_fuel_level;
    }

    public Double getEngine_rpm() {
        return engine_rpm;
    }

    public void setEngine_rpm(Double engine_rpm) {
        this.engine_rpm = engine_rpm;
    }

    public Double getTotal_mileage() {
        return total_mileage;
    }

    public void setTotal_mileage(Double total_mileage) {
        this.total_mileage = total_mileage;
    }

    public Integer getDoor_status() {
        return door_status;
    }

    public void setDoor_status(Integer door_status) {
        this.door_status = door_status;
    }

    public Double getFuel_rate() {
        return fuel_rate;
    }

    public void setFuel_rate(Double fuel_rate) {
        this.fuel_rate = fuel_rate;
    }

    public Double getEngine_temp() {
        return engine_temp;
    }

    public void setEngine_temp(Double engine_temp) {
        this.engine_temp = engine_temp;
    }

    public Double getEngine_oil_level() {
        return engine_oil_level;
    }

    public void setEngine_oil_level(Double engine_oil_level) {
        this.engine_oil_level = engine_oil_level;
    }

    public Double getEcodrive_type() {
        return ecodrive_type;
    }

    public void setEcodrive_type(Double ecodrive_type) {
        this.ecodrive_type = ecodrive_type;
    }

    public Double getEcodrive_value() {
        return ecodrive_value;
    }

    public void setEcodrive_value(Double ecodrive_value) {
        this.ecodrive_value = ecodrive_value;
    }

    public Double getEcodrive_duration() {
        return ecodrive_duration;
    }

    public void setEcodrive_duration(Double ecodrive_duration) {
        this.ecodrive_duration = ecodrive_duration;
    }

    public Double getCrash_detect() {
        return crash_detect;
    }

    public void setCrash_detect(Double crash_detect) {
        this.crash_detect = crash_detect;
    }

    public Double getJaming() {
        return jaming;
    }

    public void setJaming(Double jaming) {
        this.jaming = jaming;
    }

    public Integer getEngine_worktime() {
        return engine_worktime;
    }

    public void setEngine_worktime(Integer engine_worktime) {
        this.engine_worktime = engine_worktime;
    }

    public Double getAlert_checked() {
        return alert_checked;
    }

    public void setAlert_checked(Double alert_checked) {
        this.alert_checked = alert_checked;
    }

    public Integer getDistence_corrected() {
        return distence_corrected;
    }

    public void setDistence_corrected(Integer distence_corrected) {
        this.distence_corrected = distence_corrected;
    }

    public String getAlv_data() {
        return alv_data;
    }

    public void setAlv_data(String alv_data) {
        this.alv_data = alv_data;
    }

    @Override
    public String toString() {
        return "TrackingMysql{" +
                "tracking_time=" + tracking_time +
                ", priority=" + priority +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                ", angle=" + angle +
                ", satellites=" + satellites +
                ", gps_speed=" + gps_speed +
                ", acc_status=" + acc_status +
                ", speed=" + speed +
                ", tracking_on_event=" + tracking_on_event +
                ", event=" + event +
                ", panic_button_status=" + panic_button_status +
                ", etat=" + etat +
                ", etatSignal=" + etatSignal +
                ", reservoir1=" + reservoir1 +
                ", reservoir2=" + reservoir2 +
                ", temperature1=" + temperature1 +
                ", temperature2=" + temperature2 +
                ", km=" + km +
                ", movement=" + movement +
                ", input='" + input + '\'' +
                ", id_device=" + id_device +
                ", id_vehicule=" + id_vehicule +
                ", actual_profile=" + actual_profile +
                ", external_power=" + external_power +
                ", internal_battery=" + internal_battery +
                ", current_battery=" + current_battery +
                ", pcb_temp=" + pcb_temp +
                ", gps_hdop=" + gps_hdop +
                ", gps_pdop=" + gps_pdop +
                ", sleep_mode=" + sleep_mode +
                ", operator_code=" + operator_code +
                ", area_code=" + area_code +
                ", cell_id=" + cell_id +
                ", gnss_status=" + gnss_status +
                ", rfid='" + rfid + '\'' +
                ", message='" + message + '\'' +
                ", can_speed=" + can_speed +
                ", accelerator_pedal_position=" + accelerator_pedal_position +
                ", fuel_consumed=" + fuel_consumed +
                ", can_fuel_level=" + can_fuel_level +
                ", engine_rpm=" + engine_rpm +
                ", total_mileage=" + total_mileage +
                ", door_status=" + door_status +
                ", fuel_rate=" + fuel_rate +
                ", engine_temp=" + engine_temp +
                ", engine_oil_level=" + engine_oil_level +
                ", ecodrive_type=" + ecodrive_type +
                ", ecodrive_value=" + ecodrive_value +
                ", ecodrive_duration=" + ecodrive_duration +
                ", crash_detect=" + crash_detect +
                ", jaming=" + jaming +
                ", engine_worktime=" + engine_worktime +
                ", alert_checked=" + alert_checked +
                ", distence_corrected=" + distence_corrected +
                ", alv_data='" + alv_data + '\'' +
                '}';
    }
}

