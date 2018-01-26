/**
 * 
 */
package stream.vispar.server.logger;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link MultiLogger}.
 * 
 * @author Micha Hanselmann
 */
public class MultiLoggerTest {

    /**
     * Logger implementation for testing purposes
     */
    private class TestLogger implements ILogger {
        
        private String log = "";
        
        @Override
        public void log(String message) {
            log += message + "\n";
        }
    
        @Override
        public void logError(String error) {
            log += "[ERROR] " + error + "\n";
        }
        
        public String getLog() {
            return log;
        }
    }
    

    /**
     * Test method for {@link MultiLogger#log(String)}.
     */
    @Test
    public void testLog() {
        TestLogger t1 = new TestLogger();
        TestLogger t2 = new TestLogger();
        MultiLogger multi = new MultiLogger();
        multi.addLogger(t1);
        multi.addLogger(t2);
        
        multi.log("Hello Log.");
        assertThat(t1.getLog(), containsString("Hello Log.\n"));
        assertThat(t2.getLog(), containsString("Hello Log.\n"));
    }

    /**
     * Test method for {@link MultiLogger#logError(String)}.
     */
    @Test
    public void testLogError() {
        TestLogger t1 = new TestLogger();
        TestLogger t2 = new TestLogger();
        MultiLogger multi = new MultiLogger();
        multi.addLogger(t1);
        multi.addLogger(t2);
        
        multi.logError("Hello Error!");
        assertThat(t1.getLog(), containsString("[ERROR] Hello Error!\n"));
        assertThat(t2.getLog(), containsString("[ERROR] Hello Error!\n"));
    }

    /**
     * Test method for {@link MultiLogger#addLogger(ILogger)}.
     * 
     * Checks null argument behaviour.
     */
    @Test(expected = NullPointerException.class)
    public void testAddLoggerNull() {
        new MultiLogger().addLogger(null);
    }
}
