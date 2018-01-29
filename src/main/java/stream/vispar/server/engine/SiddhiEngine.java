package stream.vispar.server.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;

import stream.vispar.compiler.CompileException;
import stream.vispar.compiler.SiddhiCode;
import stream.vispar.compiler.SiddhiCompiler;
import stream.vispar.compiler.TreeCompiler;
import stream.vispar.model.Pattern;
import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.inputs.InputNode;
import stream.vispar.model.nodes.inputs.SensorNode;
import stream.vispar.model.nodes.outputs.MailActionNode;
import stream.vispar.model.nodes.outputs.OutputNode;
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
        instance.getLogger().log(instance.getLocalizer().get(LocalizedString.SIDDHI_ENGINE_STARTED));
    }

    @Override
    public void stop() {
        
        // undeploy all patterns
        instance.getPatternCtrl().getAll().forEach(pattern -> undeploy(pattern));
        
        instance.getLogger().log(instance.getLocalizer().get(LocalizedString.SIDDHI_ENGINE_STOPPED));
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
            instance.getLogger().logError("Siddhi compiler error: " + e.toString());
            throw new IllegalArgumentException(e.toString());
        }

        // init and start runtime
        ExecutionPlanRuntime runtime = manager.createExecutionPlanRuntime(code.getAsString());
        deploymentInstances.put(pattern.getId(), new DeploymentInstance(pattern, runtime));
        runtime.start();
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
        DeploymentInstance removed = deploymentInstances.remove(pattern.getId());

        assert Objects.nonNull(removed) : "tried to remove deployment instance, but none was present";
        assert removed.patternId.equals(
                pattern.getId()) : "inconsistent mapping found: pattern ID was mapped to wrong DeploymentInstance";
    }

    @Override
    public void sendEvent(Event event) {
        instance.getLogger()
                .log(String.format(instance.getLocalizer().get(LocalizedString.RECEIVED_EVENT), event.toString()));

        for (DeploymentInstance instance : deploymentInstances.values()) {
            InputHandler handler = instance.sensorToHandler.get(event.getSensor().getName());

            if (Objects.nonNull(handler)) {
                // a handler for the given event was found

                // this array represents the order of attributes expected by the handler
                Attribute[] attributeOrder = compiler.getAttributesOrdered(event.getSensor().getSensorNode());

                // maps the attributes to the values of the current event
                Map<Attribute, String> dataMap = event.getData();

                assert attributeOrder.length == dataMap.size() && dataMap.size() == event.getSensor().getAttributes()
                        .size() : "unexpected inconsistency with sensor attributes";

                Object[] data = new Object[attributeOrder.length];

                // extracting the data for each attribute in the correct order
                for (int i = 0; i < attributeOrder.length; ++i) {
                    String next = dataMap.get(attributeOrder[i]);

                    Object nextObject = null;
                    switch (attributeOrder[i].getType()) {
                    case DOUBLE:
                        nextObject = Double.parseDouble(next);
                        break;
                    case INTEGER:
                        nextObject = Integer.parseInt(next);
                        break;
                    case STRING:
                        nextObject = next;
                        break;
                    default:
                        throw new IllegalStateException("Attribute has unknown type");
                    }

                    data[i] = nextObject;
                }

                try {
                    handler.send(event.getTimestamp(), data);
                } catch (InterruptedException e) {
                    this.instance.getLogger().logError(e.toString());
                }
            }
        }
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

        DeploymentInstance(Pattern pattern, ExecutionPlanRuntime runtime) {
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
                        if (!sensorToHandler.containsKey(node.getSensorName())) {
                            // sensor hasn't been added yet. We need to check because one sensor might have
                            // multiple nodes in a pattern
                            sensorToHandler.put(node.getSensorName(),
                                    runtime.getInputHandler(compiler.getStreamName(node)));
                        }
                    }
                });
            }

            for (OutputNode output : pattern.getOutputNodes()) {

                // visit the output node to determine the type of output
                output.acceptVisitor(new NodeVisitorAdapter() {

                    @Override
                    public void visitMailActionNode(MailActionNode node) {
                        // the output node is a mail action node

                        final EmailAction action =
                                new EmailAction(node.getRecipientEmail(), node.getSubject(), node.getMessage());
                        actions.add(action);

                        runtime.addCallback(compiler.getStreamName(node), new StreamCallback() {
                            @Override
                            public void receive(org.wso2.siddhi.core.event.Event[] events) {
                                action.execute();
                            }
                        });
                    }

                    @Override
                    public void visitSocketActionNode(SocketActionNode node) {
                        // the output node is a socket action node

                        final SocketAction action = new SocketAction(instance.getSockHandler(), node.getMessage());
                        actions.add(action);

                        runtime.addCallback(compiler.getStreamName(node), new StreamCallback() {
                            @Override
                            public void receive(org.wso2.siddhi.core.event.Event[] events) {
                                action.execute();
                            }
                        });
                    }
                });
            }
        }
    }
}
