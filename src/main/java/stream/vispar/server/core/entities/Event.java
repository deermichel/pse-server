package stream.vispar.server.core.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import foreign.Attribute;
import stream.vispar.server.engine.IEngine;

/**
 * Event of a {@link Sensor} used in an {@link IEngine}.
 * 
 * @author Micha Hanselmann
 */
public class Event {
    
    /**
     * Timestamp of the event (ISO-6801).
     */
    private final String timestamp;
    
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
     *          the timestamp of the event (ISO-6801).
     * @param data
     *          the attributes and their values of the event.
     * @param sensor
     *          the sensor responsible for the event.
     */
    public Event(String timestamp, Map<Attribute, String> data, Sensor sensor) {
        this.timestamp = timestamp;
        this.data = new HashMap<>(Objects.requireNonNull(data));
        this.sensor = Objects.requireNonNull(sensor);
    }
    
    /**
     * Returns the data of the event.
     * 
     * @return
     *          map of attributes and their values.
     */
    public Map<Attribute, String> getData() {
        return new HashMap<>(data);
    }
}
