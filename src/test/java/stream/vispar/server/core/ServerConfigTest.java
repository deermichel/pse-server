/**
 * 
 */
package stream.vispar.server.core;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;

import stream.vispar.server.logger.ILogger;

/**
 * Tests for {@link ServerConfig}.
 * 
 * @author Micha Hanselmann
 */
public class ServerConfigTest {
    
    private ILogger logger;
    
    
    /**
     * Prepare test.
     */
    @Before
    public void setUp() {
        logger = mock(ILogger.class);
    }

    /**
     * Test method for {@link ServerConfig#ServerConfig(int, int, Locale, ILogger, String, String)}.
     */
    @Test
    public void testServerConfig() {
        ServerConfig config = new ServerConfig(80, 81, Locale.US, logger, "databaseUrl", "configPath");
        assertThat(config.getApiPort(), equalTo(80));
        assertThat(config.getSocketPort(), equalTo(81));
        assertThat(config.getLocale(), equalTo(Locale.US));
        assertThat(config.getLogger(), sameInstance(logger));
        assertThat(config.getDatabaseUrl(), equalTo("databaseUrl"));
        assertThat(config.getSensorsConfigPath(), equalTo("configPath"));
    }

    /**
     * Test method for {@link ServerConfig#ServerConfig(int, int, Locale, ILogger, String, String)}.
     * 
     * Checks behavior on null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testServerConfigNull1() {
        new ServerConfig(80, 81, null, logger, "databaseUrl", "configPath");
    }

    /**
     * Test method for {@link ServerConfig#ServerConfig(int, int, Locale, ILogger, String, String)}.
     * 
     * Checks behavior on null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testServerConfigNull2() {
        new ServerConfig(80, 81, Locale.US, null, "databaseUrl", "configPath");
    }

    /**
     * Test method for {@link ServerConfig#ServerConfig(int, int, Locale, ILogger, String, String)}.
     * 
     * Checks behavior on null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testServerConfigNull3() {
        new ServerConfig(80, 81, Locale.US, logger, null, "configPath");
    }

    /**
     * Test method for {@link ServerConfig#ServerConfig(int, int, Locale, ILogger, String, String)}.
     * 
     * Checks behavior on null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testServerConfigNull4() {
        new ServerConfig(80, 81, Locale.US, logger, "databaseUrl", null);
    }

    /**
     * Test method for {@link ServerConfig#ServerConfig(int, int, Locale, ILogger, String, String)}.
     * 
     * Checks behavior on invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testServerConfigInvalid1() {
        new ServerConfig(-1, 81, Locale.US, logger, "databaseUrl", "configPath");
    }

    /**
     * Test method for {@link ServerConfig#ServerConfig(int, int, Locale, ILogger, String, String)}.
     * 
     * Checks behavior on invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testServerConfigInvalid2() {
        new ServerConfig(80, 100081, Locale.US, logger, "databaseUrl", "configPath");
    }

    /**
     * Test method for {@link ServerConfig#ServerConfig(int, int, Locale, ILogger, String, String)}.
     * 
     * Checks behavior on invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testServerConfigInvalid3() {
        new ServerConfig(80, -23, Locale.US, logger, "databaseUrl", "configPath");
    }

    /**
     * Test method for {@link ServerConfig#ServerConfig(int, int, Locale, ILogger, String, String)}.
     * 
     * Checks behavior on invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testServerConfigInvalid4() {
        new ServerConfig(123480, 23, Locale.US, logger, "databaseUrl", "configPath");
    }

    /**
     * Test method for {@link ServerConfig#ServerConfig(int, int, Locale, ILogger, String, String)}.
     * 
     * Checks behavior on invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testServerConfigInvalid5() {
        new ServerConfig(80, 80, Locale.US, logger, "databaseUrl", "configPath");
    }
}
