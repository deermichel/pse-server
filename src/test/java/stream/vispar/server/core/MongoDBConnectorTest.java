/**
 * 
 */
package stream.vispar.server.core;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.oneOf;

import java.util.Collection;
import java.util.Locale;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;

import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonObject;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.jsonconverter.types.IJsonObject;
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
        ServerConfig config = new ServerConfig(8888, 8889, Locale.US, logger, "db", ".");
        instance = new ServerInstance(config);
        
        client = new MongoClient();
        System.out.println(client.getDatabase("admin").getCollection("system.version")
                .find(new Document("version", "")));
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
        assertThat(conn, instanceOf(MongoDBConnector.class));
        conn.connect();
        conn.disconnect();
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
        
        assertThat(client.getDatabase("vispar").listCollectionNames(), hasItem("hello_coll"));
    }

    /**
     * Test method for {@link MongoDBConnector#dropCollection(String)}.
     */
    @Test
    public void testDropCollection() {
        MongoDBConnector conn = new MongoDBConnector(instance, "localhost");
        conn.connect();
        conn.createCollection("hello_coll");
        assertThat(client.getDatabase("vispar").listCollectionNames(), hasItem("hello_coll"));
        
        // drop
        conn.dropCollection("hello_coll");
        assertThat(client.getDatabase("vispar").listCollectionNames(), not(hasItem("hello_coll")));
        conn.disconnect();
    }

    /**
     * Test method for {@link MongoDBConnector#insert(String, IJsonElement)}.
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testInsert() throws JsonException {
        MongoDBConnector conn = new MongoDBConnector(instance, "localhost");
        conn.connect();
        
        IJsonObject data = new GsonJsonObject();
        data.add("test", "hello");
        IJsonElement result = conn.insert("hello_coll", data);
        conn.disconnect();
        assertThat(result.getAsJsonObject().get("test").getAsJsonPrimitive().getAsString(), equalTo("hello"));
        
        IJsonElement manual = new GsonConverter().fromString(client.getDatabase("vispar")
                .getCollection("hello_coll").find().first().toJson());
        assertThat(manual.getAsJsonObject().get("test").getAsJsonPrimitive().getAsString(), equalTo("hello"));
    }

    /**
     * Test method for {@link MongoDBConnector#find(String, String, String)}.
     * 
     * @throws JsonParseException bad json.
     */
    @Test
    public void testFind() throws JsonParseException {
        MongoDBConnector conn = new MongoDBConnector(instance, "localhost");
        conn.connect();
        IJsonObject data = new GsonJsonObject();
        data.add("test", "hello");
        data.add("second", "value");
        client.getDatabase("vispar").getCollection("hello_coll").insertOne(Document.parse(data.toString()));
        
        IJsonElement result = conn.find("hello_coll", "test", "hello");
        conn.disconnect();
        assertThat(result.getAsJsonObject().get("second").getAsJsonPrimitive().getAsString(), equalTo("value"));
    }

    /**
     * Test method for {@link MongoDBConnector#getAll(String)}.
     * 
     * @throws JsonParseException bad json.
     */
    @Test
    public void testGetAll() throws JsonParseException {
        MongoDBConnector conn = new MongoDBConnector(instance, "localhost");
        conn.connect();
        IJsonObject data = new GsonJsonObject();
        data.add("test", "hello");
        client.getDatabase("vispar").getCollection("hello_coll").insertOne(Document.parse(data.toString()));
        data.add("test", "hello2");
        client.getDatabase("vispar").getCollection("hello_coll").insertOne(Document.parse(data.toString()));
    
        Collection<IJsonElement> result = conn.getAll("hello_coll");
        conn.disconnect();
        assertThat(result.size(), equalTo(2));
        for (IJsonElement e : result) {
            assertThat(e.getAsJsonObject().get("test").getAsJsonPrimitive().getAsString(), 
                    oneOf("hello", "hello2"));
        }
    }

    /**
     * Test method for {@link MongoDBConnector#update(String, String, String, IJsonElement)}.
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testUpdate() throws JsonException {
        MongoDBConnector conn = new MongoDBConnector(instance, "localhost");
        conn.connect();
        IJsonObject data = new GsonJsonObject();
        data.add("test", "hello");
        client.getDatabase("vispar").getCollection("hello_coll").insertOne(Document.parse(data.toString()));
        
        data.add("test", "hello2");
        IJsonElement result = conn.update("hello_coll", "test", "hello", data);
        conn.disconnect();
        assertThat(result.getAsJsonObject().get("test").getAsJsonPrimitive().getAsString(), equalTo("hello2"));
        
        IJsonElement manual = new GsonConverter().fromString(client.getDatabase("vispar")
                .getCollection("hello_coll").find().first().toJson());
        assertThat(manual.getAsJsonObject().get("test").getAsJsonPrimitive().getAsString(), equalTo("hello2"));
    }

    /**
     * Test method for {@link MongoDBConnector#delete(String, String, String)}.
     */
    @Test
    public void testDelete() {
        MongoDBConnector conn = new MongoDBConnector(instance, "localhost");
        conn.connect();
        IJsonObject data = new GsonJsonObject();
        data.add("test", "hello");
        client.getDatabase("vispar").getCollection("hello_coll").insertOne(Document.parse(data.toString()));
        
        conn.delete("hello_coll", "test", "hello");
        conn.disconnect();
        assertThat(client.getDatabase("vispar").getCollection("hello_coll").find().iterator().hasNext(), 
                equalTo(false));
    }
}