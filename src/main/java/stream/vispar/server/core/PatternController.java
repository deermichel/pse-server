package stream.vispar.server.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.model.Pattern;
import stream.vispar.model.nodes.inputs.InputNode;
import stream.vispar.model.nodes.inputs.SensorNode;
import stream.vispar.server.core.entities.adapters.NodeVisitorAdapter;
import stream.vispar.server.localization.LocalizedString;

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
     * Updates a pattern.
     * 
     * @param pattern
     *          the {@link Pattern} to be updated.
     * @return
     *          the updated {@link Pattern}.   
     * @throws IllegalArgumentException
     *          if the pattern (with same id) was edited and has a newer timestamp then the supplied.
     */
    public synchronized Pattern update(Pattern pattern) {
        IDatabaseConnector db = instance.getDBConn();
        
        // already exists?
        Pattern existingPattern = getById(pattern.getId());
        if (existingPattern != null) {

            if (existingPattern.getLastUpdated().after(pattern.getLastUpdated())) {
                throw new IllegalArgumentException("Newer version of pattern already saved");
            }
            
            // update pattern
            IJsonElement json = db.update("patterns", "id", pattern.getId(), jsonConv.toJson(pattern));
            instance.getLogger().log(String.format(
                    instance.getLocalizer().get(LocalizedString.PATTERN_UPDATED), pattern.getName()));
            
            // return updated pattern
            try {
                return jsonConv.fromJson(json);
            } catch (JsonException e) {
                instance.getLogger().logError(e.toString());
            }
            
        } else {
            
            // create pattern
            IJsonElement json = db.insert("patterns", jsonConv.toJson(pattern));
            instance.getLogger().log(String.format(
                    instance.getLocalizer().get(LocalizedString.PATTERN_CREATED), pattern.getName()));
            
            // return created pattern
            try {
                return jsonConv.fromJson(json);
            } catch (JsonException e) {
                instance.getLogger().logError(e.toString());
            }
        }
        return null;
    }
    
    /**
     * Removes a pattern.
     * 
     * @param id
     *          the id of the {@link Pattern} to be removed.
     * @throws IllegalArgumentException
     *          if the pattern did not exist before.
     */
    public synchronized void remove(String id) {
        IDatabaseConnector db = instance.getDBConn();
        
        // get pattern
        Pattern pattern = getById(id);
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern does not exist");
        }
        
        // delete pattern
        db.delete("patterns", "id", pattern.getId());
        instance.getLogger().log(String.format(instance.getLocalizer().get(LocalizedString.PATTERN_DELETED),
                pattern.getName()));
    }
    
    /**
     * Renames a pattern.
     * 
     * @param id
     *          the id of the {@link Pattern} to be renamed.
     * @param newName
     *          the new name of the pattern.
     * @return
     *          the renamed {@link Pattern}.   
     * @throws IllegalArgumentException
     *          if the pattern did not exist before.
     */
    public synchronized Pattern rename(String id, String newName) {
        IDatabaseConnector db = instance.getDBConn();
        
        // get pattern
        Pattern pattern = getById(id);
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern does not exist");
        }
        
        // rename pattern
        IJsonElement json = db.find("patterns", "id", pattern.getId());
        try {
            json.getAsJsonObject().add("name", newName);
        } catch (JsonParseException e) {
            instance.getLogger().logError(e.toString());
        }
        db.update("patterns", "id", pattern.getId(), json);
        instance.getLogger().log(String.format(instance.getLocalizer().get(
                LocalizedString.PATTERN_UPDATED), newName));
        
        // return renamed pattern
        try {
            return jsonConv.fromJson(json);
        } catch (JsonException e) {
            instance.getLogger().logError(e.toString());
        }
        return null;
    }
    
    /**
     * Deploys a pattern.
     * 
     * @param id
     *          the id pf the {@link Pattern} to be deployed.
     * @return
     *          the deployed {@link Pattern}.   
     * @throws IllegalArgumentException
     *          if the pattern does not exist.
     * @throws IllegalStateException
     *          if the pattern is already deployed, is invalid, sensors used in pattern
     *          are not registered on server.
     */
    public synchronized Pattern deploy(String id) {
        IDatabaseConnector db = instance.getDBConn();
        
        // get pattern and check deployment status
        Pattern pattern = getById(id);
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern does not exist");
        } else if (pattern.isDeployed()) {
            throw new IllegalStateException(String.valueOf(RouteError.PATTERN_ALREADY_DEPLOYED.getCode()));
        }
        
        // validate pattern and sensors used in pattern
        if (!pattern.isValid()) {
            throw new IllegalStateException(String.valueOf(RouteError.PATTERN_INVALID.getCode()));
        }
        Collection<String> usedSensors = new ArrayList<>();
        for (InputNode input : pattern.getInputNodes()) {
            input.acceptVisitor(new NodeVisitorAdapter() {
                @Override
                public void visitSensorNode(SensorNode node) {
                    usedSensors.add(node.getSensorName());
                }
            });
        }
        for (String sensor : usedSensors) {
            if (instance.getSensorCtrl().getByName(sensor) == null) {
                // throw new IllegalStateException("Sensor '" + sensor + "' not registered on server");
                throw new IllegalStateException(String.valueOf(RouteError.UNKNOWN_SENSORS.getCode()));
            }
        }
        
        // deploy pattern
        try {
            instance.getEngine().deploy(pattern);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(String.valueOf(RouteError.PATTERN_INVALID.getCode()));
        }
        instance.getLogger().log(String.format(instance.getLocalizer().get(
                LocalizedString.PATTERN_DEPLOYED), pattern.getName()));
        
        // update deployment status in db
        IJsonElement json = db.find("patterns", "id", pattern.getId());
        try {
            json.getAsJsonObject().add("isDeployed", true);
        } catch (JsonParseException e) {
            instance.getLogger().logError(e.toString());
        }
        db.update("patterns", "id", pattern.getId(), json);
        
        // return deployed pattern
        try {
            return jsonConv.fromJson(json);
        } catch (JsonException e) {
            instance.getLogger().logError(e.toString());
        }
        return null;
    }
    
    /**
     * Undeploys a pattern.
     * 
     * @param id
     *          the id of the {@link Pattern} to be undeployed.
     * @return
     *          the undeployed {@link Pattern}.   
     * @throws IllegalArgumentException
     *          if the pattern does not exist.
     * @throws IllegalStateException
     *          if the pattern is already undeployed.
     */
    public synchronized Pattern undeploy(String id) {
        IDatabaseConnector db = instance.getDBConn();
        
        // get pattern and check deployment status
        Pattern pattern = getById(id);
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern does not exist");
        } else if (!pattern.isDeployed()) {
            throw new IllegalStateException("Pattern is already undeployed");
        }
        
        // undeploy pattern
        instance.getEngine().undeploy(pattern);
        instance.getLogger().log(String.format(instance.getLocalizer().get(
                LocalizedString.PATTERN_UNDEPLOYED), pattern.getName()));
        
        // update deployment status in db
        IJsonElement json = db.find("patterns", "id", pattern.getId());
        try {
            json.getAsJsonObject().add("isDeployed", false);
        } catch (JsonParseException e) {
            instance.getLogger().logError(e.toString());
        }
        db.update("patterns", "id", pattern.getId(), json);
        
        // return undeployed pattern
        try {
            return jsonConv.fromJson(json);
        } catch (JsonException e) {
            instance.getLogger().logError(e.toString());
        }
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
    
    /**
     * Resets all patterns to be undeployed.
     */
    public void resetDeploymentStatus() {
        IDatabaseConnector db = instance.getDBConn();
        
        // for all deployed patterns
        Collection<Pattern> deployedPatterns = getAll().stream()
                .filter(pattern -> pattern.isDeployed()).collect(Collectors.toList());
        for (Pattern pattern : deployedPatterns) {
            
            // reset deployment status in db
            IJsonElement json = db.find("patterns", "id", pattern.getId());
            try {
                json.getAsJsonObject().add("isDeployed", false);
            } catch (JsonParseException e) {
                instance.getLogger().logError(e.toString());
            }
            db.update("patterns", "id", pattern.getId(), json);
        }
    }
}
