/**
 * 
 */
package stream.vispar.server.cli;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

/**
 * Tests for {@link StringCommandResult}.
 * 
 * @author Micha Hanselmann
 */
public class StringCommandResultTest {

    /**
     * Test method for {@link StringCommandResult#getMessage()}.
     */
    @Test
    public void testGetMessage() {
        assertThat(new StringCommandResult("Test Message").getMessage(), equalTo("Test Message"));
    }
}
