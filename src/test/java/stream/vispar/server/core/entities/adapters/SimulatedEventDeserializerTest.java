package stream.vispar.server.core.entities.adapters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Before;
import org.junit.Test;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.server.core.entities.SimulatedEvent;

/**
 * Tests for {@link SimulatedEventDeserializer}.
 * 
 * @author Micha Hanselmann
 */
public class SimulatedEventDeserializerTest {
    
    private IJsonConverter conv;
    
    /**
     * Create and setup converter.
     */
    @Before
    public void setup() {
        conv = new GsonConverter().registerTypeAdapter(SimulatedEvent.class, new SimulatedEventDeserializer());
    }

    /**
     * Test parsing of valid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testValidParsing() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"0\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        String s = conv.fromJson(conv.fromString(raw), SimulatedEvent.class).toString();
        assertThat(s, containsString("\"value\":{\"fixed\":\"23\"}"));
        assertThat(s, containsString("\"room\":{\"random\":[\"Kitchen\",\"WC\",\"Floor\"]}"));
        assertThat(s, containsString("\"humidity\":{\"drange\":[0.1,0.95]}"));
        assertThat(s, containsString("\"status\":{\"range\":[0,10]}}"));
        assertThat(s, containsString("repeat=repeat(4, 100)"));
        assertThat(s, containsString("sensor=temp1"));
        assertThat(s, containsString("time=10"));
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidTime() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"0\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidJSON() throws JsonException {
        String raw = "a";
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidSensor() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"te-mp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"0\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidDataKey() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"va.lue\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"0\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidDataType() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"variable\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"0\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidDataFixed() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": {} },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"0\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidDataRangeSize() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"0\", \"10\", \"20\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidDataDrangeSize() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\", \"0.12\" ] },\n"
                + "        \"status\": { \"range\": [ \"0\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidDataRangeMin() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"2.0\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidDataRangeMax() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"2\", \"a\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidDataDrangeMin() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"2\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidDataDrangeMax() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": [ \"Kitchen\", \"WC\", \"Floor\" ] },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"--0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"2\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
    
    /**
     * Test parsing of invalid simulated event.
     * 
     * @throws JsonException bad json.
     */
    @Test(expected = JsonParseException.class)
    public void testInvalidDataRandom() throws JsonException {
        String raw = "{\n"
                + "    \"time\": \"10\",\n"
                + "    \"sensor\": \"temp1\",\n"
                + "    \"repeat\": { \"count\": \"4\", \"interval\": \"100\" },\n"
                + "    \"data\": {\n"
                + "        \"value\": { \"fixed\": \"23\" },\n"
                + "        \"room\": { \"random\": \"Kitchen\" },\n"
                + "        \"humidity\": { \"drange\": [ \"0.10\", \"0.95\" ] },\n"
                + "        \"status\": { \"range\": [ \"2\", \"10\" ] }\n"
                + "    }\n"
                + "}";
        
        conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }
}
