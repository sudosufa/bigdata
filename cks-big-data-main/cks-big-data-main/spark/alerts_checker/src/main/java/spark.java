import alert.AlertsFactory;
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
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.types.StructType;
import util.Tools;

import java.io.Serializable;
import java.util.*;

public class spark implements Serializable {
    private static final Logger logger = Logger.getLogger(spark.class);

    public spark() {

    }
    public void run() throws Exception {
        SparkConf conf = getSparkConf();
        SparkSession instance = SparkSession.builder().config(conf).getOrCreate();
        instance.sparkContext().setLogLevel("ERROR");
        instance.sparkContext().setLogLevel("WARN");
        StructType schema = Tools.getSchema();
        Map<String, String> kafkaParams = getKafkaParams();
        Dataset<Tracking> kafkaStream = instance.readStream().format("org.apache.spark.sql.kafka010.KafkaSourceProvider")
                .options(kafkaParams)
                .load()
                .selectExpr("CAST(value AS STRING) as message")
                .select(functions.from_json(functions.col("message"), schema).as("json"))
                .select("json.*")
                .as(ExpressionEncoder.javaBean(Tracking.class));

        Dataset<Row> alertsStream = kafkaStream
                .groupByKey((MapFunction<Tracking, Integer>) Tracking::getId_vehicle, Encoders.INT())
                .flatMapGroupsWithState(updateFunctionCheckAlert, OutputMode.Append(),
                        Encoders.bean(Tracking.class), Encoders.bean(Alert.class),
                        GroupStateTimeout.NoTimeout()).drop("id_alert");

        //alertsStream.writeStream().queryName("table").format("console").outputMode("append").start();
        Map<String,String> producerKafkaParams = producerKafkaParams();
        alertsStream.selectExpr("to_json(struct(*)) AS value").queryName("Alert")
                .writeStream().format("org.apache.spark.sql.kafka010.KafkaSourceProvider").options(producerKafkaParams).start();

        instance.streams().awaitAnyTermination();

     /*   StreamingQuery query = trackingStream.writeStream()
                .format("org.apache.spark.sql.cassandra")
                .option("keyspace", "jptrack")
                .option("table", "tracking_by_month")
                .outputMode(OutputMode.Append()).start();
        query.awaitTermination();*/
    }

    FlatMapGroupsWithStateFunction<Integer, Tracking, Tracking, Alert> updateFunctionCheckAlert = (id_vehicle, tracking, groupState) -> {
        Tracking currentTracking;
        List<Alert> alertList = new ArrayList<>();
        if (groupState.exists()) {
            while (tracking.hasNext()) {
                Tracking trackingFromState = groupState.get();
                currentTracking = tracking.next();
                AlertsFactory alertsFactory = new AlertsFactory();
                alertList = alertsFactory.getAlert(currentTracking, trackingFromState);
                for (Alert elm : alertList
                ) {
                    System.out.println(elm);
                }
                groupState.update(currentTracking);
            }
        } else {
            while (tracking.hasNext()) {
                currentTracking = tracking.next();
                groupState.update(currentTracking);
            }
        }
        return alertList.iterator();
    };

    private Map<String, String> producerKafkaParams() {
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("kafka.bootstrap.servers", System.getenv("KAFKA_BROKET_LIST"));
        kafkaParams.put("topic", "alerts");
        kafkaParams.put("kafka.security.protocol", System.getenv("KAFKA_SECURITY_PROTOCOL"));
        kafkaParams.put("kafka.sasl.mechanism", System.getenv("KAFKA_MECHANISM"));
        kafkaParams.put("kafka.sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required\n username=\"producer\"\n password=\"producer-jp&co.001\";");
        return kafkaParams;
    }
    private Map<String, String> getKafkaParams() {
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("kafka.bootstrap.servers", System.getenv("KAFKA_BROKET_LIST"));
        kafkaParams.put("subscribe", System.getenv("KAFKA_TOPIC2"));
        kafkaParams.put("startingOffsets", "latest");
        kafkaParams.put("kafka.security.protocol", System.getenv("KAFKA_SECURITY_PROTOCOL"));
        kafkaParams.put("kafka.sasl.mechanism", System.getenv("KAFKA_MECHANISM"));
        kafkaParams.put("kafka.sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required\n username=\"consumer\"\n password=\"consumer-jp&co.001\";");
        return kafkaParams;
    }

    private SparkConf getSparkConf() {
        return new SparkConf()
                .setAppName("spark_alert")
                .setMaster(System.getenv("SPARK_MASTER"))
                .set("spark.sql.debug.maxToStringFields", "100")
                .set("spark.sql.shuffle.partitions", "1")
                .set("spark.sql.streaming.checkpointLocation", System.getenv("CHECK_POINT_DIR4"))
                .set("spark.cassandra.connection.host", System.getenv("CASSANDRA_HOST"))
                .set("spark.cassandra.connection.port", System.getenv("CASSANDRA_PORT"))
                .set("spark.cassandra.auth.username", System.getenv("CASSANDRA_USERNAME"))
                .set("spark.cassandra.auth.password", System.getenv("CASSANDRA_PASSWORD"))
                ;
    }
}
