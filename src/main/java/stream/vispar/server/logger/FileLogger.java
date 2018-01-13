package stream.vispar.server.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Logger implementation that logs to a file.
 * 
 * @author Micha Hanselmann
 */
public class FileLogger implements ILogger {
    
    /**
     * File path
     */
    private final Path filePath;
    
    /**
     * Determines whether log contains timestamps.
     */
    private final boolean timestamps;
    
    /**
     * Date formatter
     */
    private final SimpleDateFormat dateFormat;
    
    
    /**
     * Constructs a new {@link FileLogger} instance.
     * 
     * @param file
     *          the path to the file to be used for logging.
     * @param timestamps
     *          determines whether log should contain timestamps.
     */
    public FileLogger(String file, boolean timestamps) {
        Objects.requireNonNull(file);
        this.filePath = Paths.get(file);
        this.timestamps = timestamps;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        
        // test for permission
        try {
            Files.write(filePath, "".getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("[ERROR] Could not create logfile: " + e.toString());
            throw new IllegalArgumentException("Could not create logfile '" + file + "'.");
        }
    }

    @Override
    public void log(String message) {
        String entry = generateTimestamp() + message + "\n";
        try {
            Files.write(filePath, entry.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("[ERROR] Could not write to logfile: " + e.toString());
        }
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
