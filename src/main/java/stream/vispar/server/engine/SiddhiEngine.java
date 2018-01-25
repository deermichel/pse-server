package stream.vispar.server.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;

import stream.vispar.compiler.CompileException;
import stream.vispar.compiler.SiddhiCode;
import stream.vispar.compiler.SiddhiCompiler;
import stream.vispar.compiler.TreeCompiler;
import stream.vispar.model.NodeVisitor;
import stream.vispar.model.Pattern;
import stream.vispar.model.nodes.inputs.ConstantInputNode;
import stream.vispar.model.nodes.inputs.InputNode;
import stream.vispar.model.nodes.inputs.SensorNode;
import stream.vispar.model.nodes.operators.CountAggregationNode;
import stream.vispar.model.nodes.operators.FilterOperatorNode;
import stream.vispar.model.nodes.operators.FunctionAggregationNode;
import stream.vispar.model.nodes.operators.LogicalOperatorNode;
import stream.vispar.model.nodes.outputs.MailActionNode;
import stream.vispar.model.nodes.outputs.SocketActionNode;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.entities.Event;
import stream.vispar.server.core.entities.adapters.NodeVisitorAdapter;
import stream.vispar.server.localization.LocalizedString;

/**
 * Engine implementation using the WSO2 Siddhi library.
 * 
 * @author Micha Hanselmann
 * @author Nico Weidmann
 */
public class SiddhiEngine implements IEngine {

    /**
     * Server instance the engine belongs to.
     */
    private final ServerInstance instance;

    private SiddhiManager manager;
    private SiddhiCompiler compiler;

    private final Map<String, DeploymentInstance> deploymentInstances;

    /**
     * Constructs a new {@link SiddhiEngine}.
     * 
     * @param instance
     *            the {@link ServerInstance} the engine belongs to.
     */
    public SiddhiEngine(ServerInstance instance) {
        this.instance = Objects.requireNonNull(instance);
        this.manager = new SiddhiManager();
        this.compiler = new TreeCompiler();

        this.deploymentInstances = new HashMap<>();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void deploy(Pattern pattern) {
        Objects.requireNonNull(pattern);

        if (pattern.isDeployed()) {
            assert deploymentInstances
                    .containsKey(pattern.getId()) : "Pattern is deployed, but no DeploymentInstance is present.";
            // do nothing if pattern is already deployed
            return;
        }

        // compile the pattern
        SiddhiCode code = null;
        try {
            code = compiler.compile(pattern);
        } catch (CompileException e) {
            // TODO add logging
            e.printStackTrace();
            return;
        }

        ExecutionPlanRuntime runtime = manager.createExecutionPlanRuntime(code.getAsString());
        deploymentInstances.put(pattern.getId(), new DeploymentInstance(pattern, runtime));
        runtime.start();

        // who updates the deployment status in the Pattern?
    }

    @Override
    public void undeploy(Pattern pattern) {
        Objects.requireNonNull(pattern);

        if (!pattern.isDeployed()) {
            assert !deploymentInstances
                    .containsKey(pattern.getId()) : "Pattern is not deployed, but Runtime is present.";
            // do nothing if pattern is already undeployed
            return;
        }
        ExecutionPlanRuntime runtime =
                manager.getExecutionPlanRuntime(deploymentInstances.get(pattern.getId()).runtimeId);
        assert !Objects.isNull(runtime) : "couldn't find a runtime for a deployed Pattern";

        // by shutting down the runtime, pattern recognition is stopped
        runtime.shutdown();
        deploymentInstances.remove(pattern.getId());

        // who updates the deployment status in the Pattern?
    }

    @Override
    public void sendEvent(Event event) {
        instance.getLogger()
                .log(String.format(instance.getLocalizer().get(LocalizedString.RECEIVED_EVENT), event.toString()));
    }

    /**
     * The DeploymentInstance encapsulates the id of a deployed {@link Pattern}, the
     * id of the {@link ExecutionPlanRuntime} and the Handlers for the input- and
     * output streams.
     * 
     * @author Nico Weidmann
     */
    private class DeploymentInstance {

        private final String patternId;
        private final String runtimeId;

        private final Collection<IAction> actions;

        private final Map<String, InputHandler> sensorToHandler;

        public DeploymentInstance(Pattern pattern, ExecutionPlanRuntime runtime) {
            this.patternId = Objects.requireNonNull(pattern).getId();
            this.runtimeId = Objects.requireNonNull(runtime).getName();
            
            this.actions = new LinkedList<>();

            this.sensorToHandler = new HashMap<>();

            // initialize input handlers using a node visitor
            for (InputNode input : pattern.getInputNodes()) {
                input.acceptVisitor(new NodeVisitorAdapter() {

                    @Override
                    public void visitSensorNode(SensorNode node) {
                        // obtain handler for this sensor and add it to the map
                        sensorToHandler.put(node.getSensorName(),
                                runtime.getInputHandler(compiler.getStreamName(node)));
                    }
                });
            }

            // TODO initialize actions
        }
    }
}
