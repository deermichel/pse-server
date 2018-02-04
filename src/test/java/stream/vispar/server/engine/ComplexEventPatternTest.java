package stream.vispar.server.engine;

import static org.junit.Assert.fail;

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

    private SiddhiEngine subject;
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
        this.subject = (SiddhiEngine) mockedInstance.getEngine();
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
     * Test method for {@link SiddhiEngine#deploy(Pattern)}. Tests that a simple
     * pattern is recognized and it's action is executed upon detection.
     * 
     * @throws Exception
     *             cause sometimes something goes wrong
     */
    @Test
    public void testDeploy() throws Exception {
        testPattern(ComplexEventPatterns.getMultipleOutputsPattern());
    	testPattern(ComplexEventPatterns.getSensorMultipleTimesPattern());
        testPattern(ComplexEventPatterns.getAggregationPattern());
        testPattern(ComplexEventPatterns.getFilterPattern());
        testPattern(ComplexEventPatterns.getConstantFilterPattern());
        testPattern(ComplexEventPatterns.getCountAggregationPattern());
        testPattern(ComplexEventPatterns.getLogicalPattern());
        testPattern(ComplexEventPatterns.getGoodWeatherPattern());
        testPattern(ComplexEventPatterns.getBadWeatherPattern());
        
        simulate("goodWeatherSimple");
        simulate("goodWeatherSimple2");
        simulate("goodWeatherNoEvent");
        simulate("goodWeatherNoEvent2");
        simulate("goodWeatherRandom");
    }
    
    
    private void testPattern(Pattern pattern) throws Exception {
//    	System.out.println(pattern.isValid());
    	
        mockedInstance.getPatternCtrl().update(pattern);
        
        try {
        	// deploy pattern
        	mockedInstance.getPatternCtrl().deploy(pattern.getId());
        } catch (Exception e) {
        	e.printStackTrace();
        	
//        	System.out.println(new TreeCompiler().compile(pattern).getAsString());
        	fail("exception was thrown");
        	return;
        }
        
//        System.out.println("deployed " + pattern.getName());
    }
    
    private void simulate(String simulation) throws InterruptedException {
    	System.out.println("simulate " + simulation);
    	new Simulation("./src/test/resources/simulations/" + simulation + ".sim").simulate(mockedInstance);
    	Thread.sleep(5000);
    	System.out.println();
    	System.out.println();
    }

}
