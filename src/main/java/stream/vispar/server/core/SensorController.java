package stream.vispar.server.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.exceptions.JsonSyntaxException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.jsonconverter.types.IJsonObject;
import stream.vispar.model.nodes.AttributeType;
import stream.vispar.server.core.entities.Sensor;
import stream.vispar.server.core.entities.adapters.SensorJsonDeserializer;
import stream.vispar.server.localization.LocalizedString;

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
     * Path to the config files of the server.
     */
    private final String configPath;
    
    /**
     * All registered sensors.
     */
    private final Collection<Sensor> sensors;
    
    
    /**
     * Constructs a new {@link SensorController}.
     * 
     * @param instance
     *          the {@link ServerInstance} the controller belongs to.
     * @param configPath
     *          the path to the config files of the sensors.
     * @throws IllegalArgumentException
     *          if path is not a valid directory.
     */
    public SensorController(ServerInstance instance, String configPath) {
        this.instance = Objects.requireNonNull(instance);
        this.configPath = Objects.requireNonNull(configPath);
        this.sensors = new HashSet<>();
    }
    
    /**
     * Registers and parses all sensors in config path.
     */
    public void registerSensors() {

        // validate config path
        File dir = new File(Objects.requireNonNull(configPath));
        instance.getLogger().log(String.format(
                instance.getLocalizer().get(LocalizedString.READING_SENSOR_CONFIGS), dir.getAbsolutePath()));
        if (!dir.exists() || !dir.isDirectory()) {
            instance.getLogger().logError(
                    instance.getLocalizer().get(LocalizedString.CONFIG_PATH_NOT_VALID));
            instance.stop();
            System.exit(1);
        }
        
        // process each config file
        File[] files = dir.listFiles((d, name) -> name.endsWith(".conf"));
        
        // register a SensorJsonDeserializer that performs input validation
        IJsonConverter conv = new GsonConverter().registerTypeAdapter(Sensor.class, new SensorJsonDeserializer());
        for (File file : files) {
            try {
                // parse json
                String content = new String(Files.readAllBytes(file.toPath()));
                
                // parse sensor - this also does input validation
                Sensor sensor = conv.fromJson(conv.fromString(content), Sensor.class);
                sensors.add(sensor);
                instance.getLogger().log(String.format(
                        instance.getLocalizer().get(LocalizedString.SENSOR_REGISTERED), 
                        sensor.getName(), sensor.getEndpoint()));
                
            } catch (IOException | JsonException | IllegalArgumentException e) {
                instance.getLogger().logError(String.format(
                        instance.getLocalizer().get(LocalizedString.SKIP_INVALID_CONFIG), 
                        file.getAbsolutePath(), e.toString()));
            }
        }
    }
    
    /**
     * Returns all registered sensors.
     * 
     * @return
     *          collection of all {@link Sensor sensors}.
     */
    public Collection<Sensor> getAll() {
        return new HashSet<>(sensors);
    }
}
