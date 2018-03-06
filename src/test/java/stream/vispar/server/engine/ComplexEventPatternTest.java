package stream.vispar.server.engine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import stream.vispar.model.Pattern;
import stream.vispar.server.core.DBConnectorMock;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.ServerInstanceMock;
import stream.vispar.server.core.entities.Simulation;

/**
 * Tests different complex event patterns and simulates events.
 */
public class ComplexEventPatternTest {

    private ServerInstance mockedInstance;
    private DBConnectorMock mockedDB;

    /**
     * Sets up the test environment before each test.
     */
    @Before
    public void setUp() {
        this.mockedDB = new DBConnectorMock();
        this.mockedInstance = new ServerInstanceMock(mockedDB);
        mockedInstance.start();
    }

    /**
     * Cleans up the test environment after each test.
     */
    @After
    public void tearDown() {
        if (mockedInstance.isRunning()) {
            mockedInstance.stop();
        }
    }

    /**
     * Tries to deploy different complex event patterns and simulates some events.
     * 
     * @throws Exception
     *             cause sometimes something goes wrong
     */
    @Test
    public void testDeploy() throws Exception {
        testPattern(ComplexEventPatterns.getMultipleOutputsPattern());
        testPattern(ComplexEventPatterns.getSensorMultipleTimesPattern());

        Pattern aggregationPattern = ComplexEventPatterns.getAggregationPattern();
        testPattern(aggregationPattern);
        simulate(aggregationPattern, "tempAll");

        Pattern filterPattern = ComplexEventPatterns.getFilterPattern();
        testPattern(filterPattern);
        simulate(filterPattern, "tempAll");

        Pattern constantFilterPattern = ComplexEventPatterns.getConstantFilterPattern();
        testPattern(constantFilterPattern);
        simulate(constantFilterPattern, "tempAll");

        Pattern countAggregationPattern = ComplexEventPatterns.getCountAggregationPattern();
        testPattern(countAggregationPattern);
        simulate(countAggregationPattern, "tempAll");

        Pattern logicalPattern = ComplexEventPatterns.getLogicalPattern();
        testPattern(logicalPattern);
        simulate(logicalPattern, "tempAll");

        Pattern badWeatherPattern = ComplexEventPatterns.getBadWeatherPattern();
        testPattern(badWeatherPattern);
        simulate(badWeatherPattern, "badWeatherSimple");

        Pattern goodWeatherPattern = ComplexEventPatterns.getGoodWeatherPattern();
        testPattern(goodWeatherPattern);
        simulate(goodWeatherPattern, "goodWeatherSimple");
//        simulate(goodWeatherPattern, "goodWeatherSimple2");
//        simulate(goodWeatherPattern, "goodWeatherNoEvent");
//        simulate(goodWeatherPattern, "goodWeatherNoEvent2");
//        simulate(goodWeatherPattern, "goodWeatherRandom");
    }

    private void testPattern(Pattern pattern) throws Exception {
        mockedInstance.getPatternCtrl().update(pattern);

        // deploy pattern
        mockedInstance.getPatternCtrl().deploy(pattern.getId());
        mockedInstance.getPatternCtrl().undeploy(pattern.getId());
    }

    private void simulate(Pattern pattern, String simulation) throws Exception {
//        System.out.println("simulate " + simulation);

        mockedInstance.getPatternCtrl().deploy(pattern.getId());
        new Simulation("./src/test/resources/simulations/" + simulation + ".sim").simulate(mockedInstance);
//        Thread.sleep(3000);

        mockedInstance.getPatternCtrl().undeploy(pattern.getId());
    }

}
