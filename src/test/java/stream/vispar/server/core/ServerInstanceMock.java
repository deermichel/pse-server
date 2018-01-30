package stream.vispar.server.core;

import static org.mockito.Mockito.mock;

import java.util.Locale;

import stream.vispar.server.logger.ILogger;

/**
 * Mocked {@link ServerInstance} for testing purposes.
 * 
 * @author Micha Hanselmann
 */
public class ServerInstanceMock extends ServerInstance {

    /**
     * Construct new {@link ServerInstanceMock}.
     */
    public ServerInstanceMock() {
        super(getConfig());
    }
    
    private static ServerConfig getConfig() {
        ILogger logger = mock(ILogger.class);
        return new ServerConfig(8888, 8889, Locale.US, logger, "localhost", "./src/test/resources/sensors");
    }
}
