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
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonObject;
import stream.vispar.jsonconverter.types.IJsonArray;
import stream.vispar.jsonconverter.types.IJsonObject;
import stream.vispar.model.Pattern;
import stream.vispar.server.cli.Command;
import stream.vispar.server.cli.CommandResult;
import stream.vispar.server.core.entities.User;
import stream.vispar.server.localization.LocalizedString;

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
    
}
