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

import org.junit.Test;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.server.core.entities.User;

/**
 * Tests for {@link UserController}.
 * 
 * @author Micha Hanselmann
 */
public class UserControllerTest {

    /**
     * Test method for {@link UserController(ServerInstance)}.
     */
    @Test(expected = NullPointerException.class)
    public void testUserControllerNull() {
        new UserController(null);
    }

    /**
     * Test method for {@link add(entities.User)}.
     */
    @Test
    public void testAdd() {
        // prepare mocked instance
        User user = new User("user", "123");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IJsonElement jsonUser = new GsonConverter().toJson(user);
        when(db.insert("users", jsonUser)).thenReturn(jsonUser);
        when(inst.getDBConn()).thenReturn(db);
        
        // add
        UserController ctrl = new UserController(inst);
        User newUser = ctrl.add(user);
        verify(db, times(1)).insert("users", jsonUser);
        assertThat(newUser, equalTo(user));
        assertThat(newUser.checkPassword("123"), equalTo(true));
    }

    /**
     * Test method for {@link remove(java.lang.String)}.
     */
    @Test
    public void testRemove() {
        // prepare mocked instance
        User user = new User("user", "123");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IJsonElement jsonUser = new GsonConverter().toJson(user);
        when(db.find("users", "name", "user")).thenReturn(jsonUser);
        when(inst.getDBConn()).thenReturn(db);
        
        // remove
        UserController ctrl = new UserController(inst);
        ctrl.remove("user");
        verify(db, times(1)).delete("users", "name", user.getName());
    }

    /**
     * Test method for {@link getByName(java.lang.String)}.
     */
    @Test
    public void testGetByName() {
        // prepare mocked instance
        User user = new User("user", "123");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IJsonElement jsonUser = new GsonConverter().toJson(user);
        when(db.find("users", "name", "user")).thenReturn(jsonUser);
        when(inst.getDBConn()).thenReturn(db);
        
        // get
        UserController ctrl = new UserController(inst);
        User foundUser = ctrl.getByName("user");
        verify(db, times(1)).find("users", "name", user.getName());
        assertThat(foundUser, equalTo(user));
        assertThat(foundUser.checkPassword("123"), equalTo(true));
    }

    /**
     * Test method for {@link getAll()}.
     */
    @Test
    public void testGetAll() {
        // prepare mocked instance
        User user1 = new User("user1", "123");
        User user2 = new User("user2", "123");
        ServerInstance inst = spy(new ServerInstanceMock());
        IDatabaseConnector db = mock(IDatabaseConnector.class);
        IJsonElement jsonUser1 = new GsonConverter().toJson(user1);
        IJsonElement jsonUser2 = new GsonConverter().toJson(user2);
        when(db.getAll("users")).thenReturn(Arrays.asList(jsonUser1, jsonUser2));
        when(inst.getDBConn()).thenReturn(db);
        
        // get all
        UserController ctrl = new UserController(inst);
        Collection<User> users = ctrl.getAll();
        verify(db, times(1)).getAll("users");
        assertThat(users, containsInAnyOrder(user1, user2));
    }
}
