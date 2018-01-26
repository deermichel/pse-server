/**
 * 
 */
package stream.vispar.server.logger;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import stream.vispar.server.cli.IConsole;

/**
 * Tests for {@link ConsoleLogger}.
 * 
 * @author Micha Hanselmann
 */
public class ConsoleLoggerTest {
    
    private IConsole testConsole;
    private String consoleData;
    
    
    /**
     * Prepare test console.
     */
    @Before
    public void setUp() {
        testConsole = new IConsole() {
            @Override
            public String read() {
                return consoleData;
            }
            
            @Override
            public void println(String input) {
                consoleData = input;
            }
        };
    }
    
    /**
     * Cleanup.
     */
    @After
    public void tearDown() {
        consoleData = "";
    }

    /**
     * Test method for {@link ConsoleLogger#ConsoleLogger(IConsole, boolean)}.
     * 
     * Checks behavior on null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testConsoleLogger() {
        new ConsoleLogger(null, false);
    }

    /**
     * Test method for {@link ConsoleLogger#log(String)}.
     */
    @Test
    public void testLog() {
        ConsoleLogger logger = new ConsoleLogger(testConsole, false);
        logger.log("Hello Log!");
        assertThat(consoleData, equalTo("Hello Log!"));
    }

    /**
     * Test method for {@link ConsoleLogger#logError(String)}.
     */
    @Test
    public void testLogError() {
        ConsoleLogger logger = new ConsoleLogger(testConsole, true);
        logger.logError("Hello Error.");
        assertThat(consoleData, containsString("[ERROR] Hello Error."));
    }
}
