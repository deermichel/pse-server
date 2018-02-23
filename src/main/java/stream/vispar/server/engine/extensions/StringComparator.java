package stream.vispar.server.engine.extensions;

import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.exception.ExecutionPlanRuntimeException;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.Attribute.Type;
import org.wso2.siddhi.query.api.exception.ExecutionPlanValidationException;

/**
 * A siddhi extension that compares two strings.
 * Returns an integer as defined in {@link String#compareTo(String)}.
 * A {@code null} value is treated as first possible value instead of throwing an exception.
 * 
 * @author Jonas
 */
public class StringComparator extends FunctionExecutor {
	
	@Override
	protected void init(ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
		if (attributeExpressionExecutors.length != 2) {
			throw new ExecutionPlanValidationException("two input attributes required");
		}
		if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING
				|| attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
			throw new ExecutionPlanValidationException("attributes must be strings");
		}
	}

	@Override
	public Type getReturnType() {
		return Attribute.Type.INT;
	}

	@Override
	protected Object execute(Object[] data) {
		if (data[0] == null) {
			return 1;
		} else if (data[1] == null) {
			return -1;
		}
		
		if (!(data[0] instanceof String) || !(data[1] instanceof String)) {
			throw new ExecutionPlanRuntimeException("attribute is not a string");
		}
		return ((String) data[0]).compareTo((String) data[1]);
	}
	
	@Override
	protected Object execute(Object data) {
		return null;
	}
	
	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public Object[] currentState() {
		return new Object[0];
	}

	@Override
	public void restoreState(Object[] state) {
	}

}
