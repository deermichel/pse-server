package stream.vispar.server.engine;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.util.Objects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import stream.vispar.model.Pattern;
import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.Operand;
import stream.vispar.model.nodes.Point;
import stream.vispar.model.nodes.inputs.SensorNode;
import stream.vispar.model.nodes.outputs.SocketActionNode;
import stream.vispar.server.core.DBConnectorMock;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.ServerInstanceMock;
import stream.vispar.server.core.entities.Simulation;
import stream.vispar.server.localization.LocalizedString;

/**
 * Test class for {@link SiddhiEngine}.
 * 
 * @author Nico Weidmann
 */
public class SiddhiEngineTest {

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
     * Tests if a {@link SiddhiEngine} instance can be constructed without
     * exceptions.
     */
    @Test
    public void testSiddhiEngine() {

        new SiddhiEngine(new ServerInstanceMock());
    }

    /**
     * Test method for
     * {@link SiddhiEngine#SiddhiEngine(stream.vispar.server.core.ServerInstance)}
     * when passing {@code null}.
     */
    @Test(expected = NullPointerException.class)
    public void testSiddhiEngineNull() {
        new SiddhiEngine(null);
    }

    /**
     * Test method for {@link SiddhiEngine#start()}.
     */
    @Test
    public void testStart() {
        verify(mockedInstance.getLogger())
                .log(mockedInstance.getLocalizer().get(LocalizedString.SIDDHI_ENGINE_STARTED));
    }

    /**
     * Test method for {@link SiddhiEngine#stop()}. Tests that after the engine has
     * been stopped, no more patterns are detected.
     */
    @Test
    public void testStop() {

        Pattern toDeploy = new Pattern("id", false, "Pattern");
        SensorNode sensor = new SensorNode("sensornode", new Point(0, 0), "temp1", "Temperature sensor", new Operand(
                (mockedInstance.getSensorCtrl().getByName("temp1").getAttributes().toArray(new Attribute[0]))));

        SocketActionNode action = new SocketActionNode("actionnode", new Point(0, 0));
        action.setMessage("actionmessage");

        toDeploy.addInputNode(sensor);
        toDeploy.addOutputNode(action);
        sensor.setOutput(action);

        mockedInstance.getPatternCtrl().update(toDeploy);

        // deploy pattern
        mockedInstance.getPatternCtrl().deploy("id");

        mockedInstance.stop();

        assertTrue(subject.getDeploymentInstances().isEmpty());
        mockedInstance.start();
    }

    /**
     * Test method for {@link SiddhiEngine#deploy(Pattern)} when passing a pattern
     * that cannot be found in the pattern controller. Should fail in some way.
     */
    @Test(expected = Throwable.class)
    public void testDeployNonexistentPattern() {
        if (Objects.nonNull(mockedInstance.getPatternCtrl().getById(""))) {
            // the id should not exist, cannot proceed with test
            fail("cannot proceed with test, need nonexistent id.");
        }
        subject.deploy(new Pattern("", false, "Test Pattern"));
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

        // prepare a pattern we can deploy
        Pattern toDeploy = new Pattern("id", false, "Pattern");
        SensorNode sensor = new SensorNode("sensornode", new Point(0, 0), "temp1", "Temperature sensor", new Operand(
                (mockedInstance.getSensorCtrl().getByName("temp1").getAttributes().toArray(new Attribute[0]))));

        SocketActionNode action = new SocketActionNode("actionnode", new Point(0, 0));
        action.setMessage("actionmessage");

        toDeploy.addInputNode(sensor);
        toDeploy.addOutputNode(action);
        sensor.setOutput(action);

        mockedInstance.getPatternCtrl().update(toDeploy);

        // deploy pattern
        mockedInstance.getPatternCtrl().deploy("id");

        // resetting the mocked logger - not nice, but works
        reset(mockedInstance.getLogger());
        // check if events are registered - send simulated event
        new Simulation("./src/test/resources/simulations/siddhienginetest_oneeventtemp1.sim").simulate(mockedInstance);

        // wait for good measure (we have to wait until the simulation is finished)
        Thread.sleep(500);

        // verify the pattern was detected
        verify(mockedInstance.getLogger()).log(argThat((String string) -> string.contains("Received event")));
        verify(mockedInstance.getLogger())
                .log("Pattern 'Pattern' recognized. Executing action: socket{message=actionmessage}");

        assertTrue(
                subject.getDeploymentInstances().stream().anyMatch(instance -> instance.getPatternId().equals("id")));
    }

    /**
     * Test method for {@link SiddhiEngine#undeploy(Pattern)}. Tests that a pattern
     * won't be recognized after it was undeployed.
     * 
     * @throws Exception
     *             cause sometimes something goes wrong
     */
    @Test
    public void testUndeploy() throws Exception {
        Pattern toDeploy = new Pattern("id", false, "Pattern");
        SensorNode sensor = new SensorNode("sensornode", new Point(0, 0), "temp1", "Temperature sensor", new Operand(
                (mockedInstance.getSensorCtrl().getByName("temp1").getAttributes().toArray(new Attribute[0]))));

        SocketActionNode action = new SocketActionNode("actionnode", new Point(0, 0));
        action.setMessage("actionmessage");

        toDeploy.addInputNode(sensor);
        toDeploy.addOutputNode(action);
        sensor.setOutput(action);

        mockedInstance.getPatternCtrl().update(toDeploy);

        // deploy pattern
        mockedInstance.getPatternCtrl().deploy("id");

        // undeploy pattern
        mockedInstance.getPatternCtrl().undeploy("id");

        // resetting the mocked logger - not nice, but works
        reset(mockedInstance.getLogger());
        // check if events are registered - send simulated event
        new Simulation("./src/test/resources/simulations/siddhienginetest_oneeventtemp1.sim").simulate(mockedInstance);
        // wait for good measure
        Thread.sleep(500);

        // verify the pattern was not detected
        verify(mockedInstance.getLogger()).log(argThat((String string) -> string.contains("Received event")));
        verify(mockedInstance.getLogger(), never())
                .log(argThat(((String string) -> string.contains("Pattern 'Pattern' recognized."))));

        assertTrue(
                subject.getDeploymentInstances().stream().noneMatch(instance -> instance.getPatternId().equals("id")));
    }
}
