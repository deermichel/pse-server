package stream.vispar.server.core;

import java.io.IOException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import spark.Service;
import stream.vispar.server.localization.LocalizedString;

/**
 * Socket handler implementation using the Spark server framework.
 * 
 * @author Micha Hanselmann
 */
@WebSocket // annotation needed for Jetty
public class SparkSocket implements ISocketHandler {
    
    /**
     * Server instance the handler belongs to.
     */
    private final ServerInstance instance;
    
    /**
     * Port of the socket.
     */
    private final int port;
    
    /**
     * Spark service instance.
     */
    private Service http;
    
    /**
     * Currently opened connections (thread-safe).
     */
    private final Queue<Session> sessions;
    
    
    /**
     * Constructs a new {@link SparkSocket}.
     * 
     * @param instance
     *          the {@link ServerInstance} the handler belongs to.
     * @param port
     *          the (network) port the socket should use.
     */
    public SparkSocket(ServerInstance instance, int port) {
        
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Port number has to be between 0 and 65535.");
        }
        
        this.instance = Objects.requireNonNull(instance);
        this.port = port;
        this.sessions = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void start() {
        http = Service.ignite().port(port);
        
        // register error callback
        http.initExceptionHandler((exception) -> {
            instance.getLogger().logError(String.format(
                    instance.getLocalizer().get(LocalizedString.CANNOT_START_SPARK), exception.toString()));
            instance.stop();
            System.exit(1);
        });
        
        // register socket
        http.webSocket("", this);
        http.init();
        
        http.awaitInitialization();
        
        instance.getLogger().log(
                String.format(instance.getLocalizer().get(LocalizedString.SOCKET_OPENED), http.port()));
    }

    @Override
    public void stop() {
        if (http != null) {
            // disconnect all clients
            try {
                for (Session session : sessions) {
                    session.disconnect();
                }
            } catch (IOException e) {
                instance.getLogger().logError(e.toString());
            } finally {
                sessions.clear();
            }
            
            http.stop();
        }
    }

    @Override
    public void sendMessage(String message) {
        
        // send message to all connected clients
        try {
            for (Session session : sessions) {
                session.getRemote().sendString(message);
            }
        } catch (IOException e) {
            instance.getLogger().logError(e.toString());
        }
    }
    
    /**
     * Client connected to the socket.
     * 
     * @param session
     *          the connection {@link Session}.
     */
    @OnWebSocketConnect
    public void clientConnected(Session session) {
        sessions.add(session);
        instance.getLogger().log(
                String.format(instance.getLocalizer().get(LocalizedString.CONNECTED_TO_SOCKET), 
                        session.getRemoteAddress().getAddress().getHostAddress()));
    }

    /**
     * Client disconnected from the socket.
     * 
     * @param session
     *          the connection {@link Session}.
     * @param statusCode
     *          close status code.
     * @param reason
     *          close reason.
     */
    @OnWebSocketClose
    public void clientDisconnected(Session session, int statusCode, String reason) {
        sessions.remove(session);
        instance.getLogger().log(
                String.format(instance.getLocalizer().get(LocalizedString.DISCONNECTED_FROM_SOCKET), 
                        session.getRemoteAddress().getAddress().getHostAddress()));
    }
}
