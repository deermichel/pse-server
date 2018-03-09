package stream.vispar.server.engine.extensions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.ComplexEventChunk;
import org.wso2.siddhi.core.event.MetaComplexEvent;
import org.wso2.siddhi.core.event.stream.MetaStreamEvent;
import org.wso2.siddhi.core.event.stream.StreamEvent;
import org.wso2.siddhi.core.event.stream.StreamEventCloner;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.VariableExpressionExecutor;
import org.wso2.siddhi.core.query.processor.Processor;
import org.wso2.siddhi.core.query.processor.stream.window.FindableProcessor;
import org.wso2.siddhi.core.query.processor.stream.window.LengthWindowProcessor;
import org.wso2.siddhi.core.query.processor.stream.window.TimeWindowProcessor;
import org.wso2.siddhi.core.query.processor.stream.window.WindowProcessor;
import org.wso2.siddhi.core.table.EventTable;
import org.wso2.siddhi.core.util.collection.operator.Finder;
import org.wso2.siddhi.query.api.definition.AbstractDefinition;
import org.wso2.siddhi.query.api.expression.Expression;

/**
 * A window that limits the events by a given amount of time and length.
 * 
 * @author Jonas
 */
public class TimeLengthWindow extends WindowProcessor implements FindableProcessor {

    private LengthWindowProcessor lengthWindow = new LengthWindowProcessor();
    private TimeWindowProcessor timeWindow = new TimeWindowProcessor();

    public TimeLengthWindow() {
        lengthWindow.setNextProcessor(timeWindow);
        timeWindow.setNextProcessor(this);
    }

    @Override
    public AbstractDefinition initProcessor(AbstractDefinition inputDefinition,
            ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
        lengthWindow.initProcessor(inputDefinition, new ExpressionExecutor[] { attributeExpressionExecutors[1] },
                executionPlanContext);
        timeWindow.initProcessor(inputDefinition, new ExpressionExecutor[] { attributeExpressionExecutors[0] },
                executionPlanContext);
        return super.initProcessor(inputDefinition, attributeExpressionExecutors, executionPlanContext);
    }

    @Override
    public void process(@SuppressWarnings("rawtypes") ComplexEventChunk complexEventChunk) {
        super.process(complexEventChunk);
        lengthWindow.process(complexEventChunk);
        timeWindow.process(complexEventChunk);
    }

    @Override
    public Processor cloneProcessor(String key) {
        TimeLengthWindow clone = (TimeLengthWindow) super.cloneProcessor(key);
        clone.timeWindow = (TimeWindowProcessor) timeWindow.cloneProcessor(key);
        clone.lengthWindow = (LengthWindowProcessor) lengthWindow.cloneProcessor(key);
        return clone;
    }

    @Override
    public void constructStreamEventPopulater(MetaStreamEvent metaStreamEvent, int streamEventChainIndex) {
        super.constructStreamEventPopulater(metaStreamEvent, streamEventChainIndex);
        timeWindow.constructStreamEventPopulater(metaStreamEvent, streamEventChainIndex);
        lengthWindow.constructStreamEventPopulater(metaStreamEvent, streamEventChainIndex);
    }

    @Override
    public void setStreamEventCloner(StreamEventCloner streamEventCloner) {
        super.setStreamEventCloner(streamEventCloner);
        timeWindow.setStreamEventCloner(streamEventCloner);
        lengthWindow.setStreamEventCloner(streamEventCloner);
    }

    @Override
    protected void init(ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
        // nothing to do here
    }

    @Override
    protected void process(ComplexEventChunk<StreamEvent> streamEventChunk, Processor nextProcessor,
            StreamEventCloner streamEventCloner) {
        // nothing to do here
    }

    @Override
    public void start() {
        lengthWindow.start();
        timeWindow.start();
    }

    @Override
    public void stop() {
        lengthWindow.stop();
        timeWindow.stop();
    }

    @Override
    public Object[] currentState() {
        Object[] lengthState = lengthWindow.currentState();
        Object[] timeState = timeWindow.currentState();
        Object[] state = new Object[lengthState.length + timeState.length];
        System.arraycopy(state, 0, lengthState, 0, lengthState.length);
        System.arraycopy(state, lengthState.length, timeState, 0, timeState.length);
        return state;
    }

    @Override
    public void restoreState(Object[] state) {
        lengthWindow.restoreState(Arrays.copyOfRange(state, 0, 2));
        timeWindow.restoreState(Arrays.copyOfRange(state, 2, state.length));
    }

    @Override
    public StreamEvent find(ComplexEvent matchingEvent, Finder finder) {
        return timeWindow.find(matchingEvent, finder);
    }

    @Override
    public Finder constructFinder(Expression expression, MetaComplexEvent metaComplexEvent,
            ExecutionPlanContext executionPlanContext, List<VariableExpressionExecutor> variableExpressionExecutors,
            Map<String, EventTable> eventTableMap, int matchingStreamIndex, long withinTime) {
        return timeWindow.constructFinder(expression, metaComplexEvent, executionPlanContext,
                variableExpressionExecutors, eventTableMap, matchingStreamIndex, withinTime);
    }

}
