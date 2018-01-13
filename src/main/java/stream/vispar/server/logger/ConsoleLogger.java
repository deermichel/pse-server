package stream.vispar.server.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
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
     * Date formatter
     */
    private final SimpleDateFormat dateFormat;
    
    
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
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Override
    public void log(String message) {
        console.println(generateTimestamp() + message);
    }

    @Override
    public void logError(String error) {
        log("[ERROR] " + error);
    }
    
    /**
     * Generates a (ISO-standard) timestamp for the current time.
     * 
     * @return
     *          the timestamp string.
     */
    private String generateTimestamp() {
        return (timestamps) ? "[" + dateFormat.format(new Date()) + "] " : "";
    }
}
