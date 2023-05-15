package com.app.spark.util;


import com.app.spark.model.Tracking;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Class to deserialize JSON string to Tracking java object
 *
 * @author abaghel
 *
 */
public class TrackingDeserializer implements Deserializer<Tracking> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public Tracking fromBytes(byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, Tracking.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public Tracking deserialize(String s, byte[] bytes) {
        return fromBytes((byte[]) bytes);
    }

    @Override
    public void close() {

    }
}






























