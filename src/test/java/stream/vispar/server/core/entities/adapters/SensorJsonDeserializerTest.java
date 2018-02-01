package stream.vispar.server.core.entities.adapters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.AttributeType;
import stream.vispar.server.core.entities.Sensor;

/**
 * Tests for {@link SensorJsonDeserializer}.
 * 
 * @author Micha Hanselmann
 */
public class SensorJsonDeserializerTest {
    
    private IJsonConverter conv;
    
    /**
     * Create and setup converter.
     */
    @Before
    public void setup() {
        conv = new GsonConverter().registerTypeAdapter(Sensor.class, new SensorJsonDeserializer());
    }
    
    /**
     * Test parsing of valid sensor config.
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testValidParsing() throws JsonException {
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
        
        Sensor sensor = conv.fromJson(conv.fromString(raw), Sensor.class);
        assertThat(sensor.getName(), equalTo("temp1"));
        assertThat(sensor.getEndpoint(), equalTo("temp1"));
        assertThat(sensor.getDescription(), equalTo("Temperature sensor"));
        assertThat(sensor.getAttributes(), containsInAnyOrder(
                new Attribute("value", "", AttributeType.INTEGER),
                new Attribute("room", "", AttributeType.STRING)));
    }
    
    /**
     * Test parsing of invalid sensor config.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidName() throws JsonException {
        String raw = "{\n"
                + "    \"name\": \"te-mp1\",\n"
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
        
        conv.fromJson(conv.fromString(raw), Sensor.class);
    }
    
    /**
     * Test parsing of invalid sensor config.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidEndpoint() throws JsonException {
        String raw = "{\n"
                + "    \"name\": \"temp1\",\n"
                + "    \"description\": \"Temperature sensor\",\n"
                + "    \"endpoint\": \"\",\n"
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
        
        conv.fromJson(conv.fromString(raw), Sensor.class);
    }
    
    /**
     * Test parsing of invalid sensor config.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidTimestampKey() throws JsonException {
        String raw = "{\n"
                + "    \"name\": \"temp1\",\n"
                + "    \"description\": \"Temperature sensor\",\n"
                + "    \"endpoint\": \"temp1\",\n"
                + "    \"timestamp\": \"[[\",\n"
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
        
        conv.fromJson(conv.fromString(raw), Sensor.class);
    }
    
    /**
     * Test parsing of invalid sensor config.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidJSON() throws JsonException {
        String raw = "a";
        conv.fromJson(conv.fromString(raw), Sensor.class);
    }
    
    /**
     * Test parsing of invalid sensor config.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidAttributeSourceKey() throws JsonException {
        String raw = "{\n"
                + "    \"name\": \"temp1\",\n"
                + "    \"description\": \"Temperature sensor\",\n"
                + "    \"endpoint\": \"temp1\",\n"
                + "    \"attributes\": {\n"
                + "        \"value\": {\n"
                + "            \"name\": \"value\",\n"
                + "            \"type\": \"INTEGER\"\n"
                + "        },\n"
                + "        \"other[].room\": {\n"
                + "            \"name\": \"room\",\n"
                + "            \"type\": \"STRING\"\n"
                + "        }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), Sensor.class);
    }
    
    /**
     * Test parsing of invalid sensor config.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidAttributeName() throws JsonException {
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
                + "            \"name\": \"ro-om\",\n"
                + "            \"type\": \"STRING\"\n"
                + "        }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), Sensor.class);
    }
    
    /**
     * Test parsing of invalid sensor config.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidAttributeType() throws JsonException {
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
                + "            \"type\": \"FLOAT\"\n"
                + "        }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), Sensor.class);
    }
}
