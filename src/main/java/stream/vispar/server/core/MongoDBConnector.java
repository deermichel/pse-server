package stream.vispar.server.core;

import java.util.Collection;
import java.util.Objects;

import stream.vispar.jsonconverter.types.IJsonElement;

/**
 * Database connector implementation for MongoDB.
 * 
 * @author Micha Hanselmann
 */
public class MongoDBConnector implements IDatabaseConnector {
    
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
    public MongoDBConnector(ServerInstance instance, String url) {
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
    public IJsonElement insert(String collection, IJsonElement data) {
        return null;
    }

    @Override
    public IJsonElement findById(String collection, String id) {
        return null;
    }

    @Override
    public Collection<IJsonElement> getAll(String collection) {
        return null;
    }

    @Override
    public IJsonElement update(String collection, String id, IJsonElement data) {
        return null;
    }

    @Override
    public void delete(String collection, String id) {
        
    }
}
