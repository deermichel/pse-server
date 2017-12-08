package stream.vispar.server.logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Logger implementation that forwards a logged message to multiple loggers.
 * 
 * @author Micha Hanselmann
 */
public class MultiLogger implements ILogger {

    /**
     * Loggers addressed by this logger.
     */
    private final Collection<ILogger> loggers;
    
    
    /**
     * Constructs a new {@link MultiLogger} instance.
     */
    public MultiLogger() {
        loggers = new ArrayList<>();
    }

    @Override
    public void log(String message) {
        loggers.forEach(logger -> logger.log(message));
    }

    @Override
    public void logError(String error) {
        loggers.forEach(logger -> logger.logError(error));
    }
    
    /**
     * Adds a logger to this {@link MultiLogger}.
     * 
     * @param logger
     *          the {@link ILogger logger} to be added.
     */
    public void addLogger(ILogger logger) {
        loggers.add(Objects.requireNonNull(logger));
    }
}
