package stream.vispar.server.engine;

import java.util.Collection;
import java.util.Objects;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.entities.Event;

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
        
    }

    @Override
    public Collection<IAction> getActions() {
        return null;
    }
}
