package stream.vispar.server.engine;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;

import com.google.common.base.Optional;

import stream.vispar.compiler.CompileException;
import stream.vispar.compiler.SiddhiCode;
import stream.vispar.compiler.SiddhiCompiler;
import stream.vispar.compiler.TreeCompiler;
import stream.vispar.model.Pattern;
import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.inputs.InputNode;
import stream.vispar.model.nodes.inputs.PatternInputNode;
import stream.vispar.model.nodes.inputs.SensorNode;
import stream.vispar.model.nodes.outputs.MailActionNode;
import stream.vispar.model.nodes.outputs.OutputNode;
import stream.vispar.model.nodes.outputs.PatternOutputNode;
import stream.vispar.model.nodes.outputs.SocketActionNode;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.entities.Event;
import stream.vispar.server.core.entities.adapters.NodeVisitorAdapter;
import stream.vispar.server.engine.extensions.LogicalAndFunction;
import stream.vispar.server.engine.extensions.StringAverage;
import stream.vispar.server.engine.extensions.StringComparator;
import stream.vispar.server.engine.extensions.StringConcatenator;
import stream.vispar.server.engine.extensions.StringMaximum;
import stream.vispar.server.engine.extensions.StringMinimum;
import stream.vispar.server.engine.extensions.TimeLengthWindow;
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

        this.deploymentInstances = new ConcurrentHashMap<>();
        
        manager.setExtension("vispar:and", LogicalAndFunction.class);
        manager.setExtension("vispar:timeLength", TimeLengthWindow.class);
        manager.setExtension("vispar:stringcompare", StringComparator.class);
        manager.setExtension("vispar:stringsum", StringConcatenator.class);
        manager.setExtension("vispar:stringmax", StringMaximum.class);
        manager.setExtension("vispar:stringmin", StringMinimum.class);
        manager.setExtension("vispar:stringavg", StringAverage.class);
    }

    @Override
    public void start() {
        instance.getLogger().log(instance.getLocalizer().get(LocalizedString.SIDDHI_ENGINE_STARTED));
    }

    @Override
    public void stop() {

        // undeploy all patterns
        instance.getPatternCtrl().getAll().forEach(this::undeploy);

        instance.getLogger().log(instance.getLocalizer().get(LocalizedString.SIDDHI_ENGINE_STOPPED));
    }

    @Override
    public void deploy(Pattern pattern) {
        Objects.requireNonNull(pattern);

        assert Objects.nonNull(instance.getPatternCtrl().getById(
                pattern.getId())) : "tried to deploy a pattern that could not be found in the pattern controller";

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
        deploymentInstances.put(pattern.getId(), new DeploymentInstance(pattern, runtime, this));
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

        for (DeploymentInstance currentInstance : deploymentInstances.values()) {
            Collection<InputHandler> handlers = currentInstance.sensorToHandler.get(event.getSensor().getName());

            if (Objects.nonNull(handlers)) {
                // handlers for the given event were found

                // this array represents the order of attributes expected by the handler
                Attribute[] attributeOrder = currentInstance.sensorToAttributeOrder.get(event.getSensor().getName());

                // maps the attributes to the values of the current event
                Map<String, String> dataMap = new HashMap<>();

                for (Entry<Attribute, String> entry : event.getData().entrySet()) {
                    dataMap.put(entry.getKey().getName(), entry.getValue());
                }

                assert attributeOrder.length == dataMap.size() && dataMap.size() == event.getSensor().getAttributes()
                        .size() : "unexpected inconsistency with sensor attributes";

                Object[] data = new Object[attributeOrder.length];

                // extracting the data for each attribute in the correct order
                for (int i = 0; i < attributeOrder.length; ++i) {
                    String next = dataMap.get(attributeOrder[i].getName());

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

                handlers.forEach(handler -> {
                    try {
                        handler.send(event.getTimestamp(), data);
                    } catch (InterruptedException e) {
                        this.instance.getLogger().logError(e.toString());
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
    }
    
    /**
     * Sends events issued by the given {@link PatternOutputNode} in the given
     * {@link Pattern} to all DeploymentInstances that contain a corresponding
     * {@link PatternInputNode}.
     * 
     * @param sourcePattern
     *            the {@link Pattern} that caused the events
     * @param sourceNode
     *            the {@link PatternOutputNode} that issued the events
     * @param events
     *            the events to be sent to the input nodes
     */
    protected void sendEvents(Pattern sourcePattern, PatternOutputNode sourceNode,
            org.wso2.siddhi.core.event.Event[] events) {
        instance.getLogger()
                .log(String.format(instance.getLocalizer().get(LocalizedString.RECEIVED_EVENT), "pattern event"));

        for (DeploymentInstance currentInstance : deploymentInstances.values()) {
            // to calculate the correct key, by convention we have to use the node id if the name is null
            Collection<InputHandler> handlers =
                    currentInstance.patternInputToHandler.get(
                            sourcePattern.getId() + Optional.fromNullable(sourceNode.getName()).or(sourceNode.getId()));

            if (Objects.nonNull(handlers)) {
                // handlers for the events were found
                
                // send events to all handlers
                handlers.forEach(handler -> {
                    try {
                        handler.send(events);
                    } catch (InterruptedException e) {
                        this.instance.getLogger().logError(e.toString());
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
    }

    /**
     * Returns this {@link SiddhiEngine}'s {@link DeploymentInstance}s. Used for
     * testing purposes.
     * 
     * @return a {@code Collection<DeploymentInstance>} of all DeploymentInstances
     *         of this SiddhiEngine.
     */
    protected Collection<DeploymentInstance> getDeploymentInstances() {
        return deploymentInstances.values();
    }

    /**
     * The DeploymentInstance encapsulates the id of a deployed {@link Pattern}, the
     * id of the {@link ExecutionPlanRuntime} and the Handlers for the input- and
     * output streams.
     * 
     * @author Nico Weidmann
     */
    protected class DeploymentInstance {

        private final String patternId;
        private final String runtimeId;

        private final Collection<IAction> actions;

        private final Map<String, Collection<InputHandler>> sensorToHandler;
        private final Map<String, Attribute[]> sensorToAttributeOrder;
        
        private final Map<String, Collection<InputHandler>> patternInputToHandler;

        /**
         * Constructs a new instance of {@link DeploymentInstance} for the given
         * pattern, in the specified {@link ExecutionPlanRuntime}.
         * 
         * @param pattern
         *            the pattern that should be deployed
         * @param runtime
         *            the runtime the pattern should be deployed in
         * @param engine
         *            the {@link SiddhiEngine} this DeploymentInstance belongs to
         */
        DeploymentInstance(Pattern pattern, ExecutionPlanRuntime runtime, SiddhiEngine engine) {
            this.patternId = Objects.requireNonNull(pattern).getId();
            this.runtimeId = Objects.requireNonNull(runtime).getName();

            this.actions = new LinkedList<>();

            this.sensorToHandler = new HashMap<>();
            this.sensorToAttributeOrder = new HashMap<>();
            
            this.patternInputToHandler = new HashMap<>();

            // initialize input handlers using a node visitor
            for (InputNode input : pattern.getInputNodes()) {
                input.acceptVisitor(new NodeVisitorAdapter() {

                    @Override
                    public void visitSensorNode(SensorNode node) {
                        // obtain handler for this sensor and add it to the map
                        
                        // add new input handler list for the current sensor, if no list for the sensor exists
                        sensorToHandler.putIfAbsent(node.getSensorName(), new LinkedList<>());

                        // add the input handler for the current node
                        sensorToHandler.get(node.getSensorName())
                                .add(runtime.getInputHandler(compiler.getStreamName(node)));

                        // store attribute order
                        sensorToAttributeOrder.put(node.getSensorName(), compiler.getAttributesOrdered(node));
                    }
                    
                    @Override
                    public void visitPatternInputNode(PatternInputNode node) {
                        // obtain handler for this pattern input, add it to mapping

                        // pattern inputs are identified by the id of the source pattern and the name of
                        // the output
                        patternInputToHandler.putIfAbsent(node.getSourcePatternId() + node.getPatternOutputName(),
                                new LinkedList<>());
                        patternInputToHandler.get(node.getSourcePatternId() + node.getPatternOutputName())
                                .add(runtime.getInputHandler(compiler.getStreamName(node)));
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
                                logRecognized("email" + action);

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
                                logRecognized("socket" + action);

                                action.execute();
                            }
                        });
                    }
                    
                    @Override
                    public void visitPatternOutputNode(PatternOutputNode node) {
                        
                        runtime.addCallback(compiler.getStreamName(node), new StreamCallback() {
                            
                            @Override
                            public void receive(org.wso2.siddhi.core.event.Event[] events) {
                                logRecognized("Pattern Action");
                                
                                // we received events over the pattern output - we have to feed these events
                                // into the input of all other runtimes
                                engine.sendEvents(pattern, node, events);
                            }
                        });
                    }
                    
                    private void logRecognized(String action) {
                        instance.getLogger()
                        .log(String.format(
                                instance.getLocalizer().get(LocalizedString.PATTERN_RECOGNIZED),
                                pattern.getName(), action));
                    }
                });
            }
        }

        // ===== GETTERS FOR TESTING PURPOSES =====

        /**
         * Getter for the pattern id.
         * 
         * @return the pattern id
         */
        protected String getPatternId() {
            return patternId;
        }

        /**
         * Getter for the runtime id.
         * 
         * @return the runtime id
         */
        protected String getRuntimeId() {
            return runtimeId;
        }

        /**
         * Getter for the actions.
         * 
         * @return the actions
         */
        protected Collection<IAction> getActions() {
            return Collections.unmodifiableCollection(actions);
        }

        /**
         * Getter for the sensor to handler mapping.
         * 
         * @return the mapping
         */
        protected Map<String, Collection<InputHandler>> getSensorToHandler() {
            return Collections.unmodifiableMap(sensorToHandler);
        }

        /**
         * Getter for the sensor to attribute order mapping.
         * 
         * @return the mapping
         */
        protected Map<String, Attribute[]> getSensorToAttributeOrder() {
            return Collections.unmodifiableMap(sensorToAttributeOrder);
        }
    }
}
