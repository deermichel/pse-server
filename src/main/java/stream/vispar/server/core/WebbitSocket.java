package stream.vispar.server.core;

import java.util.Objects;

/**
 * Socket handler implementation using the Webbit library.
 * 
 * @author Micha Hanselmann
 */
public class WebbitSocket implements ISocketHandler {
    
    /**
     * Server instance the handler belongs to.
     */
    private final ServerInstance instance;
    
    
    /**
     * Constructs a new {@link WebbitSocket}.
     * 
     * @param instance
     *          the {@link ServerInstance} the handler belongs to.
     * @param port
     *          the (network) port the server should use.
     */
    public WebbitSocket(ServerInstance instance, int port) {
        this.instance = Objects.requireNonNull(instance);
    }

    @Override
    public void start() {
        
    }

    @Override
    public void stop() {
        
    }

    @Override
    public void sendMessage(String message) {
        
    }
}
