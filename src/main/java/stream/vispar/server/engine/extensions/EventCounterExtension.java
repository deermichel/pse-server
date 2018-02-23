package stream.vispar.server.engine.extensions;

import java.util.Arrays;
import java.util.List;

import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.query.processor.stream.function.StreamFunctionProcessor;
import org.wso2.siddhi.query.api.definition.AbstractDefinition;
import org.wso2.siddhi.query.api.definition.Attribute;

public class EventCounterExtension extends StreamFunctionProcessor {
	
	private long counter = 0;
	
	@Override
	protected List<Attribute> init(AbstractDefinition inputDefinition,
			ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
		return Arrays.asList(new Attribute[] {new Attribute("_counter", Attribute.Type.LONG)});
	}

	@Override
	protected Object[] process(Object[] data) {
		return process((Object) data);
	}

	@Override
	protected Object[] process(Object data) {
		counter++;
		return new Object[] {counter};
	}
	
	@Override
	public Object[] currentState() {
		return new Object[] {counter};
	}
	
	@Override
	public void restoreState(Object[] state) {
		counter = (long) state[0];
	}
	
	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}
	
}
