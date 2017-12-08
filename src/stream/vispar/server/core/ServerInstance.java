package stream.vispar.server.core;

import stream.vispar.server.cli.Command;
import stream.vispar.server.cli.CommandParser;
import stream.vispar.server.cli.CommandResult;
import stream.vispar.server.logger.ILogger;

/**
 * Server instance holding and controlling all components of the server.
 * 
 * @author Micha Hanselmann
 */
public class ServerInstance {
    
    /**
     * Logger used by the instance.
     */
    private final ILogger logger;
    
    
    /**
     * Constructs a new {@link ServerInstance}.
     * 
     * @param logger
     *          the {@link ILogger logger} used for this instance.
     */
    public ServerInstance(ILogger logger) {
        this.logger = logger;
    }
    
    /**
     * Prepares and starts all components of the server.
     */
    private void init() {
        
    }
    
    /**
     * Stops all components of the server.
     */
    private void stop() {
        
    }
    
    /**
     * Executes a command on this {@link ServerInstance}.
     * 
     * @param instance
     *          the {@link ServerInstance} to be used.
     * @return
     *          the {@link CommandResult result} of the execution.
     */
    public CommandResult execute(Command command) {
        return command.execute(this);
    }
    
    /**
     * Returns the logger used by this {@link ServerInstance}.
     * 
     * @return
     *          the {@link ILogger logger}.
     */
    public ILogger getLogger() {
        return logger;
    }
}
