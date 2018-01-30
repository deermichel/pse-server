/**
 * 
 */
package stream.vispar.server.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import stream.vispar.server.core.entities.User;

/**
 * Tests for {@link AuthManager}.
 * 
 * @author Micha Hanselmann
 */
public class AuthManagerTest {

    /**
     * Test method for {@link AuthManager(ServerInstance)}.
     */
    @Test(expected = NullPointerException.class)
    public void testAuthManagerNull() {
        new AuthManager(null);
    }

    /**
     * Test method for {@link login(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testLogin() {
        // prepare mocked instance
        User user = new User("user", "123");
        ServerInstance inst = spy(new ServerInstanceMock());
        UserController userCtrl = mock(UserController.class);
        when(userCtrl.getByName(Mockito.anyString())).thenReturn(user);
        when(inst.getUserCtrl()).thenReturn(userCtrl);
        
        // login
        AuthManager auth = new AuthManager(inst);
        String token = auth.login("user", "123");
        assertThat(token.length(), greaterThan(100));
    }

    /**
     * Test method for {@link logout(java.lang.String)}.
     */
    @Test
    public void testLogout() {
        // prepare mocked instance
        User user = new User("user", "123");
        ServerInstance inst = spy(new ServerInstanceMock());
        UserController userCtrl = mock(UserController.class);
        when(userCtrl.getByName(Mockito.anyString())).thenReturn(user);
        when(inst.getUserCtrl()).thenReturn(userCtrl);
        
        // login
        AuthManager auth = new AuthManager(inst);
        String token = auth.login("user", "123");
        assertThat(token.length(), greaterThan(100));
        
        // authenticate
        assertThat(auth.authenticate(token), equalTo(user));
        
        // logout
        auth.logout(token);
        
        // authenticate not possible
        assertThat(auth.authenticate(token), equalTo(null));
    }

    /**
     * Test method for {@link authenticate(java.lang.String)}.
     */
    @Test
    public void testAuthenticate() {
        // prepare mocked instance
        User user = new User("user", "123");
        ServerInstance inst = spy(new ServerInstanceMock());
        UserController userCtrl = mock(UserController.class);
        when(userCtrl.getByName(Mockito.anyString())).thenReturn(user);
        when(inst.getUserCtrl()).thenReturn(userCtrl);
        
        // login
        AuthManager auth = new AuthManager(inst);
        String token = auth.login("user", "123");
        assertThat(token.length(), greaterThan(100));
        
        // authenticate
        assertThat(auth.authenticate(token), equalTo(user));
    }

    /**
     * Test method for {@link logoutAll(User)}.
     */
    @Test
    public void testLogoutAll() {
        // prepare mocked instance
        User user = new User("user", "123");
        ServerInstance inst = spy(new ServerInstanceMock());
        UserController userCtrl = mock(UserController.class);
        when(userCtrl.getByName(Mockito.anyString())).thenReturn(user);
        when(inst.getUserCtrl()).thenReturn(userCtrl);
        
        // login multiple times
        AuthManager auth = new AuthManager(inst);
        String token1 = auth.login("user", "123");
        String token2 = auth.login("user", "123");
        String token3 = auth.login("user", "123");
        assertThat(token1.length(), greaterThan(100));
        assertThat(token2.length(), greaterThan(100));
        assertThat(token3.length(), greaterThan(100));
        
        // authenticate
        assertThat(auth.authenticate(token1), equalTo(user));
        assertThat(auth.authenticate(token2), equalTo(user));
        assertThat(auth.authenticate(token3), equalTo(user));
        
        // logout all
        auth.logoutAll(user);
        
        // authenticate not possible
        assertThat(auth.authenticate(token1), equalTo(null));
        assertThat(auth.authenticate(token2), equalTo(null));
        assertThat(auth.authenticate(token3), equalTo(null));
    }
}
