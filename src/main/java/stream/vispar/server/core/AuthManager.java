package stream.vispar.server.core;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        tokens = new HashMap<>();
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
        String token = generateRandomHexToken(64); // 512 bit
        tokens.put(token, user);
        instance.getLogger().log(String.format(instance.getLocalizer().get(LocalizedString.USER_LOGGED_IN), 
                user.getName()));
        return token;
    }
    
    /**
     * Logs an user out by invalidating the token.
     * 
     * @param token
     *          the access token of the user.
     */
    public void logout(String token) {
        tokens.remove(token);
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
        return tokens.get(token);
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
