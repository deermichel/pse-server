/**
 * 
 */
package stream.vispar.server.core.entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.Test;
import stream.vispar.model.Pattern;
import stream.vispar.server.core.ServerConfig;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.engine.IEngine;
import stream.vispar.server.logger.ILogger;

/**
 * Tests for {@link Simulation}.
 * 
 * @author Micha Hanselmann
 */
public class SimulationTest {

    /**
     * Test method for {@link Simulation#Simulation(java.lang.String)}.
     */
    @Test
    public void testSimulation() {
        Simulation sim = new Simulation("./src/test/resources/simulations/temp.sim");
        assertThat(sim, notNullValue());
    }

    /**
     * Test method for {@link Simulation#Simulation(java.lang.String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSimulationInvalidFile() {
        new Simulation(".");
    }

    /**
     * Test method for {@link Simulation#simulate(ServerInstance)}.
     */
    @Test
    public void testSimulate() {
        Collection<Event> events = new ArrayList<>();
        IEngine fakeEngine = new IEngine() {
            @Override
            public void undeploy(Pattern pattern) { }
            @Override
            public void stop() { }
            @Override
            public void start() { }
            @Override
            public void sendEvent(Event event) { 
                System.out.println(event);
                events.add(event);
            }
            @Override
            public void deploy(Pattern pattern) { }
        };
        
        ServerConfig config = new ServerConfig(8888, 8889, Locale.US, mock(ILogger.class), "localhost", 
                "./src/test/resources/sensors");
        ServerInstance instance = new ServerInstance(config);
        instance.getSensorCtrl().registerSensors();
        
        ServerInstance mockInstance = spy(instance);
        when(mockInstance.getEngine()).thenReturn(fakeEngine);
        
        Simulation sim = new Simulation("./src/test/resources/simulations/temp.sim");
        sim.simulate(mockInstance);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertThat(events.size(), equalTo(6));
        for (Event e : events) {
            assertThat(e.getSensor().getName(), equalTo("temp1"));
        }
    }
}
