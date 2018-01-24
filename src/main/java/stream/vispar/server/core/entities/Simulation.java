package stream.vispar.server.core.entities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonParseException;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.types.IJsonArray;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.jsonconverter.types.IJsonObject;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.entities.adapters.SensorJsonDeserializer;
import stream.vispar.server.core.entities.adapters.SimulatedEventDeserializer;
import stream.vispar.server.engine.IEngine;

/**
 * Collection of {@link SimulationEvent events} that can be simulated on an {@link IEngine}.
 * 
 * @author Micha Hanselmann
 */
public class Simulation {
    
    /**
     * Events of the simulation.
     */
    private final Collection<SimulatedEvent> events;
    
    
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
        
        // parse file
        // - register a SimulatedEventJsonDeserializer that performs input validation
        IJsonConverter conv = new GsonConverter()
                .registerTypeAdapter(SimulatedEvent.class, new SimulatedEventDeserializer());
        try {
            String content = new String(Files.readAllBytes(
                    new File(Objects.requireNonNull(simFile)).toPath()));
            
            // for each event
            for (IJsonElement json : new GsonConverter().fromString(content).getAsJsonArray()) {
                events.add(conv.fromJson(json, SimulatedEvent.class));
            }
            
        } catch (IOException | JsonException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
    
    /**
     * Starts simulation on a {@link ServerInstance}.
     * 
     * @param instance
     *          the {@link ServerInstance} used for the simulation.
     */
    public void simulate(ServerInstance instance) {
        
        // simulate events
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        for (SimulatedEvent simEvent : events) {
            
            // one normal execution + repeat count
            for (int i = 0; i < simEvent.getRepeatCount() + 1; i++) {
                
                // calculate delay and schedule
                int delay = simEvent.getTimeDelay() + i * simEvent.getRepeatInterval();
                scheduler.schedule(() ->  {
                    
                    // create and send event (now has correct timestamp)
                    try {
                        Event event = simEvent.createEvent(instance);
                        instance.getEngine().sendEvent(event);
                    } catch (IllegalStateException e) {
                        instance.getLogger().logError(e.toString());
                    }
                    
                }, delay, TimeUnit.MILLISECONDS);
            }
        }
    }
}
