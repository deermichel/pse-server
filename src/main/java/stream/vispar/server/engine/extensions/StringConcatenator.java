package stream.vispar.server.engine.extensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.query.selector.attribute.aggregator.AttributeAggregator;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.Attribute.Type;

public class StringConcatenator extends AttributeAggregator {
	
	private List<Object> objects = new ArrayList<>();
	
	
	@Override
	protected void init(ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (Object obj : objects) {
			builder.append(obj.toString());
		}
		
		return builder.toString();
	}
	

	@Override
	public Type getReturnType() {
		return Attribute.Type.STRING;
	}

	@Override
	public Object processAdd(Object data) {
		if (data != null) {
			objects.add(data);
		}
		return toString();
	}

	@Override
	public Object processAdd(Object[] data) {
		for (Object obj : data) {
			if (obj != null) {
				objects.add(obj);
			}
		}
		return toString();
	}

	@Override
	public Object processRemove(Object data) {
		objects.remove(data);
		return toString();
	}

	@Override
	public Object processRemove(Object[] data) {
		for (Object obj : data) {
			objects.remove(obj);
		}
		return toString();
	}

	@Override
	public Object reset() {
		objects.clear();
		return toString();
	}
	
	@Override
	public void start() {
	}
	
	@Override
	public void stop() {
	}
	
	@Override
	public Object[] currentState() {
		return objects.toArray();
	}
	
	@Override
	public void restoreState(Object[] state) {
		objects = new ArrayList<>(Arrays.asList(state));
	}
	
}