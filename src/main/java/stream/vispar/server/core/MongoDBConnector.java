package stream.vispar.server.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoDriverInformation;
import com.mongodb.client.model.Filters;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.exceptions.JsonSyntaxException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.server.localization.LocalizedString;

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
    private final MongoClientURI url;
    
    /**
     * MongoDB connection driver.
     */
    private MongoClient client;
    
    /**
     * MongoDB database.
     */
    private MongoDatabase database;
    
    /**
     * Json converter.
     */
    private final IJsonConverter jsonConv;
    

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
        this.url = new MongoClientURI("mongodb://" + Objects.requireNonNull(url));
        this.jsonConv = new GsonConverter();
    }

    @Override
    public void connect() {
        client = new MongoClient(url);
        database = client.getDatabase("vispar");

        try {
            client.getAddress();
            instance.getLogger().log(
                    String.format(instance.getLocalizer().get(LocalizedString.CONNECTED_TO_DATABASE), 
                            url));
        } catch (MongoException e) {
            instance.getLogger().logError(
                    String.format(instance.getLocalizer().get(LocalizedString.CANNOT_CONNECT_DATABASE), 
                            e.toString()));
            instance.stop();
            System.exit(1);
        }
    }

    @Override
    public void disconnect() {
        client.close();
    }

    @Override
    public void createCollection(String name) {
        database.createCollection(Objects.requireNonNull(name));
    }

    @Override
    public void dropCollection(String name) {
        database.getCollection(Objects.requireNonNull(name)).drop();
    }

    @Override
    public IJsonElement insert(String collection, IJsonElement data) {
        Document doc = Document.parse(data.toString());
        database.getCollection(collection).insertOne(doc);
        try {
            return jsonConv.fromString(doc.toJson());
        } catch (JsonParseException | JsonSyntaxException e) {
            instance.getLogger().logError(e.toString());
        }
        return null;
    }

    @Override
    public IJsonElement find(String collection, String key, String value) {
        Document doc = database.getCollection(collection).find(new Document(key, value)).first();
        if (doc == null) {
            return null;
        }
        try {
            return jsonConv.fromString(doc.toJson());
        } catch (JsonParseException | JsonSyntaxException e) {
            instance.getLogger().logError(e.toString());
        }
        return null;
    }

    @Override
    public Collection<IJsonElement> getAll(String collection) {
        HashSet<IJsonElement> result = new HashSet<>();
        try {
            for (Document doc : database.getCollection(collection).find()) {
                result.add(jsonConv.fromString(doc.toJson()));
            }
            return result;
        } catch (JsonParseException | JsonSyntaxException e) {
            instance.getLogger().logError(e.toString());
        }
        return null;
    }

    @Override
    public IJsonElement update(String collection, String key, String value, IJsonElement data) {
        Document doc = Document.parse(data.toString());
        database.getCollection(collection).replaceOne(new Document(key, value),  doc);
        try {
            return jsonConv.fromString(doc.toJson());
        } catch (JsonParseException | JsonSyntaxException e) {
            instance.getLogger().logError(e.toString());
        }
        return null;
    }

    @Override
    public void delete(String collection, String key, String value) {
        database.getCollection(collection).deleteOne(new Document(key, value));
    }
}
