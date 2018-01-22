package stream.vispar.server.core.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.AttributeType;
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
        // for loop with going deeper for . and []
        return new Event("STAMPI", new HashMap<>(), this);
    }
    
    /**
     * Parses the {@link Sensor} to a {@link SensorNode} which can be used on the client.
     * 
     * @return
     *          the {@link SensorNode}.
     */
    public SensorNode getSensorNode() {
        return new SensorNode("", new Point(0, 0), name, description, 
                new Operand(new Attribute("todo", "", AttributeType.STRING)));
    }
}
