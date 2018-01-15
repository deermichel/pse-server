/**
 * 
 */
package stream.vispar.server.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import stream.vispar.server.cli.DefaultConsole;
import stream.vispar.server.logger.ConsoleLogger;
import stream.vispar.server.logger.ILogger;

/**
 * Tests for {@link SparkServer}.
 * 
 * @author Micha Hanselmann
 */
public class SparkServerTest {
    
    private ServerInstance instance;
    

    /**
     * Create fake instance.
     */
    @Before
    public void setUp() {
        ILogger logger = new ConsoleLogger(new DefaultConsole(), false);
        ServerConfig config = new ServerConfig(8888, 8889, Locale.US, logger, "", "");
        instance = new ServerInstance(config);
    }

    /**
     * Cleanup.
     */
    @After
    public void tearDown() {
        instance.stop();
    }

    /**
     * Test method for {@link SparkServer#SparkServer(ServerInstance, int)}.
     * 
     * Checks null argument behaviour.
     */
    @Test(expected = NullPointerException.class)
    public void testSparkServerNull() {
        new SparkServer(null, 80);
    }

    /**
     * Test method for {@link SparkServer#SparkServer(ServerInstance, int)}.
     * 
     * Checks invalid argument behaviour.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSparkServerInv1() {
        new SparkServer(instance, -80);
    }

    /**
     * Test method for {@link SparkServer#SparkServer(ServerInstance, int)}.
     * 
     * Checks invalid argument behaviour.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSparkServerInv2() {
        new SparkServer(instance, 788888);
    }

    /**
     * Test method for {@link SparkServer#start()}.
     * 
     * @throws IOException server problem.
     */
    @Test
    public void testStart() throws IOException {
        SparkServer server = new SparkServer(instance, 8080);
        server.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertThat(new Socket("localhost", 8080), notNullValue());
        server.stop();
    }

    /**
     * Test method for {@link SparkServer#stop()}.
     * 
     * @throws IOException server problem.
     */
    @Test(expected = ConnectException.class)
    public void testStop() throws IOException {
        SparkServer server = new SparkServer(instance, 8081);
        server.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.stop();
        
        new Socket("localhost", 8081).close();
    }
}
