/**
 * 
 */
package stream.vispar.server.engine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

/**
 * Tests for {@link EmailAction}.
 * 
 * @author Micha Hanselmann
 */
public class EmailActionTest {

    /**
     * Test method for {@link stream.vispar.server.engine.EmailAction#execute()}.
     */
    @Test
    public void testExecute() {
        EmailAction ea = new EmailAction("info@localhost", "Email Test", "From Vispar Server");
        ea.execute(); // hmm if it not fails - it might work
    }

    /**
     * Test method for {@link stream.vispar.server.engine.EmailAction#toString()}.
     */
    @Test
    public void testToString() {
        EmailAction ea = new EmailAction("rec", "subj", "msg");
        String result = ea.toString();
        assertThat(result, containsString("subject=subj"));
        assertThat(result, containsString("recipient=rec"));
        assertThat(result, containsString("message=msg"));
    }
}
