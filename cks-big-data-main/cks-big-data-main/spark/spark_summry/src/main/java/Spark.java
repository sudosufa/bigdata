import model.Summaries;
import model.Tracking;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapGroupsWithStateFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.GroupStateTimeout;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.types.StructType;
import util.PropertyFileReader;
import util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Spark {
    private static final Logger logger = Logger.getLogger(Spark.class);
    

    public Spark() {
       
    }

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

        Dataset<Summaries> summaries = source
                .groupByKey((MapFunction<Tracking, Integer>) Tracking::getId_vehicle, Encoders.INT())
                .flatMapGroupsWithState(summariesFunc, OutputMode.Append() ,Encoders.bean(Summaries.class), Encoders.bean(Summaries.class), GroupStateTimeout.NoTimeout());

        //summaries.writeStream().option("checkpointLocation", "tmp/demo-checkpoint").queryName("Summaries").format("console").outputMode("append").start();
        summaries.writeStream().format("org.apache.spark.sql.cassandra").queryName("Summaries").option("keyspace", "jptrack").option("table", "summeries").outputMode(OutputMode.Append()).start();

        instance.streams().awaitAnyTermination();

    }

    private Map<String, String> getKafkaParams() {
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("kafka.bootstrap.servers", System.getenv("KAFKA_BROKET_LIST"));
        kafkaParams.put("subscribe", "filteredData");
        kafkaParams.put("startingOffsets", "latest");
        kafkaParams.put("kafka.security.protocol", System.getenv("KAFKA_SECURITY_PROTOCOL"));
        kafkaParams.put("kafka.sasl.mechanism", System.getenv("KAFKA_MECHANISM"));
        kafkaParams.put("kafka.sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required\n username=\"consumer\"\n password=\"consumer-jp&co.001\";");
        return kafkaParams;
    }

    private SparkConf getSparkConf() {
        return new SparkConf()
                .setAppName(System.getenv("SPARK_APP_NAME"))
                .setMaster(System.getenv("SPARK_MASTER"))
                .set("spark.sql.debug.maxToStringFields", "150")
                .set("spark.sql.shuffle.partitions", "1")
                .set("spark.sql.streaming.checkpointLocation", System.getenv("CHECK_POINT_DIR3"))
                //.set("spark.sql.streaming.multipleWatermarkPolicy ", "max")
                .set("spark.cassandra.connection.host", System.getenv("CASSANDRA_HOST"))
                .set("spark.cassandra.connection.port", System.getenv("CASSANDRA_PORT"))
                .set("spark.cassandra.auth.username", System.getenv("CASSANDRA_USERNAME"))
                .set("spark.cassandra.auth.password", System.getenv("CASSANDRA_PASSWORD"))
                ;
    }

    FlatMapGroupsWithStateFunction<Integer, Tracking, Summaries, Summaries> summariesFunc = (id_vehicle, tracking, groupState) -> {
        Tracking currentTracking;
        ArrayList<Summaries> summarieslist =new ArrayList<>();
        Summaries summaries = new Summaries() ;
        if (groupState.exists()) {
            while (tracking.hasNext()) {
                summaries = groupState.get();
                currentTracking = tracking.next();
                if(currentTracking.getTracking_time().after(summaries.getUpdated_at())){

                    summaries.setKm(currentTracking.getKm()+ summaries.getKm());
                    summaries.setUpdated_at(currentTracking.getTracking_time());

                }
                if(!currentTracking.getAcc_status().equals(summaries.getAcc_status()) && currentTracking.getTracking_time().after(summaries.getDepuis())){
                    summaries.setAcc_status(currentTracking.getAcc_status());
                    summaries.setDepuis(currentTracking.getTracking_time());
                }


                summarieslist.add(summaries);
                groupState.update(summaries);

            }
        }
        else {
            while (tracking.hasNext()) {
                currentTracking = tracking.next();
                summaries.setId_vehicule(currentTracking.getId_vehicle());
                summaries.setUpdated_at(currentTracking.getTracking_time());
                summaries.setKm(0.0);
                summaries.setDepuis(currentTracking.getTracking_time());
                summaries.setAcc_status(currentTracking.getAcc_status());
                summaries.setDevice_type(currentTracking.getDevice_type());
                summarieslist.add(summaries);
                groupState.update(summaries);
            }

        }
        return summarieslist.iterator();
    };

}

