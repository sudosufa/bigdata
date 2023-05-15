import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.cassandra.DataFrameReaderWrapper

import org.apache.commons.math3.distribution.NormalDistribution
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
object Filter extends App {

  override def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("app")
      .set("spark.debug.maxToStringFields", "200")
      .set("spark.sql.session.timeZone", "UTC")
      .set("spark.cassandra.connection.host", "51.89.43.61")
      .set("spark.cassandra.connection.port", "9042")
      .set("spark.cassandra.auth.username", "cassandra")
      .set("spark.cassandra.auth.password", "jpandco.2020")

    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")
    spark.sparkContext.setLogLevel("WARN")
    val DFcassandra = spark.read.cassandraFormat("tracking_by_month", "jptrack")
      .load().where((col("id_vehicle")===args(0).toInt) &&(col("year")===args(1).toInt)&&(col("month")===args(2).toInt) &&(col("tracking_time") >= args(3))  &&(col("tracking_time")<= args(4))                ).
      select("id_vehicle","year","month","tracking_time","longitude", "latitude","acc_status","speed","message","id_tracking")
    //DFcassandra.show()
    val DFF = DFcassandra.withColumn("Date", to_date(col("tracking_time")))
    val w = Window.partitionBy("id_vehicle", "Date").orderBy("id")
    val df2 = DFF.withColumn("id", monotonically_increasing_id)
      .withColumn("longitude_previous", lag("longitude", 1).over(w))
      .withColumn("latitude_previous", lag("latitude", 1).over(w))
      .withColumn("tracking_time_previous", lag(columnName = "tracking_time", 1).over(w))
      .withColumn("speed_previous", lag(columnName = "speed", 1).over(w))
      .withColumn("Duree",col("tracking_time_previous").cast("Long")-col("tracking_time").cast("Long"))
      .select(col("id_tracking"),col("id"), col("id_vehicle"),col("Date"),col("acc_status"), col("tracking_time"),col("tracking_time_previous"),col("Duree"), col("longitude"), col("longitude_previous"), col("latitude") ,col("latitude_previous"),col("speed"),col("speed_previous")).orderBy("id")
    //df2.show()
    //  Calcule de la distance
    def haversine_distance(longitude1 : Double,longitude2 : Double,latitude1 : Double,latitude2 : Double) : Double= {

      val R = 6372.8
      val dlat = math.toRadians(latitude2 - latitude1)
      val dlog = math.toRadians(longitude2 - longitude1)
      val a = math.sin(dlat / 2) * math.sin(dlat / 2) + math.cos(math.toRadians(latitude1)) * math.cos(math.toRadians(latitude2)) * math.sin(dlog / 2) * math.sin(dlog / 2)
      val c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
      val distance = R * c
      distance
    }
    // Calcule de l'accélération
    def acceleration (v1 : Double,v2 : Double,T : Long ) : Double = {
      val acceleration = v1-v2 /T
      acceleration
    }
    val distance_udf = udf(haversine_distance _)
    val acceleration_udf = udf(acceleration _)
    df2.cache()
    val DF_F = df2.withColumn("Distance", distance_udf(col("longitude"),col("longitude_previous"),col("latitude"),col("latitude_previous")))
                  .withColumn("Acceleration", acceleration_udf(col("speed"),col("speed_previous"),col("Duree")))
    DF_F.cache()
    val Nan=  DF_F.withColumn("speed", when(col("speed").isin(Double.PositiveInfinity,Double.NegativeInfinity,Double.NaN),0.0)
      .otherwise(col("speed"))).withColumn(colName = "Acceleration",when(col("Acceleration").isin(Double.PositiveInfinity,Double.NegativeInfinity,Double.NaN),0.0)
      .otherwise(col("Acceleration"))).withColumn("Distance", when(col("Distance").isin(Double.PositiveInfinity,Double.NegativeInfinity,Double.NaN),0.0)
      .otherwise(col("Distance")))

    val groupedMS = Nan.groupBy("id_vehicle","Date").agg(("Distance", "mean"),("Distance", "stddev_pop"))
    val groupedMS1 = Nan.groupBy("id_vehicle","Date").agg(("speed", "mean"),("speed", "stddev_pop"))
    val greoupedMS2 = Nan.groupBy("id_vehicle","Date").agg(("Acceleration", "mean"),("Acceleration", "stddev_pop"))
    val mean_sttdv=  Nan.join(groupedMS, Seq("id_vehicle","Date")).join(groupedMS1,Seq("id_vehicle","Date")).join(greoupedMS2,Seq("id_vehicle","Date")).orderBy("id").cache()

    val score = mean_sttdv.withColumn("z test_distance", (col("Distance") - col("avg(Distance)")) / col("stddev_pop(Distance)"))
                          .withColumn("z test_speed", (col("speed") - col("avg(speed)")) / col("stddev_pop(speed)"))
                          .withColumn("z test_acceleration", (col("Acceleration") - col("avg(Acceleration)")) / col("stddev_pop(Acceleration)"))

    def cdf (x:Double): Double ={
      val normm =  new NormalDistribution(0,1)
      1- normm.cumulativeProbability(x)
    }
    val fun= udf(cdf _)
    val p_value = score.cache().withColumn("p_value_distance",fun(col("z test_distance")))
                              .withColumn("p-value_speed",fun(col("z test_speed")))
                              .withColumn("p-value_acceleration",fun(col("z test_acceleration")))
    //p_value.show()
    // Filter selon le seuil 0.6
    val test_df = p_value

    val z = Window.partitionBy("id_vehicle", "Date").orderBy("id")

    val percentage_acc_before_filter_df = test_df
      .withColumn("acc_previous", lag("acc_status", 1).over(z))
      .withColumn("acc_next", lead("acc_status",1).over(z))
      .select(col("acc_status"), col("tracking_time"), col("acc_previous"),col("acc_next") )
      .orderBy("tracking_time")

    val filtered_acc = percentage_acc_before_filter_df
      .select( col("acc_status"), col("acc_previous"), col("acc_next"))
      .where((col("acc_status") === 0) && (col("acc_previous") === 0) && (col("acc_next") === 0))

    val filter_data = p_value.cache().filter(col("p_value_distance")<args(5).toDouble and col("p-value_speed")<args(6).toDouble and col("p-value_acceleration")<args(7).toDouble ).orderBy(col("tracking_time"))

    val deleted_data =  p_value.except(filter_data)

    val after_filter_df = deleted_data.withColumn("acc_previous", lag("acc_status", 1).over(w))
      .withColumn("acc_next", lead("acc_status",1).over(w))
      .orderBy("tracking_time")

    val acc_zero = after_filter_df
      .where((col("acc_status") === 0) && (col("acc_previous") === 0) && (col("acc_next") === 0))
      
    val acc_not_zero = after_filter_df.except(acc_zero);
    acc_not_zero.write.format("com.crealytics.spark.excel").option("header",value = true).save("/opt/spark-data-filter/"+args(0)+"/"+args(3)+"_to"+args(4)+"/"+args(5)+"/acc_not_zero.xlsx")

    println("total data :" + test_df.count())
    println("filtered data (to be kept) :" + filter_data.count())
    println("to be deleted data :" + deleted_data.count())
    println("% of to be deleted data : " + (( test_df.count() - filter_data.count())*100)/ test_df.count() +"%" )
    println("% acc = 0 before filter : "+ (filtered_acc.count() * 100 )/ test_df.count() + "%" )
    println("% acc = 0 in to be deleted data : "+ (acc_zero.count() * 100 )/ deleted_data.count() + "%" )

    deleted_data.write.format("com.crealytics.spark.excel").option("header",value = true).save("/opt/spark-data-filter/"+args(0)+"/"+args(3)+"_to"+args(4)+"/"+args(5)+"/deleted_data.xlsx")
    //filter_data.select(col("latitude"),col("longitude"),col("tracking_time")).cache().write.format("com.crealytics.spark.excel").option("header",value = true).save("resultat.xlsx")
    filter_data.write.format("com.crealytics.spark.excel").option("header",value = true).save("/opt/spark-data-filter/"+args(0)+"/"+args(3)+"_to"+args(4)+"/"+args(5)+"/filter_data.xlsx")
    //filter_data.show()

  }
}
