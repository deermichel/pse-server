/**
 * 
 */
package stream.vispar.server.core.entities;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonArray;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonObject;
import stream.vispar.jsonconverter.types.IJsonArray;
import stream.vispar.jsonconverter.types.IJsonObject;
import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.AttributeType;
import stream.vispar.model.nodes.inputs.SensorNode;

/**
 * Tests for {@link Sensor}.
 * 
 * @author Micha Hanselmann
 */
public class SensorTest {
    
    private Sensor sensor;
    

    /**
     * Parse demo sensor
     * 
     * @throws JsonException bad format
     */
    @Before
    public void setUp() throws JsonException {
        String raw = "{\n"
               + "    \"name\": \"temp1\",\n"
               + "    \"timestamp\": \"other.time[0]\",\n"
               + "    \"description\": \"Temperature sensor\",\n"
               + "    \"endpoint\": \"temp1\",\n"
               + "    \"attributes\": {\n"
               + "        \"value\": {\n"
               + "            \"name\": \"value\",\n"
               + "            \"type\": \"INTEGER\"\n"
               + "        },\n"
               + "        \"other.room\": {\n"
               + "            \"name\": \"room\",\n"
               + "            \"type\": \"STRING\"\n"
               + "        }\n"
               + "    }\n"
               + "}";
        
        IJsonConverter conv = new GsonConverter();
        sensor = conv.fromJson(conv.fromString(raw), Sensor.class);
    }

    /**
     * Test method for {@link Sensor#hashCode()}.
     */
    @Test
    public void testHashCode() {
        assertThat(sensor.hashCode(), equalTo("temp1".hashCode()));
    }

    /**
     * Test method for {@link Sensor#getName()}.
     */
    @Test
    public void testGetName() {
        assertThat(sensor.getName(), equalTo("temp1"));
    }

    /**
     * Test method for {@link Sensor#getDescription()}.
     */
    @Test
    public void testGetDescription() {
        assertThat(sensor.getDescription(), equalTo("Temperature sensor"));
    }

    /**
     * Test method for {@link Sensor#getEndpoint()}.
     */
    @Test
    public void testGetEndpoint() {
        assertThat(sensor.getEndpoint(), equalTo("temp1"));
    }

    /**
     * Test method for {@link Sensor#getAttributes()}.
     */
    @Test
    public void testGetAttributes() {
        Collection<Attribute> attr = sensor.getAttributes();
        assertThat(attr, contains(new Attribute("value", "", AttributeType.INTEGER),
                new Attribute("room", "", AttributeType.STRING)));
    }

    /**
     * Test method for {@link Sensor#parseEvent(stream.vispar.jsonconverter.types.IJsonElement)}.
     */
    @Test
    public void testParseEvent() {
        IJsonObject data = new GsonJsonObject();
        IJsonObject nestedData = new GsonJsonObject();
        IJsonArray array = new GsonJsonArray();
        data.add("value", "23");
        data.add("time", "8080");
        nestedData.add("room", "Kitchen");
        array.add("8080");
        nestedData.add("time", array);
        data.add("other", nestedData);
        
        Event event = sensor.parseEvent(data);
        String s = event.toString();
        assertThat(s, containsString("value: int (origin: )=23"));
        assertThat(s, containsString("room: String (origin: )=Kitchen"));
        assertThat(s, containsString("sensor=temp1"));
        assertThat(s, containsString("timestamp=8080"));
    }

    /**
     * Test method for {@link Sensor#equals(java.lang.Object)}.
     * 
     * @throws JsonException bad format
     */
    @Test
    public void testEqualsObject() throws JsonException {
        assertThat(sensor, equalTo(sensor));
        assertThat(sensor, not(equalTo(null)));
        
        String raw = "{\n"
                + "    \"name\": \"temp1\",\n"
                + "    \"description\": \"Temperature sensor\",\n"
                + "    \"endpoint\": \"temp1\",\n"
                + "    \"attributes\": {\n"
                + "        \"value\": {\n"
                + "            \"name\": \"value\",\n"
                + "            \"type\": \"INTEGER\"\n"
                + "        },\n"
                + "        \"other.room\": {\n"
                + "            \"name\": \"room\",\n"
                + "            \"type\": \"STRING\"\n"
                + "        }\n"
                + "    }\n"
                + "}";
         
         IJsonConverter conv = new GsonConverter();
         Sensor s2 = conv.fromJson(conv.fromString(raw), Sensor.class);
         assertThat(sensor, equalTo(s2));
    }

    /**
     * Test method for {@link Sensor#getSensorNode()}.
     */
    @Test
    public void testGetSensorNode() {
        SensorNode node = sensor.getSensorNode();
        assertThat(node.getSensorName(), equalTo(sensor.getName()));
        assertThat(node.getSensorDescription(), equalTo(sensor.getDescription()));
    }
}
