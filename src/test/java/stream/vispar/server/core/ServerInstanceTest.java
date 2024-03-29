/**
 * 
 */
package stream.vispar.server.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;

import java.util.Locale;

import org.junit.Test;

import stream.vispar.server.cli.DefaultConsole;
import stream.vispar.server.engine.IEngine;
import stream.vispar.server.localization.Localizer;
import stream.vispar.server.logger.ConsoleLogger;
import stream.vispar.server.logger.ILogger;

/**
 * Tests for {@link ServerInstance}.
 * 
 * @author Micha Hanselmann
 */
public class ServerInstanceTest {
    
    /**
     * Test method for {@link ServerInstance#ServerInstance(ServerConfig)}.
     */
    @Test
    public void testServerInstance() {
        ILogger logger = mock(ILogger.class);
        ServerConfig config = new ServerConfig(8080, 8081, Locale.US, logger, "localhost", ".");
        ServerInstance instance = new ServerInstance(config);
        
        assertThat(instance.isRunning(), equalTo(false));
        assertThat(instance.getLogger(), sameInstance(logger));
        assertThat(instance.getLocalizer(), instanceOf(Localizer.class));
        assertThat(instance.getDBConn(), instanceOf(IDatabaseConnector.class));
        assertThat(instance.getEngine(), instanceOf(IEngine.class));
        assertThat(instance.getUserCtrl(), instanceOf(UserController.class));
        assertThat(instance.getPatternCtrl(), instanceOf(PatternController.class));
        assertThat(instance.getSensorCtrl(), instanceOf(SensorController.class));
        assertThat(instance.getAuthMgr(), instanceOf(AuthManager.class));
        assertThat(instance.getSockHandler(), instanceOf(ISocketHandler.class));
    }

    /**
     * Test method for {@link ServerInstance#ServerInstance(ServerConfig)}.
     * 
     * Checks for null behaviour.
     */
    @Test(expected = NullPointerException.class)
    public void testServerInstanceNull() {
        new ServerInstance(null);
    }

    /**
     * Test method for {@link ServerInstance#start()} and {@link ServerInstance#stop()}.
     */
    @Test
    public void testStartStop() {
        ILogger logger = new ConsoleLogger(new DefaultConsole(), false);
        ServerConfig config = new ServerConfig(8080, 8081, Locale.US, logger, "localhost", ".");
        ServerInstance instance = new ServerInstance(config);
        
        assertThat(instance.isRunning(), equalTo(false));
        instance.start();
        assertThat(instance.isRunning(), equalTo(true));
        instance.stop();
        assertThat(instance.isRunning(), equalTo(false));
    }

    /**
     * Test method for {@link ServerInstance#start()}.
     */
    @Test
    public void testMultipleStart() {
        ILogger logger = new ConsoleLogger(new DefaultConsole(), false);
        ServerConfig config = new ServerConfig(8080, 8081, Locale.US, logger, "localhost", ".");
        ServerInstance instance = new ServerInstance(config);
        
        assertThat(instance.isRunning(), equalTo(false));
        instance.start();
        instance.start();
        instance.start();
        assertThat(instance.isRunning(), equalTo(true));
        instance.stop();
    }

    /**
     * Test method for {@link ServerInstance#start()}.
     */
    @Test
    public void testMultipleStop() {
        ILogger logger = new ConsoleLogger(new DefaultConsole(), false);
        ServerConfig config = new ServerConfig(8080, 8081, Locale.US, logger, "localhost", ".");
        ServerInstance instance = new ServerInstance(config);
        
        assertThat(instance.isRunning(), equalTo(false));
        instance.stop();
        instance.stop();
        instance.stop();
        assertThat(instance.isRunning(), equalTo(false));
    }
}
