package stream.vispar.server.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonObject;
import stream.vispar.jsonconverter.types.IJsonArray;
import stream.vispar.jsonconverter.types.IJsonObject;
import stream.vispar.model.Pattern;
import stream.vispar.model.nodes.Point;
import stream.vispar.model.nodes.inputs.ConstantDoubleNode;
import stream.vispar.model.nodes.inputs.ConstantIntegerNode;
import stream.vispar.model.nodes.outputs.PatternOutputNode;

/**
 * Tests for {@link ApiRoute}.
 * 
 * @author Micha Hanselmann
 */
public class ApiRouteTest {

    /**
     * Test for ApiRoute.POST_LOGIN
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testPostLogin() throws JsonException {
        // basics
        assertThat(ApiRoute.POST_LOGIN.getEndpoint(), equalTo("/auth/login"));
        assertThat(ApiRoute.POST_LOGIN.getType(), equalTo(RouteType.POST));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        AuthManager auth = mock(AuthManager.class);
        when(inst.getAuthMgr()).thenReturn(auth);
        when(auth.login("user123", "123456")).thenReturn("myToken");
        
        // create request
        IJsonObject request = new GsonJsonObject();
        IJsonObject requestData = new GsonJsonObject();
        requestData.add("username", "user123");
        requestData.add("password", "123456");
        request.add("data", requestData);
        
        // POST login
        IJsonObject result = ApiRoute.POST_LOGIN.execute(inst, request).getAsJsonObject()
                .get("data").getAsJsonObject();
        verify(auth, times(1)).login("user123", "123456");
        assertThat(result.has("token"), equalTo(true));
        assertThat(result.get("token").getAsJsonPrimitive().getAsString(), equalTo("myToken"));
    }

    /**
     * Test for ApiRoute.POST_LOGOUT
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testPostLogout() throws JsonException {
        // basics
        assertThat(ApiRoute.POST_LOGOUT.getEndpoint(), equalTo("/auth/logout"));
        assertThat(ApiRoute.POST_LOGOUT.getType(), equalTo(RouteType.POST));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        AuthManager auth = mock(AuthManager.class);
        when(inst.getAuthMgr()).thenReturn(auth);
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        request.add("token", "myToken");
        
        // POST logout
        IJsonObject result = ApiRoute.POST_LOGOUT.execute(inst, request).getAsJsonObject();
        verify(auth, times(1)).logout("myToken");
        assertThat(result.keySet().size(), equalTo(0));
    }

    /**
     * Test for ApiRoute.GET_USERS_ME
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testGetUsersMe() throws JsonException {
        // basics
        assertThat(ApiRoute.GET_USERS_ME.getEndpoint(), equalTo("/users/me"));
        assertThat(ApiRoute.GET_USERS_ME.getType(), equalTo(RouteType.GET));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        
        // GET users/me
        IJsonObject result = ApiRoute.GET_USERS_ME.execute(inst, request).getAsJsonObject()
                .get("data").getAsJsonObject();
        assertThat(result.has("username"), equalTo(true));
        assertThat(result.get("username").getAsJsonPrimitive().getAsString(), equalTo("user123"));
    }

    /**
     * Test for ApiRoute.GET_PATTERNS_ALL
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testGetPatternsAll() throws JsonException {
        // basics
        assertThat(ApiRoute.GET_PATTERNS_ALL.getEndpoint(), equalTo("/patterns/all"));
        assertThat(ApiRoute.GET_PATTERNS_ALL.getType(), equalTo(RouteType.GET));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        PatternController ctrl = mock(PatternController.class);
        Pattern pattern1 = new Pattern("id1", false, "my1stPattern");
        Pattern pattern2 = new Pattern("id2", false, "my2ndPattern");
        when(ctrl.getAll()).thenReturn(Arrays.asList(pattern1, pattern2));
        when(inst.getPatternCtrl()).thenReturn(ctrl);
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        
        // GET patterns/all
        IJsonArray result = ApiRoute.GET_PATTERNS_ALL.execute(inst, request).getAsJsonObject()
                .get("data").getAsJsonArray();
        verify(ctrl, times(1)).getAll();
        assertThat(result.size(), equalTo(2));
        IJsonConverter jsonConv = new GsonConverter();
        assertThat(result, containsInAnyOrder(
                jsonConv.toJson(pattern1.getAsProxy()), 
                jsonConv.toJson(pattern2.getAsProxy())));
    }

    /**
     * Test for ApiRoute.GET_PATTERNS
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testGetPatterns() throws JsonException {
        // basics
        assertThat(ApiRoute.GET_PATTERNS.getEndpoint(), equalTo("/patterns"));
        assertThat(ApiRoute.GET_PATTERNS.getType(), equalTo(RouteType.GET));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        PatternController ctrl = mock(PatternController.class);
        Pattern pattern = new Pattern("id1", false, "myPattern");
        when(ctrl.getById("id1")).thenReturn(pattern);
        when(inst.getPatternCtrl()).thenReturn(ctrl);
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        IJsonObject requestParams = new GsonJsonObject();
        requestParams.add("id", "id1");
        request.add("params", requestParams);
        
        // GET patterns
        IJsonObject result = ApiRoute.GET_PATTERNS.execute(inst, request).getAsJsonObject()
                .get("data").getAsJsonObject();
        verify(ctrl, times(1)).getById("id1");
        assertThat(result, equalTo(new GsonConverter().toJson(pattern)));
    }

    /**
     * Test for ApiRoute.POST_PATTERNS
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testPostPatterns() throws JsonException {
        // basics
        assertThat(ApiRoute.POST_PATTERNS.getEndpoint(), equalTo("/patterns"));
        assertThat(ApiRoute.POST_PATTERNS.getType(), equalTo(RouteType.POST));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        PatternController ctrl = mock(PatternController.class);
        Pattern pattern = new Pattern("id1", false, "myPattern");
        Pattern updatedPattern = new Pattern("id1", false, "myUpdatedPattern");
        when(ctrl.update(Mockito.any(Pattern.class))).thenReturn(updatedPattern);
        when(inst.getPatternCtrl()).thenReturn(ctrl);
        IJsonConverter jsonConv = new GsonConverter();
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        request.add("data", jsonConv.toJson(pattern));
        
        // POST patterns
        IJsonObject result = ApiRoute.POST_PATTERNS.execute(inst, request).getAsJsonObject()
                .get("data").getAsJsonObject();
        verify(ctrl, times(1)).update(Mockito.any(Pattern.class));
        assertThat(result, equalTo(new GsonConverter().toJson(updatedPattern)));
    }

    /**
     * Test for ApiRoute.DELETE_PATTERNS
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testDeletePatterns() throws JsonException {
        // basics
        assertThat(ApiRoute.DELETE_PATTERNS.getEndpoint(), equalTo("/patterns/delete"));
        assertThat(ApiRoute.DELETE_PATTERNS.getType(), equalTo(RouteType.POST));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        PatternController ctrl = mock(PatternController.class);
        when(inst.getPatternCtrl()).thenReturn(ctrl);
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        IJsonObject requestData = new GsonJsonObject();
        requestData.add("id", "id1");
        request.add("data", requestData);
        
        // POST patterns/delete
        IJsonObject result = ApiRoute.DELETE_PATTERNS.execute(inst, request).getAsJsonObject();
        verify(ctrl, times(1)).remove("id1");
        assertThat(result.keySet().size(), equalTo(0));
    }

    /**
     * Test for ApiRoute.RENAME_PATTERNS
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testRenamePatterns() throws JsonException {
        // basics
        assertThat(ApiRoute.RENAME_PATTERNS.getEndpoint(), equalTo("/patterns/rename"));
        assertThat(ApiRoute.RENAME_PATTERNS.getType(), equalTo(RouteType.POST));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        PatternController ctrl = mock(PatternController.class);
        Pattern renamedPattern = new Pattern("id1", false, "myRenamedPattern");
        when(ctrl.rename("id1", "myRenamedPattern")).thenReturn(renamedPattern);
        when(inst.getPatternCtrl()).thenReturn(ctrl);
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        IJsonObject requestData = new GsonJsonObject();
        requestData.add("id", "id1");
        requestData.add("name", "myRenamedPattern");
        request.add("data", requestData);
        
        // POST patterns/rename
        IJsonObject result = ApiRoute.RENAME_PATTERNS.execute(inst, request).getAsJsonObject()
                .get("data").getAsJsonObject();
        verify(ctrl, times(1)).rename("id1", "myRenamedPattern");
        assertThat(result, equalTo(new GsonConverter().toJson(renamedPattern)));
    }

    /**
     * Test for ApiRoute.POST_PATTERNS_DEPLOY
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testPostPatternsDeploy() throws JsonException {
        // basics
        assertThat(ApiRoute.POST_PATTERNS_DEPLOY.getEndpoint(), equalTo("/patterns/deploy"));
        assertThat(ApiRoute.POST_PATTERNS_DEPLOY.getType(), equalTo(RouteType.POST));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        PatternController ctrl = mock(PatternController.class);
        Pattern deployedPattern = new Pattern("id1", true, "myPattern");
        when(ctrl.deploy("id1")).thenReturn(deployedPattern);
        when(inst.getPatternCtrl()).thenReturn(ctrl);
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        IJsonObject requestData = new GsonJsonObject();
        requestData.add("id", "id1");
        request.add("data", requestData);
        
        // POST patterns/deploy
        IJsonObject result = ApiRoute.POST_PATTERNS_DEPLOY.execute(inst, request).getAsJsonObject()
                .get("data").getAsJsonObject();
        verify(ctrl, times(1)).deploy("id1");
        assertThat(result, equalTo(new GsonConverter().toJson(deployedPattern)));
    }

    /**
     * Test for ApiRoute.POST_PATTERNS_UNDEPLOY
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testPostPatternsUndeploy() throws JsonException {
        // basics
        assertThat(ApiRoute.POST_PATTERNS_UNDEPLOY.getEndpoint(), equalTo("/patterns/undeploy"));
        assertThat(ApiRoute.POST_PATTERNS_UNDEPLOY.getType(), equalTo(RouteType.POST));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        PatternController ctrl = mock(PatternController.class);
        Pattern undeployedPattern = new Pattern("id1", false, "myPattern");
        when(ctrl.undeploy("id1")).thenReturn(undeployedPattern);
        when(inst.getPatternCtrl()).thenReturn(ctrl);
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        IJsonObject requestData = new GsonJsonObject();
        requestData.add("id", "id1");
        request.add("data", requestData);
        
        // POST patterns/undeploy
        IJsonObject result = ApiRoute.POST_PATTERNS_UNDEPLOY.execute(inst, request).getAsJsonObject()
                .get("data").getAsJsonObject();
        verify(ctrl, times(1)).undeploy("id1");
        assertThat(result, equalTo(new GsonConverter().toJson(undeployedPattern)));
    }

    /**
     * Test for ApiRoute.GET_PATTERNS_INPUTNODES
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testGetPatternsInputnodes() throws JsonException {
        // basics
        assertThat(ApiRoute.GET_PATTERNS_INPUTNODES.getEndpoint(), equalTo("/patterns/inputnodes"));
        assertThat(ApiRoute.GET_PATTERNS_INPUTNODES.getType(), equalTo(RouteType.GET));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        PatternController ctrl = mock(PatternController.class);
        Pattern pattern1 = new Pattern("id1", false, "my1stPattern");
        ConstantIntegerNode ci = new ConstantIntegerNode("asd", new Point(0, 0), 23);
        PatternOutputNode out = new PatternOutputNode("adw", new Point(0, 0), pattern1.getId());
        ConstantDoubleNode cd = new ConstantDoubleNode("asd", new Point(0, 0), 2.3);
        PatternOutputNode out2 = new PatternOutputNode("adw", new Point(0, 0), pattern1.getId());
        out.addInput(ci);
        out.setName("test123");
        out2.addInput(cd);
        out2.setName("test234");
        pattern1.addInputNode(ci);
        pattern1.addOutputNode(out);
        pattern1.addInputNode(cd);
        pattern1.addOutputNode(out2);
        when(ctrl.getAll()).thenReturn(Arrays.asList(pattern1));
        when(inst.getPatternCtrl()).thenReturn(ctrl);
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        
        // GET patterns/inputnodes
        IJsonArray result = ApiRoute.GET_PATTERNS_INPUTNODES.execute(inst, request).getAsJsonObject()
                .get("data").getAsJsonArray();
        verify(ctrl, times(1)).getAll();
        assertThat(result.size(), equalTo(2));
        IJsonConverter jsonConv = new GsonConverter();
        assertThat(result, containsInAnyOrder(
                jsonConv.toJson(out.getAsPatternInputNode()), 
                jsonConv.toJson(out2.getAsPatternInputNode())));
    }

    /**
     * Test for ApiRoute.GET_SENSORS
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testGetSensors() throws JsonException {
        // basics
        assertThat(ApiRoute.GET_SENSORS.getEndpoint(), equalTo("/sensors"));
        assertThat(ApiRoute.GET_SENSORS.getType(), equalTo(RouteType.GET));
        
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        SensorController ctrl = spy(inst.getSensorCtrl());
        when(inst.getSensorCtrl()).thenReturn(ctrl);
        ctrl.registerSensors();
        
        // create request
        IJsonObject request = new GsonJsonObject();
        request.add("user", "user123");
        
        // GET sensors
        IJsonArray result = ApiRoute.GET_SENSORS.execute(inst, request).getAsJsonObject()
                .get("data").getAsJsonArray();
        verify(ctrl, times(1)).getAll();
        assertThat(result.size(), equalTo(5));
        //assertThat(result.get(0), equalTo(new GsonConverter().toJson(ctrl.getByName("temp1").getSensorNode())));
    }
    
    /**
     * Test for unauthenticated request on GET /users/me route.
     * 
     * @throws JsonException bad json.
     */
    @Test
    public void testUnauthenticatedUsersMe() throws JsonException {
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        
        // create request
        IJsonObject request = new GsonJsonObject();
        
        // GET users/me
        IJsonObject result = ApiRoute.GET_USERS_ME.execute(inst, request).getAsJsonObject();
        assertThat(result.has("error"), equalTo(true));
        assertThat(result.getAsJsonPrimitive("error").getAsInt(), equalTo(RouteError.NOT_AUTHORIZED.getCode()));
    }
}
