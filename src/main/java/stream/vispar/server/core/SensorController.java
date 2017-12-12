package stream.vispar.server.core;

import java.util.Collection;
import java.util.Objects;

import stream.vispar.server.core.entities.Sensor;

/**
 * Controls the sensors in the system.
 * 
 * @author Micha Hanselmann
 */
public class SensorController {
    
    /**
     * Server instance the controller belongs to.
     */
    private final ServerInstance instance;
    
    
    /**
     * Constructs a new {@link SensorController}.
     * 
     * @param instance
     *          the {@link ServerInstance} the controller belongs to.
     * @param configPath
     *          the path to the config files of the sensors.
     */
    public SensorController(ServerInstance instance, String configPath) {
        this.instance = Objects.requireNonNull(instance);
        Objects.requireNonNull(configPath);
    }
    
    /**
     * Returns all registered sensors.
     * 
     * @return
     *          collection of all {@link Sensor sensors}.
     */
    public Collection<Sensor> getAll() {
        return null;
    }
}
