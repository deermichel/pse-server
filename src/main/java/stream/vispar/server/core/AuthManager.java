package stream.vispar.server.core;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import stream.vispar.server.core.entities.User;
import stream.vispar.server.localization.LocalizedString;

/**
 * Manages authentication of users using tokens.
 * 
 * @author Micha Hanselmann
 */
public class AuthManager {
    
    /**
     * Server instance the manager belongs to.
     */
    private final ServerInstance instance;
    
    /**
     * User to token mapping.
     */
    private final Map<String, User> tokens;
    
    
    /**
     * Constructs a new {@link AuthManager}.
     * 
     * @param instance 
     *          the {@link ServerInstance} the manager belongs to.
     */
    public AuthManager(ServerInstance instance) {
        this.instance = Objects.requireNonNull(instance);
        tokens = new ConcurrentHashMap<>();
    }
    
    /**
     * Logs a user in and returns the token.
     * 
     * @param username
     *          the username of the {@link User} to be logged in.
     * @param password
     *          the password of the {@link User} to be logged in.
     * @return
     *          the access token of the user.     
     * @throws IllegalArgumentException
     *          if user does not exist or password is incorrect
     */
    public String login(String username, String password) {
        
        // get user and check password
        User user = instance.getUserCtrl().getByName(username);
        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        } else if (!user.checkPassword(password)) {
            throw new IllegalArgumentException("Password is incorrect");
        }
        
        // generate token
        String token = generateRandomHexToken(64); // 512 bit
        tokens.put(token, user);
        instance.getLogger().log(String.format(instance.getLocalizer().get(LocalizedString.USER_LOGGED_IN), 
                user.getName()));
        return token;
    }
    
    /**
     * Logs a user out by invalidating the token.
     * 
     * @param token
     *          the access token of the user.
     */
    public void logout(String token) {
        
        // remove token
        User user = tokens.remove(token);
        instance.getLogger().log(String.format(instance.getLocalizer().get(LocalizedString.USER_LOGGED_OUT), 
                user.getName()));
    }
    
    /**
     * Authenticates a user by the access token.
     * 
     * @param token
     *          the access token of the user.
     * @return
     *          the authenticated user or null if token is invalid.
     */
    public User authenticate(String token) {
        return tokens.get(token);
    }
    
    /**
     * Logs a user out by invalidating all tokens (e.g. if user was deleted).
     * 
     * @param user
     *          the user to be logged out.
     */
    public void logoutAll(User user) {
        tokens.forEach((t, u) -> {
            if (u.equals(user)) {
                tokens.remove(t);
            }
        });
    }
    
    /**
     * Generates a random token in hex format.
     * 
     * https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string/41156#41156
     * 
     * @param byteLength
     *          byte length of the token.
     * @return
     *          the token.
     */
    private static String generateRandomHexToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16); // hex encoding
    }
}
