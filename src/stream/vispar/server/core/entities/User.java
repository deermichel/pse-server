package stream.vispar.server.core.entities;

import java.util.Objects;

/**
 * Represents a user of the system.
 * 
 * @author Micha Hanselmann
 */
public class User {

    /**
     * Name of the user.
     */
    private final String name;
    
    /**
     * Password of the user.
     */
    private final String password;


    /**
     * Constructs a new {@link User}.
     * 
     * @param name
     *          the name of the user.
     * @param password
     *          the password of the user.
     */
    public User(String name, String password) {
        this.name = Objects.requireNonNull(name);
        this.password = Objects.requireNonNull(password);
    }
    
    /**
     * Returns the name of the user.
     * 
     * @return
     *          the name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns whether the password of the user matches the given one.
     * 
     * @param password
     *          the password to check.
     * @return
     *          true if passwords match, false otherwise.
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
