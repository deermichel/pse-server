package stream.vispar.server.core;

import java.util.Collection;
import java.util.Objects;

import todo.Json;

/**
 * Database connector implementation for MongoDB.
 * 
 * @author Micha Hanselmann
 */
class MongoDBConnector implements IDatabaseConnector {
    
    /**
     * Server instance the connector belongs to.
     */
    private final ServerInstance instance;
    
    /**
     * Url of the MongoDB database.
     */
    private final String url;
    

    /**
     * Constructs a new {@link MongoDBConnector}.
     * 
     * @param instance
     *          the {@link ServerInstance} the connector belongs to.
     * @param url
     *          the url of the MongoDB database to connect to.
     */
    MongoDBConnector(ServerInstance instance, String url) {
        this.instance = Objects.requireNonNull(instance);
        this.url = Objects.requireNonNull(url);
    }

    @Override
    public void connect() {
        
    }

    @Override
    public void disconnect() {
        
    }

    @Override
    public void createCollection(String name) {
        
    }

    @Override
    public void dropCollection(String name) {
        
    }

    @Override
    public Json insert(String collection, Json data) {
        return null;
    }

    @Override
    public Json findById(String collection, String id) {
        return null;
    }

    @Override
    public Collection<Json> getAll(String collection) {
        return null;
    }

    @Override
    public Json update(String collection, String id, Json data) {
        return null;
    }

    @Override
    public void delete(String collection, String id) {
        
    }
}
