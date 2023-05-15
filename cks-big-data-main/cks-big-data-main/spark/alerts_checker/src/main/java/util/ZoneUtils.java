package util;

import model.Tracking;
import model.Zone;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class ZoneUtils {

    public static Zone getZone(int id) {
        Zone zone = null;
        String query = "SELECT * FROM zones WHERE zones.id=" + id;
        try (Connection con = DriverManager
                .getConnection("jdbc:mysql://51.89.43.61:3306/jptrack?useSSL=false", "root", "jpandco.2020");

             PreparedStatement preparedStatement = con.prepareStatement(query);) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return null;
            } else {
                resultSet.next();
                zone = new Zone(resultSet
                        .getInt("id"), resultSet.getString("libelle"), resultSet.getString("geomtype"), resultSet.getInt("id_societe"));
                return zone;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zone;
    }

    public static List<Zone> getLastZone(Tracking previous, String idZones) {
        idZones = idZones.replace(';', ',');
        idZones = idZones.substring(0, idZones.lastIndexOf(','));
        List<Zone> zones = new ArrayList<Zone>();
        if (previous != null) {
            double longitude = previous.getLongitude();
            double latitude = previous.getLatitude();
            String query = "SELECT zones.* FROM zones WHERE zones.id IN ("
                    + idZones
                    + ") AND ((zones.geomType='Polygon' AND (ST_WITHIN(ST_GeomFromText(CONCAT('POINT(',"
                    + longitude
                    + ",' ',"
                    + latitude
                    + ",')'),4326),zones.geom))) OR (	(zones.geomType='LineString' OR zones.geomType='Point') AND	 ST_Intersects(ST_buffer(zones.geom,(zones.rayon/100000)),ST_GeomFromText(CONCAT('POINT(',"
                    + longitude
                    + ",' ',"
                    + latitude
                    + ",')'),4326)))) ";

            try (Connection con = DriverManager
                    .getConnection("jdbc:mysql://51.89.43.61:3306/jptrack?useSSL=false", "root", "jpandco.2020");

                 PreparedStatement preparedStatement = con.prepareStatement(query);) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Zone zone = new Zone(resultSet
                            .getInt("id"), resultSet.getString("libelle"), resultSet.getString("geomtype"), resultSet.getInt("id_societe"));
                    zones.add(zone);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return zones;
    }


    public static boolean isInside(Tracking tracking, Zone zone) {
        String req = "";
        boolean inside = false;

        if (zone.getGeomtype().equalsIgnoreCase("Polygon")) {
            req = "SELECT ST_WITHIN(ST_GeomFromText('POINT( "
                    + tracking.getLongitude()
                    + " "
                    + tracking.getLatitude()
                    + " )',4326),zones.geom) as inside FROM zones WHERE zones.id="
                    + zone.getId();
        } else {
            req = "SELECT  ST_WITHIN(ST_GeomFromText('POINT( "
                    + tracking.getLongitude()
                    + " "
                    + tracking.getLatitude()
                    + " )',4326),ST_Buffer(zones.geom,zones.rayon/100000)) AS inside FROM zones WHERE zones.id="
                    + zone.getId();
        }

        try (Connection con = DriverManager
                .getConnection("jdbc:mysql://51.89.43.61:3306/jptrack?useSSL=false", "root", "jpandco.2020");

             PreparedStatement preparedStatement = con.prepareStatement(req);) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return true;
            } else {
                resultSet.next();
                inside = resultSet.getBoolean("inside");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inside;
    }

    public static List<Zone> getCurrentZone(Tracking tracking, String idZones) {
        idZones = idZones.replace(';', ',');
        idZones = idZones.substring(0, idZones.lastIndexOf(','));
        List<Zone> zones = new ArrayList<Zone>();
        String req = "SELECT * FROM zones WHERE zones.id IN (" + idZones +
                ") AND ((zones.geomType='Polygon'AND ST_WITHIN(ST_GeomFromText('POINT("
                + tracking.getLongitude() + " " + tracking.getLatitude() +
                ")',4326),zones.geom)) OR ((zones.geomType='LineString' OR zones.geomType='Point') AND ST_Intersects(ST_buffer(zones.geom,(zones.rayon/100000)),ST_GeomFromText('POINT("
                + tracking.getLongitude() + " " + tracking.getLatitude() +
                ")',4326)) ) ) ";

        try (Connection con = DriverManager.getConnection(
                        "jdbc:mysql://51.89.43.61:3306/jptrack?useSSL=false",
                        "root",
                        "jpandco.2020");

             PreparedStatement preparedStatement = con.prepareStatement(req);) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Zone zone = new Zone(
                        resultSet.getInt("id"),
                        resultSet.getString("libelle"),
                        resultSet.getString("geomtype"),
                        resultSet.getInt("id_societe"));

                zones.add(zone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zones;
    }

    public static boolean contains(List<Zone> zones, Zone zone) {

        for (Zone z : zones) {
            if (z.getId() == zone.getId())
                return true;
        }
        return false;

    }

}
