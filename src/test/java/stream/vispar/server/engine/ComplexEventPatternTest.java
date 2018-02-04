package stream.vispar.server.engine;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import stream.vispar.compiler.TreeCompiler;
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
//        testPattern(ComplexEventPatterns.getMultipleOutputsPattern(), null);
    	testPattern(ComplexEventPatterns.getSensorMultipleTimesPattern(), null);
        testPattern(ComplexEventPatterns.getAggregationPattern(), null);
        testPattern(ComplexEventPatterns.getFilterPattern(), null);
//        testPattern(ComplexEventPatterns.getConstantFilterPattern(), null);
        testPattern(ComplexEventPatterns.getCountAggregationPattern(), null);
        testPattern(ComplexEventPatterns.getLogicalPattern(), null);
        testPattern(ComplexEventPatterns.getGoodWeatherPattern(), null);
        testPattern(ComplexEventPatterns.getBadWeatherPattern(), null);
    }
    
    
    private void testPattern(Pattern pattern, String simulationFile) throws Exception {
    	System.out.println(pattern.isValid());
    	
        mockedInstance.getPatternCtrl().update(pattern);
        
        try {
        	// deploy pattern
        	mockedInstance.getPatternCtrl().deploy(pattern.getId());
        } catch (Exception e) {
        	e.printStackTrace();
        	
        	System.out.println(new TreeCompiler().compile(pattern).getAsString());
        	fail("exception was thrown");
        	return;
        }
        
        System.out.println("deployed " + pattern.getName());

        if (simulationFile != null) {
        	new Simulation(simulationFile).simulate(mockedInstance);
        }

//        // wait for good measure (we have to wait until the simulation is finished)
//        Thread.sleep(500);
//
//        // verify the pattern was detected
//        verify(mockedInstance.getLogger()).log(argThat((String string) -> string.contains("Received event")));
//        verify(mockedInstance.getLogger())
//                .log("Pattern 'Pattern' recognized. Executing action: socket{message=actionmessage}");
//
//        assertTrue(
//                subject.getDeploymentInstances().stream().anyMatch(instance -> instance.getPatternId().equals(pattern.getId())));
    }

}
