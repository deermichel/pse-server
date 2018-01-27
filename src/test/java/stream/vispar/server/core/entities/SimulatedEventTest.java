/**
 * 
 */
package stream.vispar.server.core.entities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.server.core.ServerConfig;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.logger.ILogger;

/**
 * Tests for {@link SimulatedEvent}.
 * 
 * @author Micha Hanselmann
 */
public class SimulatedEventTest {
    
    private SimulatedEvent event;
    

    /**
     * Parse demo event
     * 
     * @throws Exception bad format.
     */
    @Before
    public void setUp() throws Exception {
        String raw = "{\n" 
                + "        \"time\": \"1230\",\n" 
                + "        \"sensor\": \"temp1\",\n" 
                + "        \"repeat\": { \"count\": \"4\", \"interval\": \"500\" },\n"
                + "        \"data\": {\n" 
                + "            \"value\": { \"fixed\": \"23\" },\n" 
                + "            \"room\": { \"fixed\": \"Kinderzimmer\" }\n" 
                + "        }\n" 
                + "    }";
        
        IJsonConverter conv = new GsonConverter();
        event = conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
    }

    /**
     * Test method for {@link SimulatedEvent#getTimeDelay()}.
     */
    @Test
    public void testGetTimeDelay() {
        assertThat(event.getTimeDelay(), equalTo(1230));
    }

    /**
     * Test method for {@link SimulatedEvent#getRepeatCount()}.
     */
    @Test
    public void testGetRepeatCount() {
        assertThat(event.getRepeatCount(), equalTo(4));
    }

    /**
     * Test method for {@link SimulatedEvent#getRepeatInterval()}.
     */
    @Test
    public void testGetRepeatInterval() {
        assertThat(event.getRepeatInterval(), equalTo(500));
    }

    /**
     * Test method for {@link SimulatedEvent#createEvent(ServerInstance)}.
     */
    @Test
    public void testCreateEvent() {
        ServerConfig config = new ServerConfig(8888, 8889, Locale.US, mock(ILogger.class), "localhost", 
                "./src/test/resources/sensors");
        ServerInstance instance = new ServerInstance(config);
        instance.getSensorCtrl().registerSensors();
        
        String realEvent = event.createEvent(instance).toString();
        assertThat(realEvent, containsString("value: int (origin: )=23"));
        assertThat(realEvent, containsString("room: String (origin: )=Kinderzimmer"));
        assertThat(realEvent, containsString("sensor=temp1"));
        assertThat(realEvent, containsString("timestamp="));
    }

    /**
     * Test method for {@link SimulatedEvent#toString()}.
     */
    @Test
    public void testToString() {
        String s = event.toString();
        assertThat(s, containsString("\"value\":{\"fixed\":\"23\"}"));
        assertThat(s, containsString("\"room\":{\"fixed\":\"Kinderzimmer\"}"));
        assertThat(s, containsString("repeat=repeat(4, 500)"));
        assertThat(s, containsString("sensor=temp1"));
        assertThat(s, containsString("time=1230"));
    }

    /**
     * Test method for {@link SimulatedEvent#toString()}.
     * 
     * @throws JsonException bad format.
     */
    @Test
    public void testAttributeTypes() throws JsonException {
        String raw = "{\n"
               + "        \"time\": \"2000\",\n"
               + "        \"sensor\": \"temp1\",\n"
               + "        \"repeat\": { \"count\": \"4\", \"interval\": \"500\" },\n"
               + "        \"data\": {\n"
               + "            \"humidity\": { \"drange\": [ \"2.3\", \"9\" ] },\n"
               + "            \"value\": { \"range\": [ \"17\", \"24\" ] },\n"
               + "            \"room\": { \"random\": [ \"Kinderzimmer\",\"\", \"Küche\" ] }\n"
               + "        }\n"
               + "    }";
        
        IJsonConverter conv = new GsonConverter();
        SimulatedEvent event = conv.fromJson(conv.fromString(raw), SimulatedEvent.class);
        String s = event.toString();
        assertThat(s, containsString("\"humidity\":{\"drange\":[2.3,9.0]}"));
        assertThat(s, containsString("\"value\":{\"range\":[17,24]}"));
        assertThat(s, containsString("\"room\":{\"random\":[\"Kinderzimmer\",\"\",\"Küche\"]}}"));
        
        ServerConfig config = new ServerConfig(8888, 8889, Locale.US, mock(ILogger.class), "localhost", 
                "./src/test/resources/sensors");
        ServerInstance instance = new ServerInstance(config);
        instance.getSensorCtrl().registerSensors();
        event.createEvent(instance);
    }
}
