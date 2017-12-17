package stream.vispar.server.core.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import foreign.Attribute;
import foreign.Json;
import stream.vispar.server.engine.IEngine;

/**
 * Provides events for an {@link IEngine}.
 * 
 * @author Micha Hanselmann
 */
public class Sensor {

    /**
     * Name of the sensor.
     */
    private final String name;
    
    /**
     * Description of the sensor.
     */
    private final String desc;
    
    /**
     * Endpoint of the sensor.
     */
    private final String endpoint;
    
    /**
     * Attribute mappings of the sensor.
     */
    private final Map<String, Attribute> attrMap;
    
    
    /**
     * Constructs a new {@link Sensor}.
     * 
     * @param configFile
     *          the path to the configuration file for the sensor.
     * @throws IllegalArgumentException
     *          if the config file does not exist or is invalid.
     */
    public Sensor(String configFile) {
        this.name = "";
        this.desc = "";
        this.endpoint = "";
        this.attrMap = new HashMap<>();
    }
    
    /**
     * Returns the name of the sensor.
     * 
     * @return
     *          the name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the description of the sensor.
     * 
     * @return
     *          the description.
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * Returns the endpoint of the sensor.
     * 
     * @return
     *          the endpoint.
     */
    public String getEndpoint() {
        return endpoint;
    }
    
    /**
     * Returns the available attributes of the sensor.
     * 
     * @return
     *          collection of the {@link Attribute attributes}.
     */
    public Collection<Attribute> getAttributes() {
        return new ArrayList<>(attrMap.values());
    }
    
    /**
     * Parses an event from raw data based on the sensors attribute mappings.
     * 
     * @param data 
     *          raw {@link Json} data sent from the sensor.
     * @return
     *          the parsed {@link Event}.
     * @throws IllegalArgumentException
     *          if the data could not be parsed.
     */
    public Event parseEvent(Json data) {
        return null;
    }
}
