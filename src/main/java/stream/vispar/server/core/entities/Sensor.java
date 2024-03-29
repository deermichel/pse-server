package stream.vispar.server.core.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.Operand;
import stream.vispar.model.nodes.Point;
import stream.vispar.model.nodes.inputs.SensorNode;
import stream.vispar.server.engine.IEngine;

/**
 * Provides events for an {@link IEngine}.
 * 
 * @author Micha Hanselmann
 */
public final class Sensor {

    /**
     * Name of the sensor.
     */
    private final String name;
    
    /**
     * Description of the sensor.
     */
    private final String description;
    
    /**
     * Endpoint of the sensor.
     */
    private final String endpoint;
    
    /**
     * Timestamp source of the sensor.
     */
    private final String timestamp;
    
    /**
     * Attribute mappings of the sensor.
     */
    private final Map<String, Attribute> attributes;
    
    
    /**
     * Constructs a new {@link Sensor}. Called implicitly by json conversion.
     */
    private Sensor() {
        this.name = "";
        this.description = "";
        this.endpoint = "";
        this.timestamp = "";
        this.attributes = new HashMap<>();
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
    public String getDescription() {
        return description;
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
     * Returns the attributes of the sensor.
     * 
     * @return
     *          the attributes.
     */
    public Collection<Attribute> getAttributes() {
        return new ArrayList<>(attributes.values());
    }
    
    /**
     * Parses an event from raw data based on the sensors attribute mappings.
     * 
     * @param data 
     *          raw {@link IJsonElement} sent from the sensor.
     * @return
     *          the parsed {@link Event}.
     * @throws IllegalArgumentException
     *          if the data could not be parsed.
     */
    public Event parseEvent(IJsonElement data) {
        
        // set timestamp (either from system or from sensor)
        long timestampMillis = System.currentTimeMillis(); // default
        if (timestamp != null && !timestamp.isEmpty()) {
            try {
                timestampMillis = Long.valueOf(getValueRecursively(data, timestamp));
            } catch (JsonException | NullPointerException | NumberFormatException e) {
                throw new IllegalArgumentException("Sensor data for timestamp does not match configuration: " 
                        + e.toString());
            }
        }
        
        // parse attribute values
        Map<Attribute, String> values = new HashMap<>();
        for (Entry<String, Attribute> attr : attributes.entrySet()) {
            try {
                values.put(attr.getValue(), getValueRecursively(data, attr.getKey()));
            } catch (JsonException | NullPointerException e) {
                throw new IllegalArgumentException("Sensor data for attribute '" + attr.getValue().getName() 
                        + "' does not match configuration: " + e.toString());
            }
        }
        
        // return event
        return new Event(timestampMillis, values, this);
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Sensor
                && name.equals(((Sensor) obj).name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    /**
     * Parses the {@link Sensor} to a {@link SensorNode} which can be used on the client.
     * 
     * @return
     *          the {@link SensorNode}.
     */
    public SensorNode getSensorNode() {
        Attribute[] attrArray = attributes.values().stream().toArray(Attribute[]::new);
        return new SensorNode("", new Point(0, 0), name, description, new Operand(attrArray));
    }
    
    /**
     * Returns the value of a key in a {@link IJsonElement}. It supports nested objects by recursively
     * traverse the json until the bottom-level key with its value is found.
     * 
     * @param json
     *          the {@link IJsonElement} to be traversed.
     * @param key
     *          the key to be searched for.
     * @return
     *          the value associated with the key.
     * @throws JsonException
     *          if key is invalid for given json.
     */
    private String getValueRecursively(IJsonElement json, String key) throws JsonException {
        
        // base case -> key is on root level of json
        if (!key.contains(".")) {
            // array or object?
            Matcher m = Pattern.compile(".*\\[(.*)\\]").matcher(key);
            if (m.matches()) {
                int index = Integer.parseInt(m.group(1));
                String arrayKey = key.substring(0, key.indexOf('['));
                return json.getAsJsonObject().get(arrayKey).getAsJsonArray()
                        .get(index).getAsJsonPrimitive().getAsString();
            } else {
                return json.getAsJsonObject().getAsJsonPrimitive(key).getAsString();
            }
        }
        
        // else -> go a level deeper
        String levelKey = key.split("\\.")[0];
        String newKey = key.substring(levelKey.length() + 1);
        
        // array or object?
        Matcher m = Pattern.compile(".*\\[(.*)\\]").matcher(levelKey);
        if (m.matches()) {
            int index = Integer.parseInt(m.group(1));
            String arrayKey = levelKey.substring(0, levelKey.indexOf('['));
            return getValueRecursively(json.getAsJsonObject().get(arrayKey).getAsJsonArray()
                    .get(index), newKey);
        } else {
            return getValueRecursively(json.getAsJsonObject().get(levelKey), newKey);
        }
    }
}
