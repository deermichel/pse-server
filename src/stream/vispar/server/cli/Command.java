package stream.vispar.server.cli;

import stream.vispar.server.core.ServerInstance;

/**
 * Command that performs a specific execution on a {@link ServerInstance}.
 * 
 * @author Micha Hanselmann
 */
public abstract class Command {
    
    /**
     * Executes the command on a given server instance.
     * 
     * @param instance
     *          the {@link ServerInstance} to be used.
     * @return
     *          the {@link CommandResult result} of the execution.
     */
    public abstract CommandResult execute(ServerInstance instance);
}
