package stream.vispar.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import stream.vispar.server.cli.CommandTest;
import stream.vispar.server.cli.DefaultConsoleTest;
import stream.vispar.server.cli.StringCommandResultTest;
import stream.vispar.server.core.AuthManagerTest;
import stream.vispar.server.core.MongoDBConnectorTest;
import stream.vispar.server.core.PatternControllerTest;
import stream.vispar.server.core.SensorControllerTest;
import stream.vispar.server.core.ServerConfigTest;
import stream.vispar.server.core.ServerInstanceTest;
import stream.vispar.server.core.SparkServerTest;
import stream.vispar.server.core.SparkSocketTest;
import stream.vispar.server.core.UserControllerTest;
import stream.vispar.server.core.entities.EventTest;
import stream.vispar.server.core.entities.SensorTest;
import stream.vispar.server.core.entities.SimulatedEventTest;
import stream.vispar.server.core.entities.SimulationTest;
import stream.vispar.server.core.entities.UserTest;
import stream.vispar.server.engine.EmailActionTest;
import stream.vispar.server.engine.EventActionTest;
import stream.vispar.server.engine.SocketActionTest;
import stream.vispar.server.localization.LocalizerTest;
import stream.vispar.server.logger.ConsoleLoggerTest;
import stream.vispar.server.logger.FileLoggerTest;
import stream.vispar.server.logger.MultiLoggerTest;

/**
 * Test suite for all tests.
 * 
 * @author Micha Hanselmann
 */
@RunWith(Suite.class)
@SuiteClasses({
    
    // main
    ServerAppTest.class,
    
    // cli
    CommandTest.class,
    DefaultConsoleTest.class,
    StringCommandResultTest.class,
    
    // core
    AuthManagerTest.class,
    MongoDBConnectorTest.class,
    PatternControllerTest.class,
    SensorControllerTest.class,
    ServerConfigTest.class,
    ServerInstanceTest.class,
    SparkServerTest.class,
    SparkSocketTest.class,
    UserControllerTest.class,
    
    // entities
    EventTest.class,
    SensorTest.class,
    SimulatedEventTest.class,
    SimulationTest.class,
    UserTest.class,
    
    // engine
    EmailActionTest.class,
    EventActionTest.class,
    SocketActionTest.class,
    
    // localization
    LocalizerTest.class,
    
    // logger
    ConsoleLoggerTest.class,
    FileLoggerTest.class,
    MultiLoggerTest.class
    
})
public class AllTests { }
