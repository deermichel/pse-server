package stream.vispar.server.cli;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mockito.Mockito;

import stream.vispar.model.Pattern;
import stream.vispar.server.core.PatternController;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.ServerInstanceMock;
import stream.vispar.server.core.UserController;
import stream.vispar.server.core.entities.Event;
import stream.vispar.server.core.entities.User;
import stream.vispar.server.engine.IEngine;
import stream.vispar.server.localization.LocalizedString;
import stream.vispar.server.localization.Localizer;

/**
 * Tests for {@link Command}.
 * 
 * @author Micha Hanselmann
 */
public class CommandTest {
    
    /**
     * Test for Command.ADD_USER
     */
    @Test
    public void testAddUser() {
        // prepare mocked instance
        User user = new User("user1", "123");
        ServerInstance inst = spy(new ServerInstanceMock());
        UserController ctrl = mock(UserController.class);
        when(inst.getUserCtrl()).thenReturn(ctrl);
        
        // add user
        CommandResult result = Command.ADD_USER.handle(inst, "adduser user1 123");
        verify(ctrl, times(1)).add(user);
        assertThat(result.getMessage(), 
                equalTo(inst.getLocalizer().get(LocalizedString.OK)));
    }
    
    /**
     * Test for Command.REMOVE_USER
     */
    @Test
    public void testRemoveUser() {
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        UserController ctrl = mock(UserController.class);
        when(inst.getUserCtrl()).thenReturn(ctrl);
        
        // remove user
        CommandResult result = Command.REMOVE_USER.handle(inst, "removeuser user1");
        verify(ctrl, times(1)).remove("user1");
        assertThat(result.getMessage(), 
                equalTo(inst.getLocalizer().get(LocalizedString.OK)));
    }
    
    /**
     * Test for Command.LIST_USERS
     */
    @Test
    public void testListUsers() {
        // prepare mocked instance
        User user = new User("user1", "123");
        ServerInstance inst = spy(new ServerInstanceMock());
        UserController ctrl = mock(UserController.class);
        when(inst.getUserCtrl()).thenReturn(ctrl);
        when(ctrl.getAll()).thenReturn(Arrays.asList(user));
        
        // list users
        CommandResult result = Command.LIST_USERS.handle(inst, "listusers");
        verify(ctrl, times(1)).getAll();
        assertThat(result.getMessage(), 
                equalTo(String.format(inst.getLocalizer().get(LocalizedString.USERS), "user1")));
    }
    
    /**
     * Test for Command.LIST_PATTERNS
     */
    @Test
    public void testListPatterns() {
        // prepare mocked instance
        Pattern pattern = new Pattern("id1", true, "pat1");
        ServerInstance inst = spy(new ServerInstanceMock());
        PatternController ctrl = mock(PatternController.class);
        when(inst.getPatternCtrl()).thenReturn(ctrl);
        when(ctrl.getAll()).thenReturn(Arrays.asList(pattern));
        
        // list patterns
        CommandResult result = Command.LIST_PATTERNS.handle(inst, "listpatterns");
        verify(ctrl, times(1)).getAll();
        assertThat(result.getMessage(), 
                equalTo(String.format(inst.getLocalizer().get(LocalizedString.PATTERNS), "pat1 (deployed)")));
    }
    
    /**
     * Test for Command.SIMULATE
     */
    @Test
    public void testSimulate() {
        // prepare mocked instance
        ServerInstance inst = spy(new ServerInstanceMock());
        IEngine engine = mock(IEngine.class);
        when(inst.getEngine()).thenReturn(engine);
        inst.getSensorCtrl().registerSensors();
        
        // simulate
        CommandResult result = Command.SIMULATE.handle(inst, "simulate ./src/test/resources/simulations/temp.sim");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(engine, times(6)).sendEvent(Mockito.any(Event.class));
        assertThat(result.getMessage(), 
                equalTo(String.format(inst.getLocalizer().get(LocalizedString.RUNNING_SIMULATION), 
                        "./src/test/resources/simulations/temp.sim")));
    }
    
    /**
     * Test for Command.STOP
     */
    @Test
    public void testStop() {
        // prepare mocked instance
        Localizer loc = new Localizer(Locale.US);
        ServerInstance inst = mock(ServerInstance.class);
        when(inst.getLocalizer()).thenReturn(loc);
        
        // stop
        CommandResult result = Command.STOP_SERVER.handle(inst, "stop");
        verify(inst, times(1)).stop();
        assertThat(result.getMessage(), equalTo(loc.get(LocalizedString.SERVER_STOPPED)));
    }
    
    /**
     * Test for Command.HELP
     */
    @Test
    public void testHelp() {
        // prepare mocked instance
        Localizer loc = new Localizer(Locale.US);
        ServerInstance inst = mock(ServerInstance.class);
        when(inst.getLocalizer()).thenReturn(loc);
        
        // help
        CommandResult result = Command.HELP.handle(inst, "help");
        List<String> commands = Arrays.asList(Command.values()).stream()
                .map(c -> c.toString())
                .sorted((c1, c2) -> c1.compareToIgnoreCase(c2))
                .collect(Collectors.toList());
        assertThat(result.getMessage(), equalTo(String.format(
                inst.getLocalizer().get(LocalizedString.AVAILABLE_COMMANDS), 
                String.join(", ", commands))));
    }
}
