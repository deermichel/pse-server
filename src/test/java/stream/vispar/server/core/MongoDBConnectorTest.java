/**
 * 
 */
package stream.vispar.server.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

import java.util.Locale;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;

import stream.vispar.server.cli.DefaultConsole;
import stream.vispar.server.logger.ConsoleLogger;
import stream.vispar.server.logger.ILogger;

/**
 * Tests for {@link MongoDBConnector}.
 * 
 * @author Micha Hanselmann
 */
public class MongoDBConnectorTest {
    
    private ServerInstance instance;
    private MongoClient client;
    

    /**
     * Create fake instance and mongo db client.
     */
    @Before
    public void setUp() {
        ILogger logger = new ConsoleLogger(new DefaultConsole(), false);
        ServerConfig config = new ServerConfig(8888, 8889, Locale.US, logger, "db", "");
        instance = new ServerInstance(config);
        
        client = new MongoClient();
        System.out.println(client.getDatabase("admin").getCollection("system.version").find(new Document("version", "")));
    }
    
    /**
     * Close and cleanup mongo db client.
     */
    @After
    public void tearDown() {
        client.getDatabase("vispar").getCollection("hello_coll").drop();
        client.close();
    }

    /**
     * Test method for {@link MongoDBConnector#MongoDBConnector(ServerInstance, String)}.
     */
    @Test
    public void testMongoDBConnector() {
        MongoDBConnector conn = new MongoDBConnector(instance, "localhost");
        conn.connect();
        conn.disconnect();
        // TODO test?
    }

    /**
     * Test method for {@link MongoDBConnector#MongoDBConnector(ServerInstance, String)}.
     * 
     * Checks for null behaviour.
     */
    @Test(expected = NullPointerException.class)
    public void testMongoDBConnectorNull1() {
        new MongoDBConnector(null, "localhost");
    }

    /**
     * Test method for {@link MongoDBConnector#MongoDBConnector(ServerInstance, String)}.
     * 
     * Checks for null behaviour.
     */
    @Test(expected = NullPointerException.class)
    public void testMongoDBConnectorNull2() {
        new MongoDBConnector(instance, null);
    }

    /**
     * Test method for {@link MongoDBConnector#createCollection(String)}.
     */
    @Test
    public void testCreateCollection() {
        MongoDBConnector conn = new MongoDBConnector(instance, "localhost");
        conn.connect();
        conn.createCollection("hello_coll");
        conn.disconnect();
        
        assertThat(client.getDatabase("vispar").listCollectionNames(), contains("hello_coll"));
    }

    /**
     * Test method for {@link MongoDBConnector#createCollection(String)}.
     * 
     * Checks for null behaviour.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateCollectionNull() {
        new MongoDBConnector(instance, "localhost").createCollection(null);
    }

    /**
     * Test method for {@link MongoDBConnector#dropCollection(String)}.
     */
    @Test
    public void testDropCollection() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link MongoDBConnector#insert(String, IJsonElement)}.
     */
    @Test
    public void testInsert() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link MongoDBConnector#findById(String, String)}.
     */
    @Test
    public void testFindById() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link MongoDBConnector#getAll(String)}.
     */
    @Test
    public void testGetAll() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link MongoDBConnector#update(String, String, IJsonElement)}.
     */
    @Test
    public void testUpdate() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link MongoDBConnector#delete(String, String)}.
     */
    @Test
    public void testDelete() {
        fail("Not yet implemented");
    }
}
