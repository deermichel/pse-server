package stream.vispar.server.core;

import java.util.Collection;
import java.util.Objects;

import stream.vispar.server.core.entities.Pattern;

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
     * @throws IllegalArgumentException
     *          if a pattern with the same id exists.
     */
    public void add(Pattern pattern) {
        
    }
    
    /**
     * Updates a pattern.
     * 
     * @param pattern
     *          the {@link Pattern} to be updated.
     * @throws IllegalArgumentException
     *          if the pattern did not exist before.
     */
    public void update(Pattern pattern) {
        
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
