package stream.vispar.server.core;

import java.util.Collection;
import java.util.Objects;

import foreign.Pattern;
import stream.vispar.server.core.entities.User;

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
    
    /**
     * Adds an user.
     * 
     * @param user
     *          the {@link User} to be added.
     * @return
     *          the added {@link User}. 
     * @throws IllegalArgumentException
     *          if a user with the same name exists.
     */
    public User add(User user) {
        return null;
    }
    
    /**
     * Removes an user.
     * 
     * @param user
     *          the {@link User} to be removed.
     * @throws IllegalArgumentException
     *          if the user did not exist before.
     */
    public void remove(User user) {
        
    }
    
    /**
     * Returns an user by name.
     * 
     * @param name
     *          the name of the requested user.
     * @return
     *          the {@link User} or null if not found.
     */
    public User getByName(String name) {
        return null;
    }
    
    /**
     * Returns all users.
     * 
     * @return
     *          collection of all {@link User users}.
     */
    public Collection<User> getAll() {
        return null;
    }
}
