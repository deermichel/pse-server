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
        IJsonConverter conv = new GsonConverter();
        for (File file : files) {
            try {
                // parse json
                String content = new String(Files.readAllBytes(file.toPath()));
                IJsonObject json = conv.fromString(content).getAsJsonObject();
                
                // validate sensor config json
                if (!json.getAsJsonPrimitive("name").getAsString().matches("[a-zA-Z0-9]+")) {
                    throw new IllegalArgumentException("Name has to match regex [a-zA-Z0-9]+");
                } else if (!json.getAsJsonPrimitive("endpoint").getAsString().matches("[a-zA-Z0-9]+")) {
                    throw new IllegalArgumentException("Endpoint has to match regex [a-zA-Z0-9]+");
                }
                String attrRegex = "[a-zA-Z0-9]+(\\[[0-9]+\\])?(\\.[a-zA-Z0-9]+(\\[[0-9]+\\])?)*";
                for (Entry<String, IJsonElement> attr : json.getAsJsonObject("attributes").entrySet()) {
                    if (!attr.getKey().matches(attrRegex)) {
                        throw new IllegalArgumentException("Attributes have to match regex " + attrRegex);
                    } else if (!attr.getValue().getAsJsonObject().getAsJsonPrimitive("name")
                            .getAsString().matches("[a-zA-Z0-9]+")) {
                        throw new IllegalArgumentException(
                                "Attribute names have to match regex [a-zA-Z0-9]+");
                    } else if (!attr.getValue().getAsJsonObject().getAsJsonPrimitive("type")
                            .getAsString().matches("INTEGER|DOUBLE|STRING")) {
                        throw new IllegalArgumentException(
                                "Attribute types have to be INTEGER, DOUBLE or STRING");
                    }
                }
                
                // parse sensor
                Sensor sensor = conv.fromJson(json, Sensor.class);
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
