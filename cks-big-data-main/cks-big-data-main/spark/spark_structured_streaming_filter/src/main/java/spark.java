//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import alert.AlertsFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import model.Alert;
import model.Tracking;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapGroupsWithStateFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.streaming.GroupStateTimeout;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.types.StructType;
import util.PropertyFileReader;
import util.Tools;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;

public class spark implements Serializable {
    private static final Logger logger = Logger.getLogger(spark.class);
    public spark () {};

    public void run() throws Exception {
        SparkConf conf = getSparkConf();
        SparkSession instance= SparkSession.builder().config(conf).getOrCreate();
        instance.sparkContext().setLogLevel("ERROR");
        instance.sparkContext().setLogLevel("WARN");
        StructType schema =  Tools.getSchema();
        Map<String,String> kafkaParams = getKafkaParams();
        Dataset<Tracking> source = instance.readStream().format("org.apache.spark.sql.kafka010.KafkaSourceProvider")
                .options(kafkaParams).load()
                .selectExpr("CAST(value AS STRING) as message")
                .select(functions.from_json(functions.col("message"),schema).as("json"))
                .select("json.*").as(ExpressionEncoder.javaBean(Tracking.class));
        Dataset<Tracking> test = source.filter(this::filter);

        Dataset<Tracking> true_data=test.filter(this::filterIMEI).map(Camouflage,ExpressionEncoder.javaBean(Tracking.class));

        Dataset<Tracking> updatedTracking = true_data
                .groupByKey((MapFunction<Tracking, Integer>) Tracking::getId_vehicle, Encoders.INT())
                .flatMapGroupsWithState(Camouflage_state,OutputMode.Append() ,Encoders.bean(Tracking.class), Encoders.bean(Tracking.class), GroupStateTimeout.NoTimeout());

        Dataset<Row> cassandra_Tracking = updatedTracking.drop("device_type","imei");


        //cassandra_Tracking.writeStream().queryName("tracking").format("console").outputMode("append").start();
        cassandra_Tracking.writeStream().format("org.apache.spark.sql.cassandra").queryName("tracking_cassandra").option("keyspace", "jptrack").option("table", "tracking_by_month2").outputMode(OutputMode.Append()).start();
        Map<String,String> producerKafkaParams = producerKafkaParams();
        updatedTracking
                .selectExpr("to_json(struct(*)) AS value")
                .writeStream()
                .format("org.apache.spark.sql.kafka010.KafkaSourceProvider")
                .queryName("tracking_to_kafka")
                .options(producerKafkaParams)
                .start();
        instance.streams().awaitAnyTermination();


    }


    ;
    //comferage for telkonik if data isnt old than state date


    private Map<String, String> getKafkaParams() {
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("kafka.bootstrap.servers", System.getenv("KAFKA_BROKET_LIST"));
        kafkaParams.put("subscribe", System.getenv("KAFKA_TOPIC"));
        kafkaParams.put("startingOffsets", "latest");
        kafkaParams.put("kafka.security.protocol", System.getenv("KAFKA_SECURITY_PROTOCOL"));
        kafkaParams.put("kafka.sasl.mechanism", System.getenv("KAFKA_MECHANISM"));
        kafkaParams.put("kafka.sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required\n username=\"consumer\"\n password=\"consumer-jp&co.001\";");
        return kafkaParams;
    }

    private Map<String, String> producerKafkaParams() {
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("kafka.bootstrap.servers", System.getenv("KAFKA_BROKET_LIST"));
        kafkaParams.put("topic", "filteredData");
        kafkaParams.put("kafka.security.protocol", System.getenv("KAFKA_SECURITY_PROTOCOL"));
        kafkaParams.put("kafka.sasl.mechanism", System.getenv("KAFKA_MECHANISM"));
        kafkaParams.put("kafka.sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required\n username=\"producer\"\n password=\"producer-jp&co.001\";");
        return kafkaParams;
    }

    private SparkConf getSparkConf() {
        return new SparkConf()
                .setAppName(System.getenv("SPARK_APP_NAME"))
                .setMaster(System.getenv("SPARK_MASTER"))
                .set("spark.sql.debug.maxToStringFields", "150")
                .set("spark.sql.shuffle.partitions", "1")
                .set("spark.sql.streaming.checkpointLocation", System.getenv("CHECK_POINT_DIR2"))
                .set("spark.cassandra.connection.host", System.getenv("CASSANDRA_HOST"))
                .set("spark.cassandra.connection.port", System.getenv("CASSANDRA_PORT"))
                .set("spark.cassandra.auth.username", System.getenv("CASSANDRA_USERNAME"))
                .set("spark.cassandra.auth.password", System.getenv("CASSANDRA_PASSWORD"))
                ;
    }

    public boolean filter (Tracking tracking){
        String type = tracking.getDevice_type();
        switch (type) {
            case "Teltonika":
                if(new BlackZone().istIn(tracking)){
                    //System.out.println("########## in black zone##############");
                    logger.error("##########in black zone##############");
                    return false;
                }
                if (new ExternalPowerOrSignalNullDebugger().ExternalPowerOrSignalNull(tracking)){
                    //System.out.println("########## External Power Or Signal Null##############");
                    logger.error("########## External Power Or Signal Null##############");
                    return false;
                }
               if (new TrackingIsOldDebugger().Isold(tracking)){
                   //System.out.println("########## old data ##############");
                   logger.error("########## old data ##############");
                   return false;
               }
                break;
            case "cellocator":
            case "xexun":
            case "coban":
                if(new BlackZone().istIn(tracking)){
                    //System.out.println("########## in black zone##############");
                    logger.error("##########in black zone##############");
                    return false;
                }

//                if (new TrackingIsOldDebugger().Isold(tracking)){
//                    //System.out.println("########## old data ##############");
//                    logger.error("########## old data ##############");
//                    return false;
//                }
//                break;
        }

        return true;
    }

    public boolean filterIMEI (Tracking tracking){

            String sql = "SELECT * FROM device where IMEI=" + tracking.getImei();
            try (Connection com = DriverManager.getConnection("jdbc:mysql://51.89.43.61:3306/jptrack?useSSL=false", "root", "jpandco.2020");
                 PreparedStatement preparedStatement = com.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                tracking.setId_vehicle(resultSet.getInt("id_vehicule"));
                tracking.setId_device(resultSet.getInt("ID_Device"));
                tracking.setId_societe(resultSet.getInt("fk_id_societe"));
            } catch (SQLException e) {
                logger.error(System.err.format("SQL State: %s ##########IMEI INValid############## ", e.getSQLState()));
                return false;
            }
            return true;

    }

    FlatMapGroupsWithStateFunction<Integer, Tracking, Tracking, Tracking> Camouflage_state = (id_vehicle, tracking, groupState) -> {
        Tracking currentTracking;
        ArrayList<Tracking> currentTrackinglist =new ArrayList<>();
        if (groupState.exists()) {
            while (tracking.hasNext()) {
                Tracking trackingFromState = groupState.get();
                currentTracking = tracking.next();
                Tools.calcul_km(currentTracking, trackingFromState);
                if(currentTracking.getDevice_type().equals("coban")|| currentTracking.getDevice_type().equals("xexun")){
                    Tools.Camouflage_using_state(currentTracking, trackingFromState);
               }
                if(currentTracking.getDevice_type().equals("Teltonika")){
                    Tools.FixBrokenTrackingData(currentTracking, trackingFromState);
                }
                currentTrackinglist.add(currentTracking);
                groupState.update(currentTracking);
            }
        } else {
            while (tracking.hasNext()) {
                currentTracking = tracking.next();
                if ( currentTracking.getTemperature1() != null && currentTracking.getTemperature1() == 85) {
                    currentTracking.setTemperature1(0.0);
                }
                currentTrackinglist.add(currentTracking);
                groupState.update(currentTracking);
            }
        }
        return currentTrackinglist.iterator();
    };

    private static final MapFunction<Tracking,Tracking> Camouflage = (tuple)-> {
        if(tuple != null && tuple.getDevice_type().equals("Teltonika") ){

            if (tuple.getTemperature1() != null && tuple.getTemperature1() >= 6) {
                LocalTime local_tracking_time = tuple.getTracking_time().toLocalDateTime().toLocalTime();
                if (local_tracking_time.isAfter(LocalTime.parse("04:00:00")) && local_tracking_time.isBefore(LocalTime.parse("04:30:00"))) {
                    double diff_min = ChronoUnit.MINUTES.between(LocalTime.parse("04:00:00"), local_tracking_time);
                    double condt = tuple.getTemperature1() - diff_min * 1.5;
                    if (condt > 7.5) {
                        tuple.setTemperature1(condt);
                    } else {
                        tuple.setTemperature1(7 - 20 / tuple.getTemperature1());
                    }
                } else if (local_tracking_time.isAfter(LocalTime.parse("04:30:00")) && local_tracking_time.isBefore(LocalTime.parse("12:00:00"))) {
                    tuple.setTemperature1(7 - 20 / tuple.getTemperature1());
                } else if (local_tracking_time.isAfter(LocalTime.parse("12:00:00")) && local_tracking_time.isBefore(LocalTime.parse("15:00:00"))) {
                    double diff_min = ChronoUnit.MINUTES.between(LocalTime.parse("12:00:00"), local_tracking_time);
                    double condt = tuple.getTemperature1() - (7 - 20 / tuple.getTemperature1() + diff_min * 0.2);
                    if (condt > 0) {
                        tuple.setTemperature1((7 - 20 / tuple.getTemperature1()) + diff_min * 0.2);
                    }
                }
                BigDecimal bd = BigDecimal.valueOf(tuple.getTemperature1());
                bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
                tuple.setTemperature1(bd.doubleValue());
            }

        }

        return tuple;
        };












    }


