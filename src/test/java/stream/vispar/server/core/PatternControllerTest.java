/**
 * 
 */
package stream.vispar.server.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mockito.Mockito;

import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.model.Pattern;
import stream.vispar.server.engine.IEngine;

/**
 * Tests for {@link PatternController}.
 * 
 * @author Micha Hanselmann
 */
public class PatternControllerTest {

    /**
     * Test method for {@link PatternController(ServerInstance)}.
     */
    @Test(expected = NullPointerException.class)
    public void testPatternControllerNull() {
        new PatternController(null);
    }

    /**
     * Test method for {@link update(stream.vispar.model.Pattern)}.
     */
    @Test
    public void testUpdateCreate() {
        // prepare mocked instance
        Pattern pattern = new Pattern("id1", false, "name1");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IJsonElement jsonPattern = new GsonConverter().toJson(pattern);
        when(inst.getDBConn()).thenReturn(db);
        when(db.insert("patterns", jsonPattern)).thenReturn(jsonPattern);
        
        // remove
        PatternController ctrl = new PatternController(inst);
        Pattern result = ctrl.update(pattern);
        verify(db, times(1)).insert("patterns", jsonPattern);
        assertThat(result.getName(), equalTo(pattern.getName()));
        assertThat(result.getId(), equalTo(pattern.getId()));
    }

    /**
     * Test method for {@link update(stream.vispar.model.Pattern)}.
     */
    @Test
    public void testUpdateExisting() {
        // prepare mocked instance
        Pattern pattern = new Pattern("id1", false, "name1");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IJsonElement jsonPattern = new GsonConverter().toJson(pattern);
        when(inst.getDBConn()).thenReturn(db);
        when(db.find("patterns", "id", "id1")).thenReturn(jsonPattern);
        when(db.update("patterns", "id", "id1", jsonPattern)).thenReturn(jsonPattern);
        
        // remove
        PatternController ctrl = new PatternController(inst);
        Pattern result = ctrl.update(pattern);
        verify(db, times(1)).update("patterns", "id", "id1", jsonPattern);
        assertThat(result.getName(), equalTo(pattern.getName()));
        assertThat(result.getId(), equalTo(pattern.getId()));
    }

    /**
     * Test method for {@link remove(java.lang.String)}.
     */
    @Test
    public void testRemove() {
        // prepare mocked instance
        Pattern pattern = new Pattern("id1", false, "name1");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IJsonElement jsonPattern = new GsonConverter().toJson(pattern);
        when(db.find("patterns", "id", "id1")).thenReturn(jsonPattern);
        when(inst.getDBConn()).thenReturn(db);
        
        // remove
        PatternController ctrl = new PatternController(inst);
        ctrl.remove("id1");
        verify(db, times(1)).delete("patterns", "id", pattern.getId());
    }

    /**
     * Test method for {@link rename(java.lang.String, java.lang.String)}.
     * 
     * @throws JsonParseException 
     *              json problem.
     */
    @Test
    public void testRename() throws JsonParseException {
        // prepare mocked instance
        Pattern pattern = new Pattern("id1", false, "name1");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IJsonElement jsonPattern = new GsonConverter().toJson(pattern);
        when(db.find("patterns", "id", "id1")).thenReturn(jsonPattern);
        when(inst.getDBConn()).thenReturn(db);
        
        // rename
        PatternController ctrl = new PatternController(inst);
        Pattern result = ctrl.rename("id1", "renamed1");
        verify(db, times(1)).update("patterns", "id", pattern.getId(), jsonPattern);
        assertThat(result.getName(), equalTo("renamed1"));
        assertThat(result.getId(), equalTo(pattern.getId()));
    }

    /**
     * Test method for {@link deploy(java.lang.String)}.
     */
    @Test
    public void testDeploy() {
        // prepare mocked instance
        Pattern pattern = new Pattern("id1", false, "name1");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IEngine engine = mock(IEngine.class);
        IJsonElement jsonPattern = new GsonConverter().toJson(pattern);
        when(db.find("patterns", "id", "id1")).thenReturn(jsonPattern);
        when(inst.getDBConn()).thenReturn(db);
        when(inst.getEngine()).thenReturn(engine);
        
        assertThat(pattern.isDeployed(), equalTo(false));
        assertThat(pattern.isValid(), equalTo(true));
        
        // deploy
        PatternController ctrl = new PatternController(inst);
        Pattern result = ctrl.deploy("id1");
        verify(engine, times(1)).deploy(Mockito.any(Pattern.class));
        verify(db, times(1)).update("patterns", "id", pattern.getId(), jsonPattern);
        assertThat(result.getName(), equalTo(pattern.getName()));
        assertThat(result.getId(), equalTo(pattern.getId()));
        assertThat(result.isDeployed(), equalTo(true));
    }

    /**
     * Test method for {@link undeploy(java.lang.String)}.
     * 
     * @throws JsonException 
     *              bad json.
     */
    @Test
    public void testUndeploy() throws JsonException {
        // prepare mocked instance
        Pattern pattern = new Pattern("id1", false, "name1");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IEngine engine = mock(IEngine.class);
        IJsonElement jsonPattern = new GsonConverter().toJson(pattern);
        jsonPattern.getAsJsonObject().add("isDeployed", true);
        pattern = new GsonConverter().fromJson(jsonPattern);
        when(db.find("patterns", "id", "id1")).thenReturn(jsonPattern);
        when(inst.getDBConn()).thenReturn(db);
        when(inst.getEngine()).thenReturn(engine);
        
        assertThat(pattern.isDeployed(), equalTo(true));
        assertThat(pattern.isValid(), equalTo(true));
        
        // undeploy
        PatternController ctrl = new PatternController(inst);
        Pattern result = ctrl.undeploy("id1");
        verify(engine, times(1)).undeploy(Mockito.any(Pattern.class));
        verify(db, times(1)).update("patterns", "id", pattern.getId(), jsonPattern);
        assertThat(result.getName(), equalTo(pattern.getName()));
        assertThat(result.getId(), equalTo(pattern.getId()));
        assertThat(result.isDeployed(), equalTo(false));
    }

    /**
     * Test method for {@link getById(java.lang.String)}.
     */
    @Test
    public void testGetById() {
        // prepare mocked instance
        Pattern pattern = new Pattern("id1", false, "name1");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IJsonElement jsonPattern = new GsonConverter().toJson(pattern);
        when(db.find("patterns", "id", "id1")).thenReturn(jsonPattern);
        when(inst.getDBConn()).thenReturn(db);
        
        // get
        PatternController ctrl = new PatternController(inst);
        Pattern result = ctrl.getById("id1");
        verify(db, times(1)).find("patterns", "id", pattern.getId());
        assertThat(result.getName(), equalTo(pattern.getName()));
        assertThat(result.getId(), equalTo(pattern.getId()));
    }

    /**
     * Test method for {@link getAll()}.
     */
    @Test
    public void testGetAll() {
        // prepare mocked instance
        Pattern pattern1 = new Pattern("id1", false, "name1");
        Pattern pattern2 = new Pattern("id2", false, "name2");
        IJsonElement jsonPattern1 = new GsonConverter().toJson(pattern1);
        IJsonElement jsonPattern2 = new GsonConverter().toJson(pattern2);
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        when(db.getAll("patterns")).thenReturn(Arrays.asList(jsonPattern1, jsonPattern2));
        when(inst.getDBConn()).thenReturn(db);
        
        // get all
        PatternController ctrl = new PatternController(inst);
        Collection<String> patterns = ctrl.getAll().stream().map(p -> p.getId()).collect(Collectors.toList());
        verify(db, times(1)).getAll("patterns");
        assertThat(patterns, containsInAnyOrder(pattern1.getId(), pattern2.getId()));
    }

    /**
     * Test method for {@link resetDeploymentStatus()}.
     * 
     * @throws JsonException 
     *              bad json
     */
    @Test
    public void testResetDeploymentStatus() throws JsonException {
        // prepare mocked instance
        Pattern pattern = new Pattern("id1", false, "name1");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IJsonElement jsonPattern = new GsonConverter().toJson(pattern);
        jsonPattern.getAsJsonObject().add("isDeployed", true);
        pattern = new GsonConverter().fromJson(jsonPattern);
        when(db.getAll("patterns")).thenReturn(Arrays.asList(jsonPattern));
        when(db.find("patterns", "id", "id1")).thenReturn(jsonPattern);
        when(inst.getDBConn()).thenReturn(db);
        
        assertThat(pattern.isDeployed(), equalTo(true));
        
        // reset
        PatternController ctrl = new PatternController(inst);
        ctrl.resetDeploymentStatus();
        verify(db, times(1)).update("patterns", "id", pattern.getId(), jsonPattern);
    }
}
