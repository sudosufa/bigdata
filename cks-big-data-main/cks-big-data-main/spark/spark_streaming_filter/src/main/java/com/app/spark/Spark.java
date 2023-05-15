package com.app.spark;

import com.app.spark.Filter.BlackZone;
import com.app.spark.Filter.ExternalPowerOrSignalNullDebugger;
import com.app.spark.Filter.TrackingIsOldDebugger;
import com.app.spark.model.TrackingMysql;
import com.app.spark.model.summaries;
import com.app.spark.util.PropertyFileReader;
import com.app.spark.model.Tracking;
import com.app.spark.util.Tool;
import com.app.spark.util.TrackingDeserializer;
import com.datastax.spark.connector.japi.CassandraJavaUtil;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;

import org.apache.spark.api.java.function.Function0;
import org.apache.spark.api.java.function.Function3;

import org.apache.spark.sql.*;
import org.apache.spark.streaming.Durations;

import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.*;

import org.jetbrains.annotations.NotNull;
import scala.Tuple2;
//import org.apache.spark.streaming.api.java.JavaStreamingContextFactory;
import java.io.Serializable;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.javaFunctions;
public class Spark implements Serializable {
    private static final Logger logger = Logger.getLogger(Spark.class);
    private String file;
    private static transient SparkSession instance = null;

    public Spark (String file ) {   this.file = file; }

    public void run() throws Exception {
        //System.setProperty("java.security.auth.login.config", "D:\\Work\\jp&co\\spark_streamin_app\\spark_streaming - Beta2\\src\\main\\resources\\spark_jaas.conf");
        //Properties prop = PropertyFileReader.readPropertyFile(file);
        //JavaStreamingContext ssc = new JavaStreamingContext(jsc, Durations.seconds(5));
        JavaStreamingContext ssc = JavaStreamingContext.getOrCreate(System.getenv("CHECK_POINT_DIR"),creatingFunc);
        ssc.start();
        ssc.awaitTermination();

    };



    Function0<JavaStreamingContext  > creatingFunc = () -> {
        //Properties prop = PropertyFileReader.readPropertyFile(file);
        SparkConf conf = getSparkConf();
        JavaSparkContext jsc = new JavaSparkContext(conf);
        JavaStreamingContext ssc =new JavaStreamingContext(jsc, Durations.seconds(5)) ;
        ssc.checkpoint(System.getenv("CHECK_POINT_DIR"));
        ssc.sparkContext().setLogLevel("ERROR");
        ssc.sparkContext().setLogLevel("WARN");
        Map<String,Object> kafkaParams = getKafkaParams();
        Collection<String> topics = Arrays.asList(System.getenv("KAFKA_TOPIC"));
        JavaInputDStream<ConsumerRecord<String, Tracking>> stream = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent(), ConsumerStrategies.<String, Tracking>Subscribe(topics, kafkaParams));
        logger.info("Starting Stream Processing");
        JavaDStream<Tracking> tracking= stream.map(ConsumerRecord::value) ;
        JavaDStream<Tracking> filterIMEI =  tracking.filter(this::filterIMEI);
        JavaDStream<Tracking> filteredIotDataStream =filterIMEI.filter(this::filter);
        savetocassandra(filteredIotDataStream);
        //savetomysql(filteredIotDataStream);
        
        //savetomysql2(filteredIotDataStream);
        //summaries(filteredIotDataStream);
        commitOffset(stream);
        return ssc ;
    };
    private void commitOffset(JavaInputDStream<ConsumerRecord<String, Tracking>> directKafkaStream) {
        directKafkaStream.foreachRDD((JavaRDD<ConsumerRecord<String, Tracking>> trafficRdd) -> {
            if (!trafficRdd.isEmpty()) {
                OffsetRange[] offsetRanges = ((HasOffsetRanges) trafficRdd.rdd()).offsetRanges();

                CanCommitOffsets canCommitOffsets = (CanCommitOffsets) directKafkaStream.inputDStream();
                canCommitOffsets.commitAsync(offsetRanges, new TrafficOffsetCommitCallback());
            }
        });
    }
    private Map<String, Object> getKafkaParams() {
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BROKET_LIST"));
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", TrackingDeserializer.class);
        kafkaParams.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, System.getenv("KAFKA_TOPIC"));
        kafkaParams.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, 65536);
        kafkaParams.put("security.protocol", System.getenv("KAFKA_SECURITY_PROTOCOL"));
        kafkaParams.put("sasl.mechanism", System.getenv("KAFKA_MECHANISM"));
        kafkaParams.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required\n username=\"consumer\"\n password=\"consumer-jp&co.001\";");
        return kafkaParams;
    }
    private SparkConf getSparkConf() {
        return new SparkConf()
                .setAppName(System.getenv("SPARK_APP_NAME"))
                .setMaster(System.getenv("SPARK_MASTER"))
                .set("spark.cassandra.connection.host", System.getenv("CASSANDRA_HOST"))
                .set("spark.cassandra.connection.port", System.getenv("CASSANDRA_PORT"))
                .set("spark.cassandra.auth.username", System.getenv("CASSANDRA_USERNAME"))
                .set("spark.cassandra.auth.password", System.getenv("CASSANDRA_PASSWORD"))
                .set("spark.cassandra.connection.keep_alive_ms", System.getenv("CASSANDRA_KEEP_ALIVE"))
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
                }if (new ExternalPowerOrSignalNullDebugger().ExternalPowerOrSignalNull(tracking)){
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

                if (new TrackingIsOldDebugger().Isold(tracking)){
                    //System.out.println("########## old data ##############");
                    logger.error("########## old data ##############");
                    return false;
                }
                break;
        }

        return true;
    }

    public boolean filter2 (Tracking tracking){
        return true;
    }
    public boolean filterIMEI (Tracking tracking){
        String sql = "SELECT * FROM device where IMEI="+tracking.getImei();
        try ( Connection com = DriverManager.getConnection("jdbc:mysql://"+System.getenv("DB_HOST")+":"+System.getenv("DB_PORT")+"/"+System.getenv("DB")+"?useSSL=false", System.getenv("DB_USER"), System.getenv("DB_PWD"));
              PreparedStatement preparedStatement = com.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            tracking.setId_vehicule(resultSet.getInt("id_vehicule"));
            tracking.setId_device(resultSet.getInt("ID_Device"));
        } catch (SQLException e) {
            logger.error(System.err.format("SQL State: %s %s", e.getSQLState(), e.getMessage()));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void summaries(JavaDStream<Tracking> filteredIotDataStream) {
        JavaDStream<summaries> count = filteredIotDataStream.map(maptosummaries);
        JavaPairDStream<Integer, summaries> countKmPair = count.mapToPair(track -> new Tuple2<>(track.getId_vehicule(), track));
        StateSpec<Integer, summaries, summaries, summaries> stateSpec = StateSpec.function(summariesFunc).timeout(Durations.minutes(120));;
        JavaMapWithStateDStream<Integer, summaries, summaries, summaries> countWithState =  countKmPair.mapWithState(stateSpec) ;
        JavaDStream<summaries> summaries = countWithState.map(tuple -> tuple);
        summaries.print();
        //Map<String, String> columnNameMappings = new HashMap<String, String>();
        javaFunctions(summaries)
                .writerBuilder(System.getenv("CASSANDRA_DB"), "summeries",CassandraJavaUtil
                        .mapToRow(com.app.spark.model.summaries.class))
                .saveToCassandra();
    }
    public void savetocassandra(JavaDStream<Tracking> rdd ) {
        Map<String, String> columnNameMappings = new HashMap<String, String>();
        columnNameMappings.put("temperature", "temperature1");columnNameMappings.put("etatSignal", "etatsignal");columnNameMappings.put("id_vehicule", "id_vehicle");columnNameMappings.put("avlData", "alv_data");columnNameMappings.put("lVCAN_VEHICLE_SPEED", "can_speed");columnNameMappings.put("lVCAN_ACCELERATOR_PEDAL_POSITION", "accelerator_pedal_position");columnNameMappings.put("lVCAN_FUEL_CONSUMED", "fuel_consumed");columnNameMappings.put("lVCAN_FUEL_LEVEL", "can_fuel_level");columnNameMappings.put("lVCAN_ENGINE_RPM", "engine_rpm");columnNameMappings.put("lVCAN_TOTAL_MILEAGE", "total_mileage");columnNameMappings.put("lVCAN_DOOR_STATUS", "door_status");columnNameMappings.put("lVCAN_FUEL_RATE", "fuel_rate");columnNameMappings.put("lVCAN_ENGINE_TEMPERATURE", "engine_temp");columnNameMappings.put("lVCAN_ENGINE_OIL_LEVEL", "engine_oil_level");columnNameMappings.put("lVCAN_GREEN_DRIVING_TYPE", "ecodrive_type");columnNameMappings.put("lVCAN_CRASH_DETECTION", "crash_detect");columnNameMappings.put("lVCAN_GREEN_DRIVING_VALUE", "ecodrive_value");columnNameMappings.put("lVCAN_GREEN_DRIVING_EVENT_DURATION", "ecodrive_duration");columnNameMappings.put("lVCAN_JAMMING", "jaming");columnNameMappings.put("lVCAN_ENGINE_WORKTIME", "engine_worktime");

        javaFunctions(rdd)
                .writerBuilder(System.getenv("CASSANDRA_DB"), "tracking_by_month",CassandraJavaUtil.mapToRow(Tracking.class, columnNameMappings))
                .saveToCassandra();
    }
    public void savetomysql(JavaDStream<Tracking> rdd ) throws ClassNotFoundException, SQLException {

        JavaDStream<TrackingMysql> trackingMysql = rdd.map(maptoMysql);
        trackingMysql.foreachRDD(rowRDD -> {
            if(!rowRDD.isEmpty()) {
                rowRDD.foreach(row -> {
                            Tool.DbInsert req = Tool.DbInsert.prepareQuery(row);
                            String reqTracking = req.getSQL().replace("{dbtTableName}", "tracking");
                            try ( Connection com = DriverManager.getConnection("jdbc:mysql://"+System.getenv("DB_PRO_HOST")+":"+System.getenv("DB_PRO_PORT")+"/"+System.getenv("DB_PRO")+"?useSSL=false", System.getenv("DB_PRO_USER"), System.getenv("DB_PRO_PWD"));
                                  PreparedStatement preparedStatement = com.prepareStatement(reqTracking)) {
                                preparedStatement.execute();
                            } catch (SQLException e) {
                                logger.error(System.err.format("SQL State: %s %s", e.getSQLState(), e.getMessage()));
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                );

            }
        });
    }
    public static SparkSession getInstance(SparkConf sparkConf) {
        if (instance == null) {
            instance = SparkSession
                    .builder()
                    .config(sparkConf)
                    .getOrCreate();
        }
        return instance;
    }

    private static final Function<Tracking, summaries> maptosummaries = (tuple -> {
        summaries summaries = new summaries();
        summaries.setId_vehicule(tuple.getId_vehicule());
        summaries.setUpdated_at(tuple.getTracking_time());
        summaries.setKm(tuple.getKm());
        summaries.setDepuis(tuple.getTracking_time());
        summaries.setAcc_status(tuple.getAcc_status());
        summaries.setDevice_type(tuple.getDevice_type());
        summaries.setLatitude(tuple.getLatitude());
        summaries.setLongitude(tuple.getLongitude());
        return summaries;
    });

    private static final Function3<Integer, Optional<summaries>, State<summaries>, summaries> summariesFunc = (key, kmcount, state) -> {

            summaries before = state.exists() ? state.get() : new summaries() ;
        if (kmcount.isPresent()) {
            summaries objectcurnt = kmcount.orNull();
            if (!state.exists()) {
                if(objectcurnt.getKm()== null) {
                    objectcurnt.setKm(0.0);
                }else {objectcurnt.setKm(objectcurnt.getKm());}


                if(!state.isTimingOut()) state.update(objectcurnt);
                return objectcurnt;
            }
            assert before != null;
            if (objectcurnt.getUpdated_at().compareTo(before.getUpdated_at())>=0) {
                // calcul Km
                if (objectcurnt.getDevice_type().equals("coban") || objectcurnt.getDevice_type().equals("xexun")) {
                    double distance = Tool.distanceBetween(objectcurnt.getLatitude(), objectcurnt.getLongitude(), before.getLatitude(), before.getLongitude());
                    System.out.println(distance);
                    objectcurnt.setKm(before.getKm() + distance );
                } else {
                    objectcurnt.setKm(before.getKm() + objectcurnt.getKm());
                }
            } else {
                objectcurnt.setUpdated_at(before.getUpdated_at());
                objectcurnt.setKm(before.getKm());
            }
            // check depuis
            if (objectcurnt.getAcc_status() != before.getAcc_status() &&  objectcurnt.getDepuis().compareTo(before.getDepuis())>0)  {
                    objectcurnt.setDepuis(objectcurnt.getUpdated_at());
            } else {
                    objectcurnt.setDepuis(before.getDepuis());
            }

            if (!state.isTimingOut()) state.update(objectcurnt);
            return objectcurnt;
        }

        return before;
    };

    private static final Function<Tracking, TrackingMysql> maptoMysql = (tuple -> {
        TrackingMysql mysql = new TrackingMysql();
        mysql.setAccelerator_pedal_position(tuple.getlVCAN_ACCELERATOR_PEDAL_POSITION());
        mysql.setAcc_status(tuple.getAcc_status());
        mysql.setActual_profile(tuple.getActual_profile());
        mysql.setAlert_checked(tuple.getAlert_checked());
        mysql.setAltitude(tuple.getAltitude());
        mysql.setAlv_data(new Gson().toJson(tuple.getAvlData()));
        mysql.setAngle(tuple.getAngle());
        mysql.setArea_code(tuple.getArea_code());
        mysql.setCan_fuel_level(tuple.getlVCAN_FUEL_LEVEL());
        mysql.setCell_id(tuple.getCell_id());
        mysql.setCurrent_battery(tuple.getCurrent_battery());
        mysql.setCan_speed(tuple.getlVCAN_VEHICLE_SPEED());
        mysql.setCrash_detect(tuple.getlVCAN_CRASH_DETECTION());
        mysql.setDistence_corrected(tuple.getDistence_corrected());
        mysql.setDoor_status(tuple.getlVCAN_DOOR_STATUS());
        mysql.setEcodrive_duration(tuple.getlVCAN_GREEN_DRIVING_EVENT_DURATION());
        mysql.setEcodrive_type(tuple.getlVCAN_GREEN_DRIVING_TYPE());
        mysql.setEcodrive_value(tuple.getlVCAN_GREEN_DRIVING_VALUE());
        mysql.setEngine_oil_level(tuple.getlVCAN_ENGINE_OIL_LEVEL());
        mysql.setEngine_temp(tuple.getlVCAN_ENGINE_TEMPERATURE());
        mysql.setEngine_rpm(tuple.getlVCAN_ENGINE_OIL_LEVEL());
        mysql.setEngine_worktime(tuple.getlVCAN_ENGINE_WORKTIME());
        mysql.setEtat(tuple.getEtat());
        mysql.setEtatSignal(tuple.getEtatSignal());
        mysql.setEvent(tuple.getEvent());
        mysql.setExternal_power(tuple.getExternal_power());
        mysql.setFuel_consumed(tuple.getlVCAN_FUEL_CONSUMED());
        mysql.setFuel_rate(tuple.getlVCAN_FUEL_RATE());
        mysql.setGnss_status(tuple.getGnss_status());
        mysql.setGps_hdop(tuple.getGps_hdop());
        mysql.setGps_pdop(tuple.getGps_pdop());
        mysql.setGps_speed(tuple.getGps_speed());
        mysql.setId_device(tuple.getId_device());
        mysql.setId_vehicule(tuple.getId_vehicule());
        mysql.setInput(tuple.getInput());
        mysql.setInternal_battery(tuple.getInternal_battery());
        mysql.setJaming(tuple.getlVCAN_JAMMING());
        mysql.setKm(tuple.getKm());
        mysql.setLatitude(tuple.getLatitude());
        mysql.setLongitude(tuple.getLongitude());
        mysql.setMessage(tuple.getMessage());
        mysql.setMovement(tuple.getMovement());
        mysql.setOperator_code(tuple.getOperator_code());
        mysql.setPanic_button_status(tuple.getPanic_button_status());
        mysql.setPcb_temp(tuple.getPcb_temp());
        mysql.setPriority(tuple.getPriority());
        mysql.setReservoir1(tuple.getReservoir1());
        mysql.setReservoir2(tuple.getReservoir2());
        mysql.setRfid(tuple.getRfid());
        mysql.setSatellites(tuple.getSatellites());
        mysql.setSleep_mode(tuple.getSleep_mode());
        mysql.setSpeed(tuple.getSpeed());
        mysql.setTemperature1(tuple.getTemperature());
        mysql.setTemperature2(tuple.getTemperature2());
        mysql.setTotal_mileage(tuple.getlVCAN_TOTAL_MILEAGE());
        mysql.setTracking_on_event(tuple.getTracking_on_event());
        mysql.setTracking_time(String.valueOf(tuple.getTracking_time()));
        return mysql;
    });




}
final class TrafficOffsetCommitCallback implements OffsetCommitCallback, Serializable {

    private static final Logger log = Logger.getLogger(TrafficOffsetCommitCallback.class);

    @Override
    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
        log.info("---------------------------------------------------");
        log.info(String.format("{0} | {1}", offsets, exception));
        log.info("---------------------------------------------------");
    }
}

  /*
  //Properties connectionProperties = new Properties();
                // connectionProperties.put("user", "olickochre");
                //connectionProperties.put("password", "n@mTfVVZ8R--UqpR");
  spark.read().jdbc("jdbc:mysql://51.68.181.61:3306/jptrack?useSSL=false", "(SELECT * FROM jptrack.view_device) as t", connectionProperties).as(Encoders.bean(Device.class)).limit(1);
*/
