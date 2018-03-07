package stream.vispar.server.core.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.model.nodes.Attribute;
import stream.vispar.server.core.ServerInstance;

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
        private int count = 0;
        
        /**
         * Delay between repetitions.
         */
        private int interval = 0;
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
        private int[] range;
        
        /**
         * Double range.
         */
        private double[] drange;
        
        /**
         * Random value.
         */
        private String[] random;
    }
    
    
    /**
     * When event should be sent after simulation has been started.
     */
    private final int time;
    
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
        this.time = 0;
        this.repeat = new RepeatConfig();
        this.sensor = "";
        this.data = new HashMap<>();
    }
    
    /**
     * Returns the time when event should be sent after simulation has been started.
     * 
     * @return
     *          the time delay.
     */
    public int getTimeDelay() {
        return this.time;
    }
    
    /**
     * Returns the number of repetitions.
     * 
     * @return
     *          the repeat count.
     */
    public int getRepeatCount() {
        return this.repeat.count;
    }
    
    /**
     * Returns the delay between repetitions.
     * 
     * @return
     *          the repeat interval.
     */
    public int getRepeatInterval() {
        return this.repeat.interval;
    }
    
    /**
     * Constructs a real {@link Event} using the simulation data.
     * 
     * @param instance
     *          the target {@link ServerInstance} for the event.
     * @return
     *          the {@link Event}.
     * @throws IllegalStateException
     *          if the data could not be mapped to the sensors (not registered, bad format).
     */
    public Event createEvent(ServerInstance instance) {
        
        // get sensor and its attributes
        Sensor eventSensor = instance.getSensorCtrl().getByName(this.sensor);
        if (eventSensor == null) {
            throw new IllegalStateException("Sensor '" + this.sensor + "' not registered");
        }
        Collection<Attribute> attributes = eventSensor.getAttributes();
        
        // map data
        Map<Attribute, String> simulatedData = new HashMap<>();
        for (Attribute attr : attributes) {
            
            // get matching value
            AttributeValue value = data.get(attr.getName());
            if (value == null) {
                throw new IllegalStateException("No simulation data for attribute '" + attr.getName() + "'");
            }
            
            // derive attribute type
            String val = "";
            if (value.fixed != null) { // fixed static value
                val = value.fixed;
            } else if (value.range != null) { // integer range
                int random = ThreadLocalRandom.current().nextInt(value.range[0], value.range[1] + 1);
                val = String.valueOf(random);
            } else if (value.drange != null) { // double range
                double random = ThreadLocalRandom.current().nextDouble(value.drange[0], value.drange[1]);
                val = String.valueOf(random);
            } else if (value.random != null) { // random
                int random = ThreadLocalRandom.current().nextInt(value.random.length);
                val = value.random[random];
            } else { // shouldn't happen due to prior file validation
                throw new IllegalStateException("No simulation data for attribute '" + attr.getName() + "'");
            }
            simulatedData.put(attr, val);
        }
        
        // create event
        return new Event(System.currentTimeMillis(), simulatedData, eventSensor);
    }
    
    @Override
    public String toString() {
        Map<String, String> output = new HashMap<>();
        output.put("time", String.valueOf(time));
        output.put("sensor", sensor);
        output.put("repeat", String.format("repeat(%d, %d)", repeat.count, repeat.interval));
        output.put("data", new GsonConverter().toJson(data).toString());
        return output.toString();
    }
}
