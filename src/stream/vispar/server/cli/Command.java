package stream.vispar.server.cli;

import stream.vispar.server.core.ServerInstance;

/**
 * Command that performs a specific execution on a {@link ServerInstance}.
 * 
 * @author Micha Hanselmann
 */
public abstract class Command {
    
    /**
     * Executes the command.
     * 
     * @return
     *          the {@link CommandResult result} of the execution.
     */
    public abstract CommandResult execute();
}
