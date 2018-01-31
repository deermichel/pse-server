/**
 * 
 */
package stream.vispar.server.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.Locale;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import stream.vispar.server.cli.DefaultConsole;
import stream.vispar.server.logger.ConsoleLogger;
import stream.vispar.server.logger.ILogger;

/**
 * Tests for {@link SparkSocket}.
 * 
 * @author Micha Hanselmann
 */
public class SparkSocketTest {
    
    private ServerInstance instance;
    private String receivedMsg = "";
    private boolean connected = false;
    
    /**
     * Socket client for testing.
     */
    @WebSocket
    protected class TestSocket {
        /**
         * Connected.
         * 
         * @param session
         *          the session.
         */
        @OnWebSocketConnect
        public void onConnect(Session session) {
            connected = true;
        }
        
        /**
         * Received message.
         * 
         * @param msg
         *          the message.
         */
        @OnWebSocketMessage
        public void onMessage(String msg) {
            receivedMsg = msg;
        }
    }

    
    /**
     * Create fake instance.
     */
    @Before
    public void setUp() {
        ILogger logger = new ConsoleLogger(new DefaultConsole(), false);
        ServerConfig config = new ServerConfig(8890, 8891, Locale.US, logger, "localhost", 
                "./src/test/resources/sensors");
        instance = new ServerInstance(config);
    }

    /**
     * Test method for {@link SparkSocket#SparkSocket(ServerInstance, int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSparkSocketBadPort1() {
        new SparkSocket(instance, -80);
    }

    /**
     * Test method for {@link SparkSocket#SparkSocket(ServerInstance, int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSparkSocketBadPort2() {
        new SparkSocket(instance, 65536);
    }

    /**
     * Test method for {@link SparkSocket#SparkSocket(ServerInstance, int)}.
     */
    @Test(expected = NullPointerException.class)
    public void testSparkSocketNull() {
        new SparkSocket(null, 80);
    }

    /**
     * Test method for {@link SparkSocket#start()}.
     * 
     * @throws IOException socket problem.
     */
    @Test
    public void testStart() throws IOException {
        SparkSocket socket = new SparkSocket(instance, 8080);
        socket.start();
        assertThat(new Socket("localhost", 8080), notNullValue());
        socket.stop();
    }

    /**
     * Test method for {@link SparkSocket#stop()}.
     * 
     * @throws IOException server problem.
     */
    @Test
    public void testStopNoException() throws IOException {
        SparkSocket socket = new SparkSocket(instance, 8080);
        socket.stop();
    }

    /**
     * Test method for {@link SparkSocket#stop()}.
     */
    @Ignore("stopping is not synchronous")
    @Test
    public void testStop() {
    }

    /**
     * Test method for {@link SparkSocket#sendMessage(java.lang.String)}.
     * 
     * @throws Exception socket problem.
     */
    @Test(timeout = 5000)
    public void testSendMessage() throws Exception {
        SparkSocket socket = new SparkSocket(instance, 8080);
        socket.start();
        assertThat(new Socket("localhost", 8080), notNullValue());
        
        // connect
        WebSocketClient client = new WebSocketClient();
        TestSocket testSocket = new TestSocket();
        client.start();
        client.connect(testSocket, new URI("ws://localhost:8080"));
        while (!connected) {
            Thread.sleep(100);
        }
        
        // send
        socket.sendMessage("Hello Socket!");
        while (receivedMsg.length() == 0) {
            Thread.sleep(100);
        }
        assertThat(receivedMsg, equalTo("Hello Socket!"));
        
        socket.stop();
        client.stop();
    }
}
