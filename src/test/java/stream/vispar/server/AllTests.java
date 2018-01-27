package stream.vispar.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import stream.vispar.server.cli.DefaultConsoleTest;
import stream.vispar.server.core.ServerConfigTest;
import stream.vispar.server.core.ServerInstanceTest;
import stream.vispar.server.core.SparkServerTest;
import stream.vispar.server.core.entities.EventTest;
import stream.vispar.server.core.entities.SensorTest;
import stream.vispar.server.core.entities.SimulatedEventTest;
import stream.vispar.server.core.entities.SimulationTest;
import stream.vispar.server.core.entities.UserTest;
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
    
    // cli
    DefaultConsoleTest.class,
    
    // core
    ServerConfigTest.class,
    ServerInstanceTest.class,
    SparkServerTest.class,
    
    // entities
    EventTest.class,
    SensorTest.class,
    SimulatedEventTest.class,
    SimulationTest.class,
    UserTest.class,
    
    // localization
    LocalizerTest.class,
    
    // logger
    ConsoleLoggerTest.class,
    FileLoggerTest.class,
    MultiLoggerTest.class
    
})
public class AllTests { }
