package stream.vispar.server.engine.extensions;

import java.util.List;
import java.util.Map;

import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.ComplexEventChunk;
import org.wso2.siddhi.core.event.MetaComplexEvent;
import org.wso2.siddhi.core.event.stream.StreamEvent;
import org.wso2.siddhi.core.event.stream.StreamEventCloner;
import org.wso2.siddhi.core.executor.ConstantExpressionExecutor;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.VariableExpressionExecutor;
import org.wso2.siddhi.core.query.processor.Processor;
import org.wso2.siddhi.core.query.processor.SchedulingProcessor;
import org.wso2.siddhi.core.query.processor.stream.window.FindableProcessor;
import org.wso2.siddhi.core.query.processor.stream.window.WindowProcessor;
import org.wso2.siddhi.core.table.EventTable;
import org.wso2.siddhi.core.util.Scheduler;
import org.wso2.siddhi.core.util.collection.operator.Finder;
import org.wso2.siddhi.core.util.parser.CollectionOperatorParser;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.exception.ExecutionPlanValidationException;
import org.wso2.siddhi.query.api.expression.Expression;

/**
 * A window that limits the events by a given amount of time and length.
 * 
 * @author Jonas
 */
public class TimeLengthWindow extends WindowProcessor implements SchedulingProcessor, FindableProcessor {
	
	private int length;
    private int count = 0;
    private long timeInMilliSeconds;
    private ComplexEventChunk<StreamEvent> expiredEventChunk;
    private Scheduler scheduler;
    private ExecutionPlanContext executionPlanContext;

    public void setTimeInMilliSeconds(long timeInMilliSeconds) {
        this.timeInMilliSeconds = timeInMilliSeconds;
    }

    @Override
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
	protected void init(ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
    	this.executionPlanContext = executionPlanContext;
        this.expiredEventChunk = new ComplexEventChunk<StreamEvent>();
        
		if (attributeExpressionExecutors.length != 2) {
			throw new ExecutionPlanValidationException("time-length-window must have exactly two arguments");
		}
				
		if (attributeExpressionExecutors[0].getReturnType() == Attribute.Type.INT) {
            timeInMilliSeconds = (Integer) ((ConstantExpressionExecutor) attributeExpressionExecutors[0]).getValue();
        } else if (attributeExpressionExecutors[0].getReturnType() == Attribute.Type.LONG) {
            timeInMilliSeconds = (Long) ((ConstantExpressionExecutor) attributeExpressionExecutors[0]).getValue();
        } else {
            throw new ExecutionPlanValidationException("Time window's parameter attribute should be either int or long, but found " + attributeExpressionExecutors[0].getReturnType());
        }
		
		length = (Integer) ((ConstantExpressionExecutor) attributeExpressionExecutors[1]).getValue();
	}

    @Override
    protected synchronized void process(ComplexEventChunk<StreamEvent> streamEventChunk, Processor nextProcessor, StreamEventCloner streamEventCloner) {
    	while (streamEventChunk.hasNext()) {
    		long currentTime = executionPlanContext.getTimestampGenerator().currentTime();
            StreamEvent streamEvent = streamEventChunk.next();
            StreamEvent clonedEvent = streamEventCloner.copyStreamEvent(streamEvent);
            clonedEvent.setType(StreamEvent.Type.EXPIRED);
            
            if (streamEvent.getType() == StreamEvent.Type.CURRENT) {
                clonedEvent.setTimestamp(currentTime + timeInMilliSeconds);
            }

            boolean eventScheduled = false;
            while (expiredEventChunk.hasNext()) {
                StreamEvent expiredEvent = expiredEventChunk.next();
                long timeDiff = expiredEvent.getTimestamp() - currentTime;
                if (timeDiff <= 0) {
                    expiredEventChunk.remove();
                    count--;
                    System.out.println(count);
                    streamEventChunk.insertBeforeCurrent(expiredEvent);
                } else {
                    scheduler.notifyAt(expiredEvent.getTimestamp());
                    expiredEventChunk.reset();
                    eventScheduled = true;
                    break;
                }
            }

            if (streamEvent.getType() == StreamEvent.Type.CURRENT) {
            	if (count < length) {
	            	count++;
	            	System.out.println(count);
	                this.expiredEventChunk.add(clonedEvent);
            	} else {
                    StreamEvent firstEvent = this.expiredEventChunk.poll();
                    if (firstEvent != null) {
                        streamEventChunk.insertBeforeCurrent(firstEvent);
                        this.expiredEventChunk.add(clonedEvent);
                    } else {
                        streamEventChunk.insertBeforeCurrent(clonedEvent);
                    }
                }
            
                if (!eventScheduled) {
                    scheduler.notifyAt(clonedEvent.getTimestamp());
                }
            }
            expiredEventChunk.reset();
        }
    	
        nextProcessor.process(streamEventChunk);
    }

    @Override
    public synchronized StreamEvent find(ComplexEvent matchingEvent, Finder finder) {
        return finder.find(matchingEvent, expiredEventChunk,streamEventCloner);
    }

    @Override
    public Finder constructFinder(Expression expression, MetaComplexEvent metaComplexEvent, ExecutionPlanContext executionPlanContext, List<VariableExpressionExecutor> variableExpressionExecutors, Map<String, EventTable> eventTableMap, int matchingStreamIndex, long withinTime) {
        return CollectionOperatorParser.parse( expression, metaComplexEvent, executionPlanContext, variableExpressionExecutors, eventTableMap, matchingStreamIndex, inputDefinition, withinTime);
    }

    @Override
    public void start() {
        //Do nothing
    }

    @Override
    public void stop() {
        //Do nothing
    }

    @Override
    public Object[] currentState() {
        return new Object[]{expiredEventChunk, count};
    }

    @Override
    public void restoreState(Object[] state) {
        expiredEventChunk = (ComplexEventChunk<StreamEvent>) state[0];
        count = (Integer) state[1];
    }
}
