/**
 * 
 */
package stream.vispar.server.engine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import stream.vispar.server.core.ISocketHandler;

/**
 * Tests for {@link SocketAction}.
 * 
 * @author Micha Hanselmann
 */
public class SocketActionTest {

    /**
     * Test method for {@link SocketAction(ISocketHandler, java.lang.String)}.
     */
    @Test(expected = NullPointerException.class)
    public void testSocketActionNull1() {
        new SocketAction(null, "asd");
    }

    /**
     * Test method for {@link SocketAction(ISocketHandler, java.lang.String)}.
     */
    @Test(expected = NullPointerException.class)
    public void testSocketActionNull2() {
        ISocketHandler handler = mock(ISocketHandler.class);
        new SocketAction(handler, null);
    }

    /**
     * Test method for {@link execute()}.
     */
    @Test
    public void testExecute() {
        ISocketHandler handler = mock(ISocketHandler.class);
        new SocketAction(handler, "Hello Socket!").execute();
        verify(handler, times(1)).sendMessage("Hello Socket!");
    }

    /**
     * Test method for {@link toString()}.
     */
    @Test
    public void testToString() {
        ISocketHandler handler = mock(ISocketHandler.class);
        String result = new SocketAction(handler, "Hello Socket!").toString();
        assertThat(result, equalTo("{message=Hello Socket!}"));
    }
}
