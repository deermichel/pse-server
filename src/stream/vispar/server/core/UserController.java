package stream.vispar.server.core;

import java.util.Objects;

/**
 * Controls the users in the system.
 * 
 * @author Micha Hanselmann
 */
public class UserController {
    
    /**
     * Server instance the controller belongs to.
     */
    private final ServerInstance instance;
    
    
    /**
     * Constructs a new {@link UserController}.
     * 
     * @param instance
     *          the {@link ServerInstance} the controller belongs to.
     */
    public UserController(ServerInstance instance) {
        this.instance = Objects.requireNonNull(instance);
    }
}
