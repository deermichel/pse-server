package stream.vispar.server.core;

import java.util.Collection;

import stream.vispar.jsonconverter.types.IJsonElement;

/**
 * Defines functionality to connect to a (NoSQL) database.
 * 
 * @author Micha Hanselmann
 */
public interface IDatabaseConnector {
    
    /**
     * Connects to the database.
     */
    void connect();
    
    /**
     * Disconnects from the database.
     */
    void disconnect();

    /**
     * Creates a collection.
     * 
     * @param name
     *          the name of the collection.
     */
    void createCollection(String name);
    
    /**
     * Deletes a collection including all its content.
     * 
     * @param name
     *          the name of the collection.
     * @throws IllegalArgumentException
     *          if collection does not exist.
     */
    void dropCollection(String name);
    
    /**
     * Inserts data into a collection.
     * 
     * @param collection
     *          the name of the collection.
     * @param data
     *          a {@link IJsonElement} representation of the data.
     * @return
     *          a {@link IJsonElement} representation of the inserted database entry.
     * @throws IllegalArgumentException
     *          if collection does not exist.
     */
    IJsonElement insert(String collection, IJsonElement data);

    /**
     * Finds data by its identifier in a collection.
     * 
     * @param collection
     *          the name of the collection.
     * @param id
     *          the id of the requested data.
     * @return
     *          a {@link IJsonElement} representation of the data or null if not found.
     * @throws IllegalArgumentException
     *          if collection does not exist.
     */
    IJsonElement findById(String collection, String id);
    
    /**
     * Finds data by a key/value pair in a collection.
     * 
     * @param collection
     *          the name of the collection.
     * @param key
     *          the key of the requested data.
     * @param value
     *          the value for the key of the requested data.
     * @return
     *          a {@link IJsonElement} representation of the data or null if not found.
     * @throws IllegalArgumentException
     *          if collection does not exist.
     */
    IJsonElement find(String collection, String key, String value);
    
    /**
     * Returns all entries of a collection.
     * 
     * @param collection
     *          the name of the collection.
     * @return
     *          (Java) collection of {@link IJsonElement} representations of the data.
     * @throws IllegalArgumentException
     *          if collection does not exist.
     */
    Collection<IJsonElement> getAll(String collection);

    /**
     * Updates data by its identifier in a collection.
     * 
     * @param collection
     *          the name of the collection.
     * @param key
     *          the key of the data which should be updated.
     * @param value
     *          the value for the key of the data which should be updated.
     * @param data
     *          a {@link IJsonElement} representation of the new data.
     * @return
     *          a {@link IJsonElement} representation of the updated database entry.
     * @throws IllegalArgumentException
     *          if collection or entry does not exist.
     */
    IJsonElement update(String collection, String key, String value, IJsonElement data);
    
    /**
     * Deletes data by its identifier in a collection.
     * 
     * @param collection
     *          the name of the collection.
     * @param key
     *          the key of the data which should be deleted.
     * @param value
     *          the value for the key of the data which should be deleted.
     * @throws IllegalArgumentException
     *          if collection or entry does not exist.
     */
    void delete(String collection, String key, String value);
}
