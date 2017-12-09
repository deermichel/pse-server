package stream.vispar.server.core;

import java.util.Objects;

import stream.vispar.server.core.entities.User;

/**
 * Manages authentication of users using tokens.
 * 
 * @author Micha Hanselmann
 */
class AuthManager {
    
    /**
     * Server instance the manager belongs to.
     */
    private final ServerInstance instance;
    
    
    /**
     * Constructs a new {@link AuthManager}.
     * 
     * @param instance 
     *          the {@link ServerInstance} the manager belongs to.
     */
    AuthManager(ServerInstance instance) {
        this.instance = Objects.requireNonNull(instance);
    }
    
    /**
     * Logs an user in and returns the token.
     * 
     * @param user
     *          the {@link User} to be logged in.
     * @return
     *          the access token of the user.         
     */
    public String login(User user) {
        return "";
    }
    
    /**
     * Logs an user out by invalidating the token.
     * 
     * @param token
     *          the access token of the user.
     */
    public void logout(String token) {
        
    }
    
    /**
     * Authenticates an user by the access token.
     * 
     * @param token
     *          the access token of the user.
     * @return
     *          the authenticated user or null if token is invalid.
     */
    public User authenticate(String token) {
        return null;
    }
}
