/**
 * 
 */
package stream.vispar.server.engine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.AttributeType;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.ServerInstanceMock;
import stream.vispar.server.core.entities.Event;
import stream.vispar.server.core.entities.Sensor;

/**
 * Tests for {@link EventAction}.
 * 
 * @author Micha Hanselmann
 */
public class EventActionTest {

    /**
     * Test method for {@link EventAction(IEngine, Event)}.
     */
    @Test(expected = NullPointerException.class)
    public void testEventActionNull1() {
        new EventAction(null, mock(Event.class));
    }

    /**
     * Test method for {@link EventAction(IEngine, Event)}.
     */
    @Test(expected = NullPointerException.class)
    public void testEventActionNull2() {
        new EventAction(mock(IEngine.class), null);
    }

    /**
     * Test method for {@link execute()}.
     */
    @Test
    public void testExecute() {
        // prepare and mock
        ServerInstance inst = new ServerInstanceMock();
        inst.getSensorCtrl().registerSensors();
        Sensor sensor = inst.getSensorCtrl().getByName("temp1");
        Map<Attribute, String> data = new HashMap<>();
        data.put(new Attribute("value", "", AttributeType.INTEGER), "34");
        IEngine engine = mock(IEngine.class);
        
        Event event = new Event(74362, data, sensor);
        new EventAction(engine, event).execute();
        
        verify(engine, times(1)).sendEvent(event);
    }

    /**
     * Test method for {@link toString()}.
     */
    @Test
    public void testToString() {
        // prepare and mock
        ServerInstance inst = new ServerInstanceMock();
        inst.getSensorCtrl().registerSensors();
        Sensor sensor = inst.getSensorCtrl().getByName("temp1");
        Map<Attribute, String> data = new HashMap<>();
        data.put(new Attribute("value", "", AttributeType.INTEGER), "34");
        
        Event event = new Event(74362, data, sensor);
        EventAction ea = new EventAction(inst.getEngine(), event);
        String result = ea.toString();
        
        assertThat(result, containsString("event={data={value: int (origin: )=34}"));
        assertThat(result, containsString("sensor=temp1"));
        assertThat(result, containsString("timestamp=74362"));
    }
}
