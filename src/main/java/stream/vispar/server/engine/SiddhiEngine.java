package stream.vispar.server.engine;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;

import stream.vispar.compiler.CompileException;
import stream.vispar.compiler.SiddhiCode;
import stream.vispar.compiler.SiddhiCompiler;
import stream.vispar.compiler.TreeCompiler;
import stream.vispar.model.Pattern;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.entities.Event;
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
    
    private final Map<String, String> patternToRuntime;
    
    /**
     * Constructs a new {@link SiddhiEngine}.
     * 
     * @param instance
     *          the {@link ServerInstance} the engine belongs to.
     */
    public SiddhiEngine(ServerInstance instance) {
        this.instance = Objects.requireNonNull(instance);
        this.manager = new SiddhiManager();
        this.compiler = new TreeCompiler();
        
        this.patternToRuntime = new HashMap<>();
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
            assert patternToRuntime.containsKey(pattern.getId()) : "Pattern is deployed, but no Runtime is present.";
            // do nothing if pattern is already deployed
            return;
        }
        
        SiddhiCode code = null;
        try {
            code = compiler.compile(pattern);
        } catch (CompileException e) {
            // TODO add logging
            e.printStackTrace();
            return;
        }
        
        ExecutionPlanRuntime runtime = manager.createExecutionPlanRuntime(code.getAsString());
        
        // TODO add StreamListener for Action(s), feed events into runtime
        
        patternToRuntime.put(pattern.getId(), runtime.getName());
        runtime.start();
    }
    
    @Override
    public void undeploy(Pattern pattern) {
        Objects.requireNonNull(pattern);
        
        if (!pattern.isDeployed()) {
            assert !patternToRuntime.containsKey(pattern.getId()) : "Pattern is not deployed, but Runtime is present.";
            // do nothing if pattern is already undeployed
            return;
        }
        ExecutionPlanRuntime runtime = manager.getExecutionPlanRuntime(patternToRuntime.get(pattern.getId()));
        assert !Objects.isNull(runtime) : "couldn't find a runtime for a deployed Pattern";

        runtime.shutdown();
        patternToRuntime.remove(pattern.getId());
    }

    @Override
    public void sendEvent(Event event) {
        instance.getLogger().log(String.format(instance.getLocalizer().get(LocalizedString.RECEIVED_EVENT), 
                event.toString()));
    }
}
