package model;
public class ProgramedAlert {
    private int id;
    private String title;
    private String category;
    private String startHour;
    private String endHour;
    private String startDate;
    private String endDate;
    private int limitSpeed;
    private String emails;
    private boolean energy;
    private boolean battery;
    private String event;
    private String idZones;
    private String idPois;
    private int rayon;
    private boolean isStopped;
    private int idCompany;
    private int approv;
    private String days;

    public ProgramedAlert() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getLimitSpeed() {
        return limitSpeed;
    }

    public void setLimitSpeed(int limitSpeed) {
        this.limitSpeed = limitSpeed;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public boolean isEnergy() {
        return energy;
    }

    public void setEnergy(boolean energy) {
        this.energy = energy;
    }

    public boolean isBattery() {
        return battery;
    }

    public void setBattery(boolean battery) {
        this.battery = battery;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getIdZones() {
        return idZones;
    }

    public void setIdZones(String idZones) {
        this.idZones = idZones;
    }

    public String getIdPois() {
        return idPois;
    }

    public void setIdPois(String idPois) {
        this.idPois = idPois;
    }

    public int getRayon() {
        return rayon;
    }

    public void setRayon(int rayon) {
        this.rayon = rayon;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public int getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
    }

    public int getApprov() {
        return approv;
    }

    public void setApprov(int approv) {
        this.approv = approv;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}