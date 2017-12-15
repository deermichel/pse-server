package stream.vispar.server.core.entities;

import java.util.ArrayList;
import java.util.Collection;

import stream.vispar.server.engine.IEngine;

/**
 * Collection of {@link Event events} that can be simulated on an {@link IEngine}.
 * 
 * @author Micha Hanselmann
 */
public class Simulation {
    
    /**
     * Events of the simulation.
     */
    private final Collection<Event> events;
    
    
    /**
     * Constructs a new {@link Simulation}.
     * 
     * @param simFile
     *          the path to the file containing the simulation data.
     * @throws IllegalArgumentException
     *          if the simFile does not exist or is invalid.
     */
    public Simulation(String simFile) {
        this.events = new ArrayList<>();
    }
    
    /**
     * Starts simulation on an engine.
     * 
     * @param engine
     *          the {@link IEngine} used for the simulation.
     */
    public void simulate(IEngine engine) {
        
    }
}
