package stream.vispar.server.engine.extensions;

import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.query.selector.attribute.aggregator.AttributeAggregator;
import org.wso2.siddhi.query.api.definition.Attribute.Type;
import org.wso2.siddhi.query.api.exception.ExecutionPlanValidationException;

/**
 * An aggregation that requires one {@link String} parameter and returns a String.
 * 
 * @author Jonas
 */
abstract class StringAggregation extends AttributeAggregator {

	@Override
	protected void init(ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
		if (attributeExpressionExecutors.length != 1) {
			throw new ExecutionPlanValidationException("exactly one attribute required");
		}
		if (attributeExpressionExecutors[0].getReturnType() != Type.STRING) {
			throw new ExecutionPlanValidationException("attribute must be of type string");
		}
	}

	@Override
	public Type getReturnType() {
		return Type.STRING;
	}

	@Override
	public Object processAdd(Object[] data) {
		return null;
	}

	@Override
	public Object processRemove(Object[] data) {
		return null;
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

}
