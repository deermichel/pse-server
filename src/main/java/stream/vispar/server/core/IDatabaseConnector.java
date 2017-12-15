package stream.vispar.server.core;

import java.util.Collection;

import todo.Json;

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
     *          a {@link Json} representation of the data.
     * @return
     *          a {@link Json} representation of the inserted database entry.
     * @throws IllegalArgumentException
     *          if collection does not exist.
     */
    Json insert(String collection, Json data);

    /**
     * Finds data by its identifier in a collection.
     * 
     * @param collection
     *          the name of the collection.
     * @param id
     *          the id of the requested data.
     * @return
     *          a {@link Json} representation of the data or null if not found.
     * @throws IllegalArgumentException
     *          if collection does not exist.
     */
    Json findById(String collection, String id);
    
    /**
     * Returns all entries of a collection.
     * 
     * @param collection
     *          the name of the collection.
     * @return
     *          (Java) collection of {@link Json} representations of the data.
     * @throws IllegalArgumentException
     *          if collection does not exist.
     */
    Collection<Json> getAll(String collection);

    /**
     * Updates data by its identifier in a collection.
     * 
     * @param collection
     *          the name of the collection.
     * @param id
     *          the id of the data which should be updated.
     * @param data
     *          a {@link Json} representation of the new data.
     * @return
     *          a {@link Json} representation of the updated database entry.
     * @throws IllegalArgumentException
     *          if collection or id does not exist.
     */
    Json update(String collection, String id, Json data);
    
    /**
     * Deletes data by its identifier in a collection.
     * 
     * @param collection
     *          the name of the collection.
     * @param id
     *          the id of the data which should be deleted.
     * @throws IllegalArgumentException
     *          if collection or id does not exist.
     */
    void delete(String collection, String id);
}
