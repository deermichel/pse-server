package stream.vispar.server.core;

import java.util.Objects;

import stream.vispar.server.cli.Command;
import stream.vispar.server.cli.CommandParser;
import stream.vispar.server.cli.CommandResult;
import stream.vispar.server.engine.IEngine;
import stream.vispar.server.engine.SiddhiEngine;
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
     * @param logger
     *          the {@link ILogger logger} used for the instance.
     */
    public ServerInstance(ILogger logger) {
        this.running = false;
        this.logger = Objects.requireNonNull(logger);
        
        // init server components
        dbConn = new MongoDBConnector(this, "");
        engine = new SiddhiEngine(this);
        userCtrl = new UserController(this);
        patternCtrl = new PatternController(this);
        sensorCtrl = new SensorController(this, "");
        authMgr = new AuthManager(this);
        reqHandler = new SparkServer(this, 0);
        sockHandler = new WebbitSocket(this, 0);
    }
    
    /**
     * Starts all components of the server.
     */
    public void start() {
        dbConn.connect();
        engine.start();
        reqHandler.start();
        sockHandler.start();
        this.running = true;
    }
    
    /**
     * Stops all components of the server.
     */
    public void stop() {
        dbConn.disconnect();
        engine.stop();
        reqHandler.stop();
        sockHandler.stop();
        this.running = false;
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
     * Returns the request handler used by the instance.
     * 
     * @return
     *          the {@link IRequestHandler}.
     */
    public IRequestHandler getReqHandler() {
        return reqHandler;
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
