package stream.vispar.server.core;

import java.util.Collection;
import java.util.Objects;

import foreign.Pattern;

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
     * Constructs a new {@link PatternController}.
     * 
     * @param instance
     *          the {@link ServerInstance} the controller belongs to.
     */
    public PatternController(ServerInstance instance) {
        this.instance = Objects.requireNonNull(instance);
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
     *          if the pattern did not exist before.
     */
    public Pattern update(Pattern pattern) {
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
        return null;
    }
    
    /**
     * Returns all saved patterns.
     * 
     * @return
     *          collection of all {@link Pattern patterns}.
     */
    public Collection<Pattern> getAll() {
        return null;
    }
}
