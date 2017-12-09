package stream.vispar.server.core;

/**
 * Defines functionality of a handler for API requests.
 * 
 * @author Micha Hanselmann
 */
public interface ISocketHandler {

    /**
     * Starts the socket handler.
     */
    void start();
    
    /**
     * Stops the socket handler.
     */
    void stop();
    
    /**
     * Sends a message over the socket.
     * 
     * @param message
     *          the message to be sent.
     */
    void sendMessage(String message);
}
