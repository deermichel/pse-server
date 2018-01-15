package stream.vispar.server.core;

import java.util.Objects;

import spark.Service;
import stream.vispar.server.localization.LocalizedString;

/**
 * Request handler implementation using Spark server framework.
 * 
 * @author Micha Hanselmann
 */
public class SparkServer implements IRequestHandler {
    
    /**
     * Server instance the handler belongs to.
     */
    private final ServerInstance instance;
    
    /**
     * Port of the server.
     */
    private final int port;
    
    /**
     * Spark service instance.
     */
    private Service http;
    
    
    /**
     * Constructs a new {@link SparkServer}.
     * 
     * @param instance
     *              the {@link ServerInstance} the handler belongs to.
     * @param port
     *              the (network) port the server should use.
     */
    public SparkServer(ServerInstance instance, int port) {
        
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Port number has to be between 0 and 65535.");
        }
        
        this.instance = Objects.requireNonNull(instance);
        this.port = port;
    }

    @Override
    public void start() {
        http = Service.ignite().port(port);
        
        // register error callback
        http.initExceptionHandler((exception) -> {
            instance.getLogger().logError(instance.getLocalizer().get(LocalizedString.CANNOT_START_SPARK)
                    + exception.toString());
            instance.stop();
        });
        
        // register routes
        http.get("/", (req, res) -> "Hi");
        
        instance.getLogger().log(
                String.format(instance.getLocalizer().get(LocalizedString.LISTENING_FOR_API_REQUESTS), 
                        http.port()));
    }

    @Override
    public void stop() {
        if (http != null) {
            http.stop();
        }
    }
}
