package stream.vispar.server.engine;
import java.util.Objects;

import stream.vispar.model.Pattern;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.entities.Event;
import stream.vispar.server.localization.LocalizedString;

/**
 * Engine implementation using the WSO2 Siddhi library.
 * 
 * @author Micha Hanselmann
 */
public class SiddhiEngine implements IEngine {
    
    /**
     * Server instance the engine belongs to.
     */
    private final ServerInstance instance;
    
    
    /**
     * Constructs a new {@link SiddhiEngine}.
     * 
     * @param instance
     *          the {@link ServerInstance} the engine belongs to.
     */
    public SiddhiEngine(ServerInstance instance) {
        this.instance = Objects.requireNonNull(instance);
    }

    @Override
    public void start() {
        
    }

    @Override
    public void stop() {
        
    }

    @Override
    public void deploy(Pattern pattern) {
        
    }

    @Override
    public void undeploy(Pattern pattern) {
        
    }

    @Override
    public void sendEvent(Event event) {
        instance.getLogger().log(String.format(instance.getLocalizer().get(LocalizedString.RECEIVED_EVENT), 
                event.toString()));
    }
}
