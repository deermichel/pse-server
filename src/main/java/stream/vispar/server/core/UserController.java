package stream.vispar.server.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.server.core.entities.User;
import stream.vispar.server.localization.LocalizedString;

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
     * Json converter.
     */
    private final IJsonConverter jsonConv;
    
    
    /**
     * Constructs a new {@link UserController}.
     * 
     * @param instance
     *          the {@link ServerInstance} the controller belongs to.
     */
    public UserController(ServerInstance instance) {
        this.instance = Objects.requireNonNull(instance);
        this.jsonConv = new GsonConverter();
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
        IDatabaseConnector db = instance.getDBConn();
        
        // does user already exist?
        if (getByName(user.getName()) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        
        // create user
        IJsonElement json = db.insert("users", jsonConv.toJson(user));
        instance.getLogger().log(String.format(instance.getLocalizer().get(LocalizedString.USER_ADDED), 
                user.getName()));
        
        // return created user
        try {
            return jsonConv.fromJson(json, User.class);
        } catch (JsonException e) {
            instance.getLogger().logError(e.toString());
        }
        return null;
    }
    
    /**
     * Removes an user.
     * 
     * @param username
     *          the username of the {@link User} to be removed.
     * @throws IllegalArgumentException
     *          if the user did not exist before.
     */
    public void remove(String username) {
        IDatabaseConnector db = instance.getDBConn();
        
        // get user
        User user = getByName(username);
        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }
        
        // delete user
        db.delete("users", "name", user.getName());
        instance.getLogger().log(String.format(instance.getLocalizer().get(LocalizedString.USER_REMOVED), 
                user.getName()));
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
        IJsonElement json = instance.getDBConn().find("users", "name", name);
        if (json != null) {
            try {
                return jsonConv.fromJson(json, User.class);
            } catch (JsonException e) {
                instance.getLogger().logError(e.toString());
            }
        }
        return null;
    }
    
    /**
     * Returns all users.
     * 
     * @return
     *          collection of all {@link User users}.
     */
    public Collection<User> getAll() {
        Collection<IJsonElement> jsonCol = instance.getDBConn().getAll("users");
        Collection<User> users = new HashSet<>();
        try {
            for (IJsonElement json : jsonCol) {
                users.add(jsonConv.fromJson(json, User.class));
            }
            return users;
        } catch (JsonException e) {
            instance.getLogger().logError(e.toString());
        }
        return null;
    }
}
