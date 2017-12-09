package stream.vispar.server.core;

import java.util.Objects;

/**
 * Request handler implementation using Spark server framework.
 * 
 * @author Micha Hanselmann
 */
class SparkServer implements IRequestHandler {
    
    /**
     * Server instance the handler belongs to.
     */
    private final ServerInstance instance;
    
    
    /**
     * Constructs a new {@link SparkServer}.
     * 
     * @param instance
     *              the {@link ServerInstance} the handler belongs to.
     * @param port
     *              the (network) port the server should use.
     */
    SparkServer(ServerInstance instance, int port) {
        this.instance = Objects.requireNonNull(instance);
    }

    @Override
    public void start() {
        
    }

    @Override
    public void stop() {
        
    }
}
