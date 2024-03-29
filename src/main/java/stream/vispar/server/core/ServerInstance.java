package stream.vispar.server.core;

import java.util.Objects;
import java.util.Optional;

import stream.vispar.server.engine.IEngine;
import stream.vispar.server.engine.SiddhiEngine;
import stream.vispar.server.localization.LocalizedString;
import stream.vispar.server.localization.Localizer;
import stream.vispar.server.logger.ILogger;

/**
 * Server instance holding and controlling all components of the server.
 * 
 * @author Micha Hanselmann
 */
public class ServerInstance {
    
    /**
     * Logger used by the instance.
     */
    private final ILogger logger;
    
    /**
     * Localizer used for the instance.
     */
    private final Localizer localizer;
    
    /**
     * Database connector used by the instance.
     */
    private final IDatabaseConnector dbConn;
    
    /**
     * Engine used by the instance.
     */
    private final IEngine engine;
    
    /**
     * User controller used by the instance.
     */
    private final UserController userCtrl;
    
    /**
     * Pattern controller used by the instance.
     */
    private final PatternController patternCtrl;
    
    /**
     * Sensor controller used by the instance.
     */
    private final SensorController sensorCtrl;
    
    /**
     * Authentication manager used by the instance.
     */
    private final AuthManager authMgr;
    
    /**
     * Request handler used by the instance.
     */
    private final IRequestHandler reqHandler;
    
    /**
     * Socket handler used by the instance.
     */
    private final ISocketHandler sockHandler;
    
    /**
     * Determines whether instance is running.
     */
    private boolean running;
    
    
    /**
     * Constructs a new {@link ServerInstance}.
     * 
     * @param config
     *          the {@link ServerConfig} used for the instance.
     */
    public ServerInstance(ServerConfig config) {
        this(config, null);
    }
    
    /**
     * Constructs a new {@link ServerInstance}. For testing purposes.
     * 
     * @param config
     *            the {@link ServerConfig} used for this instance.
     * @param dbConn
     *            the {@link IDatabaseConnector} used for this instance. A new
     *            {@link MongoDBConnector} is created if {@code null}.
     */
    protected ServerInstance(ServerConfig config, IDatabaseConnector dbConn) {
        this.running = false;
        Objects.requireNonNull(config);
        
        // init server components
        logger = config.getLogger();
        localizer = new Localizer(config.getLocale());
        this.dbConn = Optional.ofNullable(dbConn).orElse(new MongoDBConnector(this, config.getDatabaseUrl()));
        engine = new SiddhiEngine(this);
        userCtrl = new UserController(this);
        patternCtrl = new PatternController(this);
        sensorCtrl = new SensorController(this, config.getSensorsConfigPath());
        authMgr = new AuthManager(this);
        reqHandler = new SparkServer(this, config.getApiPort());
        sockHandler = new SparkSocket(this, config.getSocketPort());
    }
    
    /**
     * Starts all components of the server.
     */
    public void start() {
        if (isRunning()) {
            return;
        }
        this.running = true;
        logger.log(localizer.get(LocalizedString.STARTING_SERVER));
        dbConn.connect();
        patternCtrl.resetDeploymentStatus();
        sensorCtrl.registerSensors();
        engine.start();
        sockHandler.start();
        reqHandler.start();
    }
    
    /**
     * Stops all components of the server.
     */
    public void stop() {
        if (!isRunning()) {
            return;
        }
        this.running = false;
        logger.log(localizer.get(LocalizedString.STOPPING_SERVER));
        engine.stop();
        dbConn.disconnect();
        reqHandler.stop();
        sockHandler.stop();
    }
    
    /**
     * Returns whether instance is running.
     * 
     * @return
     *          true if running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Returns the logger used by the instance.
     * 
     * @return
     *          the {@link ILogger}.
     */
    public ILogger getLogger() {
        return logger;
    }
    
    /**
     * Returns the localizer used for the instance.
     * 
     * @return
     *          the {@link Localizer}.
     */
    public Localizer getLocalizer() {
        return localizer;
    }
    
    /**
     * Returns the database connector used by the instance.
     * 
     * @return
     *          the {@link IDatabaseConnector}.
     */
    public IDatabaseConnector getDBConn() {
        return dbConn;
    }
    
    /**
     * Returns the engine used by the instance.
     * 
     * @return
     *          the {@link IEngine}.
     */
    public IEngine getEngine() {
        return engine;
    }
    
    /**
     * Returns the user controller used by the instance.
     * 
     * @return
     *          the {@link UserController}.
     */
    public UserController getUserCtrl() {
        return userCtrl;
    }
    
    /**
     * Returns the pattern controller used by the instance.
     * 
     * @return
     *          the {@link PatternController}.
     */
    public PatternController getPatternCtrl() {
        return patternCtrl;
    }
    
    /**
     * Returns the sensor controller used by the instance.
     * 
     * @return
     *          the {@link SensorController}.
     */
    public SensorController getSensorCtrl() {
        return sensorCtrl;
    }
    
    /**
     * Returns the authentication manager used by the instance.
     * 
     * @return
     *          the {@link AuthManager}.
     */
    public AuthManager getAuthMgr() {
        return authMgr;
    }
    
    /**
     * Returns the socket handler used by the instance.
     * 
     * @return
     *          the {@link ISocketHandler}.
     */
    public ISocketHandler getSockHandler() {
        return sockHandler;
    }
}
