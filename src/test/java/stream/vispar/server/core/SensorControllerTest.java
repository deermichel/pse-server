/**
 * 
 */
package stream.vispar.server.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.AttributeType;
import stream.vispar.server.core.entities.Sensor;

/**
 * Tests for {@link SensorController}.
 * 
 * @author Micha Hanselmann
 */
public class SensorControllerTest {

    /**
     * Test method for {@link SensorController(ServerInstance, java.lang.String)}.
     */
    @Test(expected = NullPointerException.class)
    public void testSensorControllerNull1() {
        new SensorController(null, "abc");
    }

    /**
     * Test method for {@link SensorController(ServerInstance, java.lang.String)}.
     */
    @Test(expected = NullPointerException.class)
    public void testSensorControllerNull2() {
        new SensorController(mock(ServerInstance.class), null);
    }

    /**
     * Test method for {@link registerSensors()}.
     */
    @Test
    public void testRegisterSensors() {
        ServerInstance inst = new ServerInstanceMock();
        
        SensorController ctrl = new SensorController(inst, "./src/test/resources/sensors");
        assertThat(ctrl.getAll().size(), equalTo(0));
        ctrl.registerSensors();
        assertThat(ctrl.getAll().size(), equalTo(1));
        ctrl.registerSensors(); // same sensor shouldnt be registered twice
        assertThat(ctrl.getAll().size(), equalTo(1));
        
        Sensor s = ctrl.getAll().iterator().next();
        assertThat(s.getName(), equalTo("temp1"));
        assertThat(s.getEndpoint(), equalTo("temp1"));
        assertThat(s.getAttributes(), containsInAnyOrder(
                new Attribute("value", "", AttributeType.INTEGER), 
                new Attribute("room", "", AttributeType.STRING)));
    }

    /**
     * Test method for {@link getAll()}.
     */
    @Test
    public void testGetAll() {
        ServerInstance inst = new ServerInstanceMock();
        
        SensorController ctrl = new SensorController(inst, "./src/test/resources/sensors");
        assertThat(ctrl.getAll().size(), equalTo(0));
        ctrl.registerSensors();
        assertThat(ctrl.getAll().size(), equalTo(1));
    }

    /**
     * Test method for {@link getByName(java.lang.String)}.
     */
    @Test
    public void testGetByName() {
        ServerInstance inst = new ServerInstanceMock();
        
        SensorController ctrl = new SensorController(inst, "./src/test/resources/sensors");
        assertThat(ctrl.getAll().size(), equalTo(0));
        ctrl.registerSensors();
        assertThat(ctrl.getAll().size(), equalTo(1));
        
        Sensor s = ctrl.getByName("temp1");
        assertThat(s.getName(), equalTo("temp1"));
        assertThat(s.getEndpoint(), equalTo("temp1"));
        assertThat(s.getAttributes(), containsInAnyOrder(
                new Attribute("value", "", AttributeType.INTEGER), 
                new Attribute("room", "", AttributeType.STRING)));
    }
}
