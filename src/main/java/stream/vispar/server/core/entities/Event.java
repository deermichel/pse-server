package stream.vispar.server.core.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import stream.vispar.model.nodes.Attribute;
import stream.vispar.server.engine.IEngine;

/**
 * Event of a {@link Sensor} used in an {@link IEngine}.
 * 
 * @author Micha Hanselmann
 */
public class Event {

    /**
     * Timestamp of the event (in milliseconds from 1970).
     */
    private final long timestamp;

    /**
     * Attributes and their values of the event.
     */
    private final Map<Attribute, String> data;

    /**
     * Sensor responsible for the event.
     */
    private final Sensor sensor;
    

    /**
     * Constructs a new {@link Event}.
     * 
     * @param timestamp
     *            the timestamp of the event (in milliseconds from 1970).
     * @param data
     *            the attributes and their values of the event.
     * @param sensor
     *            the sensor responsible for the event.
     */
    public Event(long timestamp, Map<Attribute, String> data, Sensor sensor) {
        this.timestamp = timestamp;
        this.data = new HashMap<>(Objects.requireNonNull(data));
        this.sensor = Objects.requireNonNull(sensor);
    }

    /**
     * Returns the data of the event.
     * 
     * @return map of attributes and their values.
     */
    public Map<Attribute, String> getData() {
        return new HashMap<>(data);
    }
    
    /**
     * Returns the timestamp of the event (in milliseconds from 1970).
     * 
     * @return
     *          the timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the sensor of the event.
     * 
     * @return the sensor of the event.
     */
    public Sensor getSensor() {
        return this.sensor;
    }

    @Override
    public String toString() {
        Map<String, String> output = new HashMap<>();
        output.put("sensor", sensor.getName());
        output.put("timestamp", String.valueOf(timestamp));
        output.put("data", data.toString());
        return output.toString();
    }
}
