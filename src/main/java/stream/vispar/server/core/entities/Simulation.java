package stream.vispar.server.core.entities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;

import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.exceptions.JsonSyntaxException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.types.IJsonArray;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.jsonconverter.types.IJsonObject;
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
        
        GsonConverter c = new GsonConverter();
        String src = "{\n" + 
                "        \"timestamp\": \"0\",\n" + 
                "        \"sensor\": \"temp1\",\n" + 
                "        \"data\": {\n" + 
                "            \"value\": { \"fixed\": \"23\" },\n" + 
                "            \"room\": { \"fixed\": \"Kinderzimmer\" }\n" + 
                "        }\n" + 
                "    }";
        
        try {
            IJsonElement raw = c.fromString(src);
            
            System.out.println(c.fromJson(raw, SimulatedEvent.class));
            
            
            
            
        } catch (JsonException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // parse file
//        try {
//            String content = new String(Files.readAllBytes(
//                    new File(Objects.requireNonNull(simFile)).toPath()));
//            
//            // for each event
//            for (IJsonElement json : new GsonConverter().fromString(content).getAsJsonArray()) {
//                
//            }
//            
//        } catch (IOException | JsonParseException | JsonSyntaxException e) {
//            throw new IllegalArgumentException(e.toString());
//        }
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
