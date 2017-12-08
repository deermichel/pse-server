package stream.vispar.server.logger;

/**
 * Defines functionality of a logger.
 * 
 * @author Micha Hanselmann
 */
public interface ILogger {

    /**
     * Logs a (normal) string message.
     * 
     * @param message
     *          the message to be logged.
     */
    void log(String message);
    
    /**
     * Logs an error string message.
     * 
     * @param error
     *          the error message to be logged.
     */
    void logError(String error);
}
