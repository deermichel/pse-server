package stream.vispar.server.cli;

import stream.vispar.server.core.ServerInstance;

/**
 * Factory to generate commands for a specific {@link ServerInstance}.
 * 
 * @author Micha Hanselmann
 */
public class CommandFactory {
    
    /**
     * Target instance of generated commands.
     */
    private final ServerInstance instance;
    
    
    /**
     * Constructs a new {@link CommandFactory}.
     * 
     * @param instance
     *          the {@link ServerInstance} where generated commands should execute on.
     */
    public CommandFactory(ServerInstance instance) {
        this.instance = instance;
    }
    
    /**
     * Creates a new add user command.
     * 
     * @return
     *          the {@link Command}.
     */
    public Command cmdAddUser() {
        return null;
    }
    
    /**
     * Creates a new remove user command.
     * 
     * @return
     *          the {@link Command}.
     */
    public Command cmdRemoveUser() {
        return null;
    }
    
    /**
     * Creates a new list users command.
     * 
     * @return
     *          the {@link Command}.
     */
    public Command cmdListUsers() {
        return null;
    }
    
    /**
     * Creates a new list patterns command.
     * 
     * @return
     *          the {@link Command}.
     */
    public Command cmdListPatterns() {
        return null;
    }
    
    /**
     * Creates a new simulate command.
     * 
     * @return
     *          the {@link Command}.
     */
    public Command cmdSimulate() {
        return null;
    }
    
    /**
     * Creates a new stop server command.
     * 
     * @return
     *          the {@link Command}.
     */
    public Command cmdStopServer() {
        return null;
    }
}
