package stream.vispar.server.core.entities.adapters;

import stream.vispar.model.NodeVisitor;
import stream.vispar.model.nodes.inputs.ConstantInputNode;
import stream.vispar.model.nodes.inputs.PatternInputNode;
import stream.vispar.model.nodes.inputs.SensorNode;
import stream.vispar.model.nodes.operators.CountAggregationNode;
import stream.vispar.model.nodes.operators.FilterOperatorNode;
import stream.vispar.model.nodes.operators.FunctionAggregationNode;
import stream.vispar.model.nodes.operators.LogicalOperatorNode;
import stream.vispar.model.nodes.outputs.MailActionNode;
import stream.vispar.model.nodes.outputs.PatternOutputNode;
import stream.vispar.model.nodes.outputs.SocketActionNode;

/**
 * A {@link NodeVisitor} that does nothing on every visit. Convenience class
 * that allows for easily defining NodeVisitors that only care for one or a few
 * node types.
 * 
 * @author Nico Weidmann
 */
public abstract class NodeVisitorAdapter implements NodeVisitor {

    @Override
    public void visitSensorNode(SensorNode node) {
        // does nothing
    }

    @Override
    public void visitConstantInputNode(ConstantInputNode node) {
        // does nothing
    }

    @Override
    public void visitMailActionNode(MailActionNode node) {
        // does nothing
    }

    @Override
    public void visitSocketActionNode(SocketActionNode node) {
        // does nothing
    }

    @Override
    public void visitFilterOperatorNode(FilterOperatorNode node) {
        // does nothing
    }

    @Override
    public void visitFunctionAggregationNode(FunctionAggregationNode node) {
        // does nothing
    }

    @Override
    public void visitCountAggregationNode(CountAggregationNode node) {
        // does nothing
    }

    @Override
    public void visitLogicalOperatorNode(LogicalOperatorNode node) {
        // does nothing
    }
    
    @Override
    public void visitPatternInputNode(PatternInputNode node) {
        // does nothing
    }
    
    @Override
    public void visitPatternOutputNode(PatternOutputNode node) {
        // does nothing
    }
}
