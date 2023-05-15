package util;
import model.Poi;
import model.Tracking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class PoiUtils {

    public Poi getPoi(int id) {
        String query = "SELECT * FROM p_poi WHERE p_poi.p_id==" + id;
        Poi poi = new Poi();
        try (Connection con = DriverManager
                .getConnection("jdbc:mysql://51.89.43.61:3306/jptrack?useSSL=false", "root", "jpandco.2020");
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return null;
            } else {
                resultSet.next();
                poi.setId(resultSet.getInt("p_id"));
                poi.setLabel(resultSet.getString("p_libelle"));
                poi.setRayon(resultSet.getInt("p_rayon"));
                poi.setId_societe(resultSet.getInt("id_societe"));
                poi.setLongitude(resultSet.getDouble("longitude"));
                poi.setLatitude(resultSet.getDouble("latitude"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poi;
    }

    public static List<Poi> getLastPoi(Tracking previous, String rayon, String idPois) {

        idPois = idPois.replace(';', ',');
        idPois = idPois.substring(0, idPois.lastIndexOf(','));
        List<Poi> pois = new ArrayList<Poi>();

        if (previous != null) {
            double longitude = previous.getLongitude();
            double latitude = previous.getLatitude();
            String query = "SELECT p_poi.* FROM p_poi WHERE p_poi.p_id IN("
                    + idPois
                    + ") AND  ST_Intersects(ST_buffer(ST_GeomFromText(CONCAT('POINT(',p_poi.longitude,' ',p_poi.latitude,')'),4326),("
                    + rayon + "/1000000)),ST_GeomFromText(CONCAT('POINT('," + longitude + ",' '," + latitude
                    + ",')'),4326))";
            try (Connection con = DriverManager
                    .getConnection("jdbc:mysql://51.89.43.61:3306/jptrack?useSSL=false", "root", "jpandco.2020");
                 PreparedStatement preparedStatement = con.prepareStatement(query);) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Poi poi = new Poi(resultSet.getInt("p_id"),
                            resultSet.getString("p_libelle"),
                            resultSet.getInt("p_rayon"),
                            resultSet.getDouble("longitude"),
                            resultSet.getDouble("latitude"),
                            resultSet.getInt("id_societe"));
                    pois.add(poi);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pois;
    }


    public static List<Poi> getCurrentPoi(Tracking current, String rayon, String idPois) {
        idPois = idPois.replace(';', ',');
        idPois = idPois.substring(0, idPois.lastIndexOf(','));
        List<Poi> pois = new ArrayList<>();
        String query = "SELECT p_poi.* FROM p_poi WHERE p_poi.p_id IN(" + idPois
                + ") AND  ST_Intersects(ST_buffer(ST_GeomFromText(CONCAT('POINT(',p_poi.longitude,' ',p_poi.latitude,')'),4326),("
                + rayon + "/1000000)),ST_GeomFromText(CONCAT('POINT('," + current.getLongitude() + ",' ',"
                + current.getLatitude() + ",')'),4326)) ";


        try (Connection con = DriverManager
                .getConnection("jdbc:mysql://51.89.43.61:3306/jptrack?useSSL=false", "root", "jpandco.2020");
             PreparedStatement preparedStatement = con.prepareStatement(query);) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Poi poi = new Poi(resultSet.getInt("p_id"),
                        resultSet.getString("p_libelle"),
                        resultSet.getInt("p_rayon"),
                        resultSet.getDouble("longitude"),
                        resultSet.getDouble("latitude"),
                        resultSet.getInt("id_societe"));
                pois.add(poi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pois;
    }

    public static boolean isInside(Tracking current, Poi poi, String rayon) {
        boolean inside = false;
        String query = "SELECT ST_WITHIN(ST_GeomFromText('POINT( " + current.getLongitude() + " "
                + current.getLatitude() + " )',4326),ST_Buffer(ST_GeomFromText('POINT( " + poi.getLongitude()
                + " " + poi.getLatitude() + " )',4326)," + rayon
                + "/1000000)) AS inside FROM p_poi WHERE p_poi.p_id=" + poi.getId();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://51.89.43.61:3306/jptrack?useSSL=false",
                "root",
                "jpandco.2020");
             PreparedStatement preparedStatement = con.prepareStatement(query);) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return false;
            } else {
                resultSet.next();
                inside = resultSet.getBoolean("inside");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inside;
    }
}