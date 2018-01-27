/**
 * 
 */
package stream.vispar.server.core.entities;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.mock;
import java.util.HashMap;
import java.util.Locale;
import org.junit.Test;

import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.AttributeType;
import stream.vispar.server.core.ServerConfig;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.logger.ILogger;

/**
 * Tests for {@link Event}.
 * 
 * @author Micha Hanselmann
 */
public class EventTest {

    /**
     * Test method for {@link Event#Event(long, java.util.Map, Sensor)}.
     */
    @Test
    public void testEvent() {
        ServerConfig config = new ServerConfig(8888, 8889, Locale.US, mock(ILogger.class), "localhost", 
                "./src/test/resources/sensors");
        ServerInstance instance = new ServerInstance(config);
        instance.getSensorCtrl().registerSensors();
        
        Sensor sensor = instance.getSensorCtrl().getByName("temp1");
        HashMap<Attribute, String> data = new HashMap<>();
        Attribute attr = new Attribute("attr", "", AttributeType.INTEGER);
        data.put(attr, "32");
        Event event = new Event(123, data, sensor);
        
        assertThat(event.getData(), hasEntry(attr, "32"));
        assertThat(event.getTimestamp(), equalTo(123L));
        assertThat(event.getSensor(), sameInstance(sensor));
    }

    /**
     * Test method for {@link Event#toString()}.
     */
    @Test
    public void testToString() {
        ServerConfig config = new ServerConfig(8888, 8889, Locale.US, mock(ILogger.class), "localhost", 
                "./src/test/resources/sensors");
        ServerInstance instance = new ServerInstance(config);
        instance.getSensorCtrl().registerSensors();
        
        Sensor sensor = instance.getSensorCtrl().getByName("temp1");
        HashMap<Attribute, String> data = new HashMap<>();
        Attribute attr = new Attribute("attr", "", AttributeType.INTEGER);
        data.put(attr, "32");
        Event event = new Event(123, data, sensor);
        
        assertThat(event.toString(), containsString("data={attr: int (origin: )=32}"));
        assertThat(event.toString(), containsString("sensor=temp1"));
        assertThat(event.toString(), containsString("timestamp=123"));
    }
}
