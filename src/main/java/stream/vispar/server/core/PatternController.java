package stream.vispar.server.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.model.Pattern;

/**
 * Controls the patterns in the system.
 * 
 * @author Micha Hanselmann
 */
public class PatternController {
    
    /**
     * Server instance the controller belongs to.
     */
    private final ServerInstance instance;
    
    /**
     * Json converter.
     */
    private final IJsonConverter jsonConv;
    
    
    /**
     * Constructs a new {@link PatternController}.
     * 
     * @param instance
     *          the {@link ServerInstance} the controller belongs to.
     */
    public PatternController(ServerInstance instance) {
        this.instance = Objects.requireNonNull(instance);
        this.jsonConv = new GsonConverter();
    }
    
    /**
     * Adds a pattern.
     * 
     * @param pattern
     *          the {@link Pattern} to be added.
     * @return
     *          the added {@link Pattern}.         
     * @throws IllegalArgumentException
     *          if a pattern with the same id exists.
     */
    public Pattern add(Pattern pattern) {
        return null;
    }
    
    /**
     * Updates a pattern.
     * 
     * @param pattern
     *          the {@link Pattern} to be updated.
     * @return
     *          the updated {@link Pattern}.   
     * @throws IllegalArgumentException
     *          if the pattern (with same id) was edited and has a newer timestamp then the supplied.
     */
    public Pattern update(Pattern pattern) {
        IDatabaseConnector db = instance.getDBConn();
        
        // already exists?
        if (getById(pattern.getId()) != null) {
            
            // check timestamps
            
            
        } else {
            
            // create new
            IJsonElement json = db.insert("patterns", jsonConv.toJson(pattern));
            
        }
        return null;
    }
    
    /**
     * Removes a pattern.
     * 
     * @param pattern
     *          the {@link Pattern} to be removed.
     * @throws IllegalArgumentException
     *          if the pattern did not exist before.
     */
    public void remove(Pattern pattern) {
        
    }
    
    /**
     * Deploys a pattern.
     * 
     * @param pattern
     *          the {@link Pattern} to be deployed.
     * @return
     *          the deployed {@link Pattern}.   
     * @throws IllegalArgumentException
     *          if the pattern did not exist before.
     */
    public Pattern deploy(Pattern pattern) {
        return null;
    }
    
    /**
     * Undeploys a pattern.
     * 
     * @param pattern
     *          the {@link Pattern} to be undeployed.
     * @return
     *          the undeployed {@link Pattern}.   
     * @throws IllegalArgumentException
     *          if the pattern did not exist before.
     */
    public Pattern undeploy(Pattern pattern) {
        return null;
    }
    
    /**
     * Returns a pattern by id.
     * 
     * @param id
     *          the id of the requested pattern.
     * @return
     *          the {@link Pattern} or null if not found.
     */
    public Pattern getById(String id) {
        IJsonElement json = instance.getDBConn().find("patterns", "id", id);
        if (json != null) {
            try {
                return jsonConv.fromJson(json);
            } catch (JsonException e) {
                instance.getLogger().logError(e.toString());
            }
        }
        return null;
    }
    
    /**
     * Returns all saved patterns.
     * 
     * @return
     *          collection of all {@link Pattern patterns}.
     */
    public Collection<Pattern> getAll() {
        Collection<IJsonElement> jsonCol = instance.getDBConn().getAll("patterns");
        Collection<Pattern> patterns = new ArrayList<>();
        try {
            for (IJsonElement json : jsonCol) {
                patterns.add(jsonConv.fromJson(json));
            }
            return patterns;
        } catch (JsonException e) {
            instance.getLogger().logError(e.toString());
        }
        return null;
    }
}
