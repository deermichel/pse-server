package stream.vispar.server.engine.extensions;

import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.Attribute.Type;
import org.wso2.siddhi.query.api.exception.ExecutionPlanValidationException;

// NOTE: it seems like both inputs have different instances...
public class LogicalAndFunction extends FunctionExecutor {
	
	private long lastFirstInput = -1;
	private long lastSecondInput = -1;

	@Override
	protected void init(ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
		if (attributeExpressionExecutors.length != 2) {
			throw new ExecutionPlanValidationException("exactly two input attributes required");
		}
		
		if ((attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG
				&& attributeExpressionExecutors[0].getReturnType() != Attribute.Type.INT)
			|| (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.LONG
				&& attributeExpressionExecutors[1].getReturnType() != Attribute.Type.INT)) {
			throw new ExecutionPlanValidationException("attributes must be long or integer");
		}
	}

	@Override
	public Type getReturnType() {
		return Attribute.Type.BOOL;
	}
	
	@Override
	protected Object execute(Object[] data) {
		long in1 = (long) data[0];
		long in2 = (long) data[1];
		
		if (in1 > lastFirstInput && in2 > lastSecondInput) {
			lastFirstInput = in1;
			lastSecondInput = in2;
			System.out.println();
			return true;
		}
		
		return false;
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
		return new Object[] {lastFirstInput, lastSecondInput};
	}

	@Override
	public void restoreState(Object[] state) {
		lastFirstInput = (long) state[0];
		lastSecondInput = (long) state[1];
	}
	
}
