package stream.vispar.server.logger;

import java.util.Objects;

import stream.vispar.server.cli.IConsole;

/**
 * Logger implementation that logs to a {@link IConsole console}.
 * 
 * @author Micha Hanselmann
 */
public class ConsoleLogger implements ILogger {

    /**
     * Console to be used for logging.
     */
    private final IConsole console;
    
    /**
     * Determines whether log contains timestamps.
     */
    private final boolean timestamps;
    
    
    /**
     * Constructs a new {@link ConsoleLogger} instance.
     * 
     * @param console
     *          the console to be used for logging.
     * @param timestamps
     *          determines whether log should contain timestamps.
     */
    public ConsoleLogger(IConsole console, boolean timestamps) {
        this.console = Objects.requireNonNull(console);
        this.timestamps = timestamps;
    }

    @Override
    public void log(String message) {
        console.println(generateTimestamp() + "[INFO] " + message);
    }

    @Override
    public void logError(String error) {
        console.println(generateTimestamp() + "[ERROR] " + error);
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
