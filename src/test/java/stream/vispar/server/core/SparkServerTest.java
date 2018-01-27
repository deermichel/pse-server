/**
 * 
 */
package stream.vispar.server.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import stream.vispar.server.cli.DefaultConsole;
import stream.vispar.server.engine.IEngine;
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
        ServerConfig config = new ServerConfig(8888, 8889, Locale.US, logger, "localhost", 
                "./src/test/resources/sensors");
        instance = new ServerInstance(config);
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
        assertThat(new Socket("localhost", 8080), notNullValue());
        server.stop();
    }

    /**
     * Test method for {@link SparkServer#stop()}.
     * 
     * @throws IOException server problem.
     */
    @Test
    public void testStopNoException() throws IOException {
        SparkServer server = new SparkServer(instance, 8080);
        server.stop();
    }

    /**
     * Test method for {@link SparkServer#stop()}.
     * 
     * @throws IOException server problem.
     */
    @Ignore("stopping is not synchronous")
    @Test(expected = ConnectException.class)
    public void testStop() throws IOException {
        SparkServer server = new SparkServer(instance, 8081);
        server.start();
        server.stop();
        new Socket("localhost", 8081).close();
    }

    /**
     * Test method for /me route (if api route creation works).
     * 
     * @throws IOException server problem.
     */
    @Test
    public void testMeRouteCreated() throws IOException {
        SparkServer server = new SparkServer(instance, 8080);
        server.start();
        
        URL url = new URL("http://localhost:8080/api/users/me");
        URLConnection conn = url.openConnection();
        InputStream stream = conn.getInputStream();
        String result = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));

        assertThat(result, equalToIgnoringCase("{\"error\":1001}"));
        
        stream.close();
        server.stop();
    }

    /**
     * Test method for /sensor/temp1 route (if sensor route creation works).
     * 
     * @throws IOException server problem.
     */
    @Test
    public void testSensorRouteCreated() throws IOException {
        instance.getSensorCtrl().registerSensors();
        assertThat(instance.getSensorCtrl().getByName("temp1"), notNullValue());
        ServerInstance spy = spy(instance);
        when(spy.getEngine()).thenReturn(mock(IEngine.class));
        
        SparkServer server = new SparkServer(spy, 8080);
        server.start();
        
        URL url = new URL("http://localhost:8080/sensor/temp1");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        
        // sensor request
        String request = "{\n"
                        + "    \"value\": \"23\",\n"
                        + "    \"other\": {\n"
                        + "        \"room\": \"home\"\n"
                        + "    }\n" 
                        + "}";
        byte[] out = request.getBytes(StandardCharsets.UTF_8);
        http.setFixedLengthStreamingMode(out.length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try (OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        
        InputStream stream = http.getInputStream();
        String result = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));

        assertThat(result, equalToIgnoringCase("{}"));
        
        stream.close();
        server.stop();
    }
}
