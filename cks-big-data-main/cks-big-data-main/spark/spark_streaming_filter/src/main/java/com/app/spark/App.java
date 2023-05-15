
package com.app.spark;


import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class App   {
    public static void main(String[] args) throws Exception {
        String file = "iot-spark.properties";
        Spark app = new Spark(file);
        app.run();
       
    }
}
