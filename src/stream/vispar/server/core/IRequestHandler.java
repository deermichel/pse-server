package stream.vispar.server.core;

/**
 * Defines functionality of a handler for API requests.
 * 
 * @author Micha Hanselmann
 */
public interface IRequestHandler {

    /**
     * Starts the request handler.
     */
    void start();
    
    /**
     * Stops the request handler.
     */
    void stop();
}
