package stream.vispar.server.engine;

import foreign.Pattern;
import stream.vispar.server.core.entities.Event;

/**
 * Defines functionality of a CEP engine.
 * 
 * @author Micha Hanselmann
 */
public interface IEngine {

    /**
     * Starts the engine.
     */
    void start();
    
    /**
     * Stops the engine.
     */
    void stop();
    
    /**
     * Deploys a pattern which will start its detection.
     * 
     * @param pattern
     *          the {@link Pattern} to be deployed.
     */
    void deploy(Pattern pattern);
    
    /**
     * Undeploys a pattern which will stop its detection.
     * 
     * @param pattern
     *          the {@link Pattern} to be undeployed.
     */
    void undeploy(Pattern pattern);
    
    /**
     * Sends an event to the engine (e.g. from a sensor).
     * 
     * @param event
     *          the {@link Event} to be sent.
     */
    void sendEvent(Event event);
}
