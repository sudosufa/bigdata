package app.decoder.devices;

import app.decoder.basic.DecoderDevice;
import app.model.DeviceTracking;
import app.tools.Tools;
import app.tools.vondors.CodecException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XexunDecoder extends DecoderDevice {
    protected List<String> data = new ArrayList<>();
    private Keys keys = new Keys();

    @Override
    public void initDataInputStream() throws IOException, CodecException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String tram = br.readLine();
        if (tram == null)
            throw new CodecException("error occured.");
        data = Arrays.asList(tram.split(","));
    }

    @Override
    public String getImei() {
        return data.get(keys.IMEI);
    }

    @Override
    public List<DeviceTracking> decode() {
        List<DeviceTracking> deviceTrackings = new ArrayList<>();
        DeviceTracking deviceTracking = newTracking();
        for (int i = 0; i < data.size(); i++) {
            deviceTracking.setAvlData(i, data.get(i));
        }
        deviceTracking.setBasicData(device.getImei(), device.getId(), device.getId_vehicule(), null);
        deviceTracking.setAcc_status(Integer.parseInt(data.get(keys.ACC)));
        deviceTracking.setTracking_time(Tools.DateTime.getTimestampFromDate(data.get(keys.TRACKING_SEND_DATETIME)));
        deviceTracking.setLongitude(Tools.GBS.toDD(data.get(keys.GPS_LONGITUDE), data.get(keys.GPS_W_E), keys.GPS_LATITUDE_IngDegreeEnd));
        deviceTracking.setLatitude(Tools.GBS.toDD(data.get(keys.GPS_LATITUDE), data.get(keys.GPS_N_S), keys.GPS_LONGITUDE_IngDegreeEnd));
        if (!data.get(keys.ALTITUDE).equals("")) {
            deviceTracking.setAltitude(Double.parseDouble(data.get(keys.ALTITUDE)));
        }
        deviceTracking.setMessage(data.get(keys.MESSAGE));
        // convert knote to KM
        double speedKnot = Double.parseDouble(data.get(keys.GPS_SPEED));
        double speed;
        if (speedKnot <= 1) {
            speed = 0;
        } else {
            speed = speedKnot * 1.852000;
            if (speed > 150) {
                deviceTracking.setGps_speed((int) speed);
                speed = -1;
            }
        }
        deviceTracking.setSpeed(speed);
        Matcher m_reservoir = Pattern.compile("([0-9]{0,3}\\.[0-9]{0,2})%").matcher(data.get(keys.RESERVOIR1));
        if (m_reservoir.find()) {
            deviceTracking.setReservoir1(Double.parseDouble(m_reservoir.group(1)));
        }
        if (!data.get(keys.TEMPERATURE).equals("")) {
            deviceTracking.setTemperature(Double.parseDouble(data.get(keys.TEMPERATURE)));
        }
        deviceTracking.setExternal_power(Double.parseDouble(data.get(keys.External_power)));
        String str = data.get(keys.BATTERY_STATUS);

        Pattern p = Pattern.compile("[0-9].[0-9]*");
        Matcher m = p.matcher(str);
        if (m.find()) {
            deviceTracking.setCurrent_battery(Double.parseDouble(m.group()));
        }
        if (!data.get(keys.TEMPERATURE2).equals("")) {
            deviceTracking.setTemperature2(Double.parseDouble(data.get(keys.TEMPERATURE2)));
        } else {
            deviceTracking.setTemperature2(Double.valueOf(0));
        }

        Matcher m_reservoir2 = Pattern.compile("([0-9]{0,3}\\.[0-9]{0,2})%").matcher(data.get(keys.RESERVOIR2));
        if (m_reservoir2.find()) {
            deviceTracking.setReservoir2(Double.parseDouble(m_reservoir2.group(1)));
        }

        if (data.get(keys.GPS_QUALITY).equals("F")) {
            deviceTracking.setEtatSignal(4);
        } else {
            deviceTracking.setEtatSignal(1);
        }
        deviceTracking.setSatellites(Integer.parseInt(data.get(keys.NUMBER_OF_SATELLITE)));

        deviceTrackings.add(deviceTracking);
        return deviceTrackings;
    }


    private static class Keys {
        int
//                AUTHORIZED_NUMBER = 1,
//                GPRM = 2,
//                GPS_TIME = 3,//todo ??
//                GPS_STATUS = 4,
//                GPS_COURS = 10,
//                GPS_DATE = 11,//todo ??
//                GPS_MAGNITIC_DEGRE = 12,
//                CRC16 = 14,
//                GPS_LEN = 22,
//                CRC = 23,
//                MCC = 24,
//                MNC = 25,
//                LAC = 26,
//                CELLID = 27,
                TRACKING_SEND_DATETIME = 0,
                GPS_LONGITUDE_IngDegreeEnd = 2,
                GPS_LATITUDE_IngDegreeEnd = 3,
                GPS_LATITUDE = 5,
                GPS_N_S = 6,
                GPS_LONGITUDE = 7,
                GPS_W_E = 8,
                GPS_SPEED = 9,
                GPS_QUALITY = 15,
                MESSAGE = 16,
                IMEI = 17,
                NUMBER_OF_SATELLITE = 18,
                ALTITUDE = 19,
                BATTERY_STATUS = 20,
                External_power = 21,
                ACC = 29,
                TEMPERATURE = 30,
                TEMPERATURE2 = 31,
                RESERVOIR2 = 32,
                RESERVOIR1 = 33;
    }

}

