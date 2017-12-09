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
    private final AuthManager authManager;
    
    /**
     * Request handler used by the instance.
     */
    private final IRequestHandler reqHandler;
    
    /**
     * Socket handler used by the instance.
     */
    private final ISocketHandler sockHandler;
    
    
    /**
     * Constructs a new {@link ServerInstance}.
     * 
     * @param logger
     *          the {@link ILogger logger} used for the instance.
     */
    public ServerInstance(ILogger logger) {
        this.logger = Objects.requireNonNull(logger);
        
        // init server components
        dbConn = new MongoDBConnector(this, "");
        engine = new SiddhiEngine(this);
        userCtrl = new UserController(this);
        patternCtrl = new PatternController(this);
        sensorCtrl = new SensorController(this);
        authManager = new AuthManager(this);
        reqHandler = new SparkServer(this);
        sockHandler = new WebbitSocket(this);
    }
    
    /**
     * Starts all components of the server.
     */
    private void start() {
        
    }
    
    /**
     * Stops all components of the server.
     */
    private void stop() {
        
    }
    
    /**
     * Returns the logger used by the instance.
     * 
     * @return
     *          the {@link ILogger logger}.
     */
    public ILogger getLogger() {
        return logger;
    }
}
