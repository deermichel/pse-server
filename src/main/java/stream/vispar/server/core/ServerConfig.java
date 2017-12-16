package stream.vispar.server.core;

import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;

import stream.vispar.server.logger.ILogger;

/**
 * Contains the configuration for a {@link ServerInstance}.
 * 
 * @author Micha Hanselmann
 */
public class ServerConfig {
    
    /**
     * Network port used by the api server.
     */
    private final int apiPort;
    
    /**
     * Network port used by the socket server.
     */
    private final int socketPort;
    
    /**
     * Locale used for the log/output.
     */
    private final Locale locale;
    
    /**
     * Logger used by the server.
     */
    private final ILogger logger;
    
    /**
     * Url of the database.
     */
    private final String databaseUrl;
    
    /**
     * Path to the config files of the sensors.
     */
    private final String sensorsConfigPath;
    
    

    /**
     * Constructs a new {@link ServerConfig}.
     * 
     * @param apiPort
     *          the network port used by the api server.
     * @param socketPort
     *          the network port used by the socket server.
     * @param locale
     *          the {@link Locale} used for the log/output.
     * @param logger
     *          the {@link Logger} used by the server.
     * @param databaseUrl
     *          the url for the database used by the server.
     * @param sensorsConfigPath
     *          the path to the config files of the sensors.
     */
    public ServerConfig(int apiPort, int socketPort, Locale locale, ILogger logger, String databaseUrl, String sensorsConfigPath) {
        this.apiPort = apiPort;
        this.socketPort = socketPort;
        this.locale = Objects.requireNonNull(locale);
        this.logger = Objects.requireNonNull(logger);
        this.databaseUrl = Objects.requireNonNull(databaseUrl);
        this.sensorsConfigPath = Objects.requireNonNull(sensorsConfigPath);
    }
    
    /**
     * Returns the network port used by the api server.
     * 
     * @return
     *          the port.
     */
    public int getApiPort() {
        return apiPort;
    }
    
    /**
     * Returns the network port used by the socket server.
     * 
     * @return
     *          the port.
     */
    public int getSocketPort() {
        return socketPort;
    }
    
    /**
     * Returns the locale used for the log/output.
     * 
     * @return
     *          the {@link Locale}.
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * Returns the logger used by the server.
     * 
     * @return
     *          the {@link ILogger}.
     */
    public ILogger getLogger() {
        return logger;
    }
    
    /**
     * Returns the url for the database used by the server.
     * 
     * @return
     *          the database url.
     */
    public String getDatabaseUrl() {
        return databaseUrl;
    }
    
    /**
     * Returns the path to the config files of the sensors.
     * 
     * @return
     *          the path.
     */
    public String getSensorsConfigPath() {
        return sensorsConfigPath;
    }
}
