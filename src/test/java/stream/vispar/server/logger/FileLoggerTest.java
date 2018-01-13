/**
 * 
 */
package stream.vispar.server.logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

/**
 * Tests for {@link FileLogger}.
 * 
 * @author Micha Hanselmann
 */
public class FileLoggerTest {

    /**
     * Cleanup.
     * 
     * @throws Exception cause bad things sometimes happen.
     */
    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(Paths.get("test.log"));
        Files.deleteIfExists(Paths.get("testError.log"));
    }

    /**
     * Test method for {@link FileLogger#FileLogger(String, boolean)}.
     */
    @Test
    public void testFileLogger() {
        new FileLogger("test.log", false);
        assertThat(Files.exists(Paths.get("test.log")), equalTo(true));
    }

    /**
     * Test method for {@link FileLogger#FileLogger(String, boolean)}.
     * 
     * Checks behavior on null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testFileLoggerNull() {
        new FileLogger(null, false);
    }

    /**
     * Test method for {@link FileLogger#FileLogger(String, boolean)}.
     * 
     * Checks behavior on bad file.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFileLoggerBadFile() {
        new FileLogger("/", false);
    }

    /**
     * Test method for {@link FileLogger#log(String)}.
     * 
     * @throws Exception io problem.
     */
    @Test
    public void testLog() throws Exception {
        FileLogger logger = new FileLogger("test.log", true);
        logger.log("Hello Log!");
        List<String> lines = Files.readAllLines(Paths.get("test.log"));
        assertThat(lines.size(), equalTo(1));
        assertThat(lines.get(0), containsString("Hello Log!"));
    }

    /**
     * Test method for {@link FileLogger#logError(String)}.
     * 
     * @throws Exception io problem.
     */
    @Test
    public void testLogError() throws Exception {
        FileLogger logger = new FileLogger("testError.log", false);
        logger.logError("Oh no!");
        List<String> lines = Files.readAllLines(Paths.get("testError.log"));
        assertThat(lines.size(), equalTo(1));
        assertThat(lines.get(0), equalTo("[ERROR] Oh no!"));
    }
}
