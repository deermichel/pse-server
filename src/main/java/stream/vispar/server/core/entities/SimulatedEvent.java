package stream.vispar.server.core.entities;

import java.util.HashMap;
import java.util.Map;

import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonNull;
import stream.vispar.jsonconverter.types.IJsonElement;

/**
 * Events used in a {@link Simulation} to simulate a real {@link Event}.
 * 
 * @author Micha Hanselmann
 */
public final class SimulatedEvent {
    
    /**
     * Configuration for repeated execution.
     */
    private class RepeatConfig {
        
        /**
         * Number of repetitions.
         */
        private int count;
        
        /**
         * Delay between repetitions.
         */
        private int interval;
    }
    
    /**
     * Attribute values.
     */
    private class AttributeValue {
        
        /**
         * Fixed static value.
         */
        private String fixed;
        
        /**
         * (Integer) Range.
         */
        private String[] range;
        
        /**
         * Double range.
         */
        private String[] drange;
        
        /**
         * Random value.
         */
        private String[] random;
    }
    
    
    /**
     * When event should be sent after simulation has been started.
     */
    private final int timestamp;
    
    /**
     * Repeated execution.
     */
    private final RepeatConfig repeat;
    
    /**
     * Sensor simulated by this event.
     */
    private final String sensor;
    
    /**
     * Data of the event.
     */
    private final Map<String, AttributeValue> data;

    
    /**
     * Constructs a new {@link SimulatedEvent}. Called implicitly by json conversion.
     */
    private SimulatedEvent() {
        this.timestamp = 0;
        this.repeat = new RepeatConfig();
        this.sensor = "";
        this.data = new HashMap<>();
    }
    
    @Override
    public String toString() {
        Map<String, String> output = new HashMap<>();
        output.put("timestamp", String.valueOf(timestamp));
        output.put("sensor", sensor);
        output.put("repeat", String.format("repeat(%d, %d)", repeat.count, repeat.interval));
        output.put("data", new GsonConverter().toJson(data).toString());
        return output.toString();
    }
}
