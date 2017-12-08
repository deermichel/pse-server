package stream.vispar.server.logger;

/**
 * Logger implementation that logs to a file.
 * 
 * @author Micha Hanselmann
 */
public class FileLogger implements ILogger {
    
    /**
     * Determines whether log contains timestamps.
     */
    private final boolean timestamps;
    
    
    /**
     * Constructs a new {@link FileLogger} instance.
     * 
     * @param file
     *          the path to the file to be used for logging.
     * @param timestamps
     *          determines whether log should contain timestamps.
     */
    public FileLogger(String file, boolean timestamps) {
        this.timestamps = timestamps;
    }

    @Override
    public void log(String message) {
    }

    @Override
    public void logError(String error) {
    }
    
    /**
     * Generates a (ISO-standard) timestamp for the current time.
     * 
     * @return
     *          the timestamp string.
     */
    private String generateTimestamp() {
        return (timestamps) ? "[todo] " : "";
    }
}
