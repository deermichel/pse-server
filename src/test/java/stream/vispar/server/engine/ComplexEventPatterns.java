package stream.vispar.server.engine;

import java.util.Arrays;
import java.util.UUID;

import stream.vispar.model.Pattern;
import stream.vispar.model.nodes.Attribute;
import stream.vispar.model.nodes.AttributeType;
import stream.vispar.model.nodes.Operand;
import stream.vispar.model.nodes.Point;
import stream.vispar.model.nodes.inputs.ConstantDoubleNode;
import stream.vispar.model.nodes.inputs.ConstantInputNode;
import stream.vispar.model.nodes.inputs.SensorNode;
import stream.vispar.model.nodes.operators.AggregationFunction;
import stream.vispar.model.nodes.operators.CountAggregationNode;
import stream.vispar.model.nodes.operators.FilterOperatorNode;
import stream.vispar.model.nodes.operators.FunctionAggregationNode;
import stream.vispar.model.nodes.operators.LengthWindow;
import stream.vispar.model.nodes.operators.LogicalOperatorNode;
import stream.vispar.model.nodes.operators.Operation;
import stream.vispar.model.nodes.operators.Relation;
import stream.vispar.model.nodes.operators.TimeWindow;
import stream.vispar.model.nodes.outputs.MailActionNode;
import stream.vispar.model.nodes.outputs.SocketActionNode;

public class ComplexEventPatterns {
    
    static final Point p = new Point(0, 0);
    
    static final Attribute intAttr1 = new Attribute("int1", "", AttributeType.INTEGER);
    static final Attribute intAttr2 = new Attribute("int2", "", AttributeType.INTEGER);
    static final Attribute intAttr3 = new Attribute("int3", "", AttributeType.INTEGER);
    static final Attribute doubleAttr1 = new Attribute("double1", "", AttributeType.DOUBLE);
    static final Attribute doubleAttr2 = new Attribute("double2", "", AttributeType.DOUBLE);
    static final Attribute doubleAttr3 = new Attribute("double3", "", AttributeType.DOUBLE);
    static final Attribute stringAttr1 = new Attribute("String1", "", AttributeType.STRING);
    static final Attribute stringAttr2 = new Attribute("String2", "", AttributeType.STRING);
    static final Attribute stringAttr3 = new Attribute("String3", "", AttributeType.STRING);
    
    
    public static Pattern getEmptyPattern() {
        return new Pattern("0", false, "empty pattern");
    }
    
    
    /** @return Pattern that has a sensor node that appears in multiple input nodes */
    public static Pattern getSensorMultipleTimesPattern() {
        Pattern pattern = new Pattern("0", false, "sensor multiple times pattern");
        
        Operand operand = new Operand(intAttr1, intAttr2);
        
        SensorNode in1 = new SensorNode("1", p, "sensor", "sensor node", operand);
        pattern.addInputNode(in1);
        SensorNode in2 = new SensorNode("2", p, "sensor", "sensor node", operand);
        pattern.addInputNode(in2);
        SensorNode in3 = new SensorNode("3", p, "sensor", "sensor node", operand);
        pattern.addInputNode(in3);
        
        FilterOperatorNode filter = new FilterOperatorNode("4", p, Relation.GREATER_EQUAL);
        filter.addInput(in1);
        filter.addInput(in2);
        filter.setFirstAttribute(intAttr1.mutableCopy("1"));
        filter.setSecondAttribute(intAttr2.mutableCopy("2"));
        pattern.addOperatorNode(filter);
        
        
        FunctionAggregationNode aggregation = new FunctionAggregationNode("5", p, AggregationFunction.SUM);
        aggregation.addInput(filter);
        aggregation.setInputAttribute(intAttr2.mutableCopy("1"));
        aggregation.setTimeWindow(new TimeWindow(61_000));
        aggregation.setOutputAttributeName("sumInt");
        pattern.addOperatorNode(aggregation);
        
        
        LogicalOperatorNode logical = new LogicalOperatorNode("6", p, Operation.SEQUENCE);
        logical.addInput(aggregation);
        logical.addInput(in3);
        logical.setLengthWindow(new LengthWindow(1));
        pattern.addOperatorNode(logical);
        
        
        MailActionNode output = new MailActionNode("7", p);
        output.addInput(logical);
        output.setMessage("Hello World");
        output.setRecipientEmail("test@test.com");
        output.setSubject("foo");
        pattern.addOutputNode(output);
        
        return pattern;
    }
    
    
    public static Pattern getInvalidPattern() {
        Pattern pattern = new Pattern("0", false, "invalid pattern");
        pattern.addOutputNode(new MailActionNode("0", p));
        return pattern;
    }
    
    
    public static Pattern getAggregationPattern() {
        Pattern pattern = new Pattern("p1", false, "aggregation pattern");
        
        SensorNode in1 = new SensorNode("1", p, "sensor1", "description", new Operand(doubleAttr1, intAttr1, stringAttr1));
        pattern.addInputNode(in1);
        
        FunctionAggregationNode agg1 = new FunctionAggregationNode("2", p, AggregationFunction.MAXIUMUM);
        in1.setOutput(agg1);
        agg1.setInputAttribute(intAttr1.mutableCopy("1"));
        agg1.setOutputAttributeName("maxI1");
        agg1.setLengthWindow(new LengthWindow(10));
        pattern.addOperatorNode(agg1);
        
        FunctionAggregationNode agg2 = new FunctionAggregationNode("3", p, AggregationFunction.SUM);
        agg1.setOutput(agg2);
        agg2.setInputAttribute(new Attribute("maxI1", "2", AttributeType.INTEGER));
        agg2.setOutputAttributeName("sumMaxI1");
        agg2.setTimeWindow(new TimeWindow(61_000));
        pattern.addOperatorNode(agg2);
        
        SocketActionNode out1 = new SocketActionNode("4", p);
        out1.setMessage("Hello World");
        agg2.setOutput(out1);
        pattern.addOutputNode(out1);
        
        return pattern;
    }
    
    
    public static Pattern getCountAggregationPattern() {
        Pattern pattern = new Pattern("p1", false, "aggregation pattern");
        
        SensorNode in1 = new SensorNode("1", p, "sensor1", "description", new Operand(intAttr1));
        pattern.addInputNode(in1);
        
        CountAggregationNode count = new CountAggregationNode("2", p);
        in1.setOutput(count);
        count.setOutputAttributeName("count");
        count.setLengthWindow(new LengthWindow(10));
        count.setTimeWindow(new TimeWindow(62_999));
        pattern.addOperatorNode(count);
        
        SocketActionNode out1 = new SocketActionNode("3", p);
        out1.setMessage("Hello World");
        count.setOutput(out1);
        pattern.addOutputNode(out1);
        
        return pattern;
    }
    
    
    public static Pattern getConstantFilterPattern() {
        Pattern pattern = new Pattern("p1", false, "aggregation pattern");
        
        SensorNode in1 = new SensorNode("1-2-3", p, "sensor1", "description",
                new Operand(doubleAttr1, intAttr1, stringAttr1));
        pattern.addInputNode(in1);
        
        ConstantInputNode in2 = new ConstantDoubleNode("1", p, 10.5);
        ConstantInputNode in3 = new ConstantDoubleNode("1", p, 15.5);
        
        FilterOperatorNode fil1 = new FilterOperatorNode("2-3-4", p, Relation.GREATER_EQUAL);
        in1.setOutput(fil1);
        in2.setOutput(fil1);
        fil1.setFirstAttribute(doubleAttr1.mutableCopy("1-2-3"));
        fil1.setSecondAttribute(in2.asAttribute());
        pattern.addOperatorNode(fil1);
        
        FilterOperatorNode fil2 = new FilterOperatorNode("3-4-5", p, Relation.SMALLER);
        fil1.setOutput(fil2);
        in3.setOutput(fil2);
        fil2.setFirstAttribute(in3.asAttribute());
        fil2.setSecondAttribute(doubleAttr1.mutableCopy("1-2-3"));
        pattern.addOperatorNode(fil2);
        
        SocketActionNode out1 = new SocketActionNode("4-5-6", p);
        out1.setMessage("Hello World");
        fil2.setOutput(out1);
        pattern.addOutputNode(out1);
        
        return pattern;
    }
    
    
    public static Pattern getFilterPattern() {
        Pattern pattern = new Pattern("p1", false, "aggregation pattern");
        
        SensorNode in1 = new SensorNode(uuid(), p, "sensor1", "description",
                new Operand(doubleAttr1, intAttr1, stringAttr1));
        pattern.addInputNode(in1);
        SensorNode in2 = new SensorNode(uuid(), p, "sensor1", "description",
                new Operand(doubleAttr1, intAttr1, stringAttr1));
        pattern.addInputNode(in2);
        
        
        FilterOperatorNode fil1 = new FilterOperatorNode(uuid(), p, Relation.SMALLER_EQUAL);
        in1.setOutput(fil1);
        in2.setOutput(fil1);
        fil1.setFirstAttribute(intAttr1.mutableCopy(in1.getId()));
        fil1.setSecondAttribute(intAttr1.mutableCopy(in2.getId()));
        pattern.addOperatorNode(fil1);
        
        SocketActionNode out1 = new SocketActionNode(uuid(), p);
        out1.setMessage("Hello World");
        fil1.setOutput(out1);
        pattern.addOutputNode(out1);
        
        return pattern;
    }
    
    
    public static Pattern getMultipleOutputsPattern() {
        Pattern pattern = new Pattern("p1", false, "aggregation pattern");
        
        SensorNode in1 = new SensorNode("1-1", p, "sensor1", "description", new Operand(intAttr1));
        pattern.addInputNode(in1);
        
        FunctionAggregationNode agg1 = new FunctionAggregationNode("1-2", p, AggregationFunction.MAXIUMUM);
        in1.setOutput(agg1);
        agg1.setInputAttribute(intAttr1.mutableCopy(in1.getId()));
        agg1.setOutputAttributeName("maxI");
        agg1.setLengthWindow(new LengthWindow(5));
        pattern.addOperatorNode(agg1);
        
        SocketActionNode out1 = new SocketActionNode("1-3", p);
        out1.setMessage("Hello World");
        agg1.setOutput(out1);
        pattern.addOutputNode(out1);
        
        
        SensorNode in2 = new SensorNode("2-1", p, "sensor1", "description", new Operand(intAttr1));
        pattern.addInputNode(in2);
        
        FunctionAggregationNode agg2 = new FunctionAggregationNode("2-2", p, AggregationFunction.MINIMUM);
        in2.setOutput(agg2);
        agg2.setInputAttribute(intAttr1.mutableCopy(in2.getId()));
        agg2.setOutputAttributeName("minI");
        agg2.setLengthWindow(new LengthWindow(5));
        pattern.addOperatorNode(agg2);
        
        SocketActionNode out2 = new SocketActionNode("2-3", p);
        out2.setMessage("Hello World");
        agg2.setOutput(out2);
        pattern.addOutputNode(out2);
        
        
        SensorNode in3 = new SensorNode("3-1", p, "sensor1", "description", new Operand(intAttr1));
        pattern.addInputNode(in3);
        
        FunctionAggregationNode agg3 = new FunctionAggregationNode("3-2", p, AggregationFunction.SUM);
        in3.setOutput(agg3);
        agg3.setInputAttribute(intAttr1.mutableCopy(in3.getId()));
        agg3.setOutputAttributeName("sumI");
        agg3.setLengthWindow(new LengthWindow(5));
        pattern.addOperatorNode(agg3);
        
        SocketActionNode out3 = new SocketActionNode("3-3", p);
        out3.setMessage("Hello World");
        agg3.setOutput(out3);
        pattern.addOutputNode(out3);
        
        
        SensorNode in4 = new SensorNode("4-1", p, "sensor1", "description", new Operand(intAttr1));
        pattern.addInputNode(in4);
        
        FunctionAggregationNode agg4 = new FunctionAggregationNode("4-2", p, AggregationFunction.AVERAGE);
        in4.setOutput(agg4);
        agg4.setInputAttribute(intAttr1.mutableCopy(in4.getId()));
        agg4.setOutputAttributeName("averageI");
        agg4.setLengthWindow(new LengthWindow(5));
        pattern.addOperatorNode(agg4);
        
        SocketActionNode out4 = new SocketActionNode("4-3", p);
        out4.setMessage("Hello World");
        agg4.setOutput(out4);
        pattern.addOutputNode(out4);
        
        return pattern;
    }
    
    
    public static Pattern getLogicalPattern() {
        Pattern pattern = new Pattern("p1", false, "aggregation pattern");
        
        SensorNode in1a = new SensorNode("1-1a", p, "sensor1", "description", new Operand(stringAttr1));
        pattern.addInputNode(in1a);
        
        SensorNode in1b = new SensorNode("1-1b", p, "sensor1", "description", new Operand(stringAttr1));
        pattern.addInputNode(in1b);
        
        LogicalOperatorNode log1 = new LogicalOperatorNode("1-2", p, Operation.OR);
        in1a.setOutput(log1);
        in1b.setOutput(log1);
        log1.setTimeWindow(new TimeWindow(15_000));
        pattern.addOperatorNode(log1);
        
        SocketActionNode out1 = new SocketActionNode("1-3", p);
        out1.setMessage("Hello World");
        log1.setOutput(out1);
        pattern.addOutputNode(out1);
        
        
        SensorNode in2a = new SensorNode("2-1a", p, "sensor1", "description", new Operand(stringAttr1));
        pattern.addInputNode(in2a);
        
        SensorNode in2b = new SensorNode("2-1b", p, "sensor1", "description", new Operand(stringAttr1));
        pattern.addInputNode(in2b);
        
        LogicalOperatorNode log2 = new LogicalOperatorNode("2-2", p, Operation.AND);
        in2a.setOutput(log2);
        in2b.setOutput(log2);
        log2.setTimeWindow(new TimeWindow(15_000));
        pattern.addOperatorNode(log2);
        
        SocketActionNode out2 = new SocketActionNode("2-3", p);
        out2.setMessage("Hello World");
        log2.setOutput(out2);
        pattern.addOutputNode(out2);
        
        
        SensorNode in3a = new SensorNode("3-1a", p, "sensor1", "description", new Operand(stringAttr1));
        pattern.addInputNode(in3a);
        
        SensorNode in3b = new SensorNode("3-1b", p, "sensor1", "description", new Operand(stringAttr1));
        pattern.addInputNode(in3b);
        
        LogicalOperatorNode log3 = new LogicalOperatorNode("3-2", p, Operation.SEQUENCE);
        in3a.setOutput(log3);
        in3b.setOutput(log3);
        log3.setInputSequence(Arrays.asList("3-1b", "3-1a"));
        log3.setTimeWindow(new TimeWindow(15_000));
        pattern.addOperatorNode(log3);
        
        SocketActionNode out3 = new SocketActionNode("3-3", p);
        out3.setMessage("Hello World");
        log3.setOutput(out3);
        pattern.addOutputNode(out3);
        
        return pattern;
    }
    
    
    /*
     *  (Temperature Sensor)-----\
     *                            (>= Filter)-------\
     *  (Constant 20°C Double)---/                   \                     [24h TimeWindow]
     *                                                \                           |
     *                                                 \-----------------(&& Logical Operator)--------(Mail Action)
     *                    [10# LengthWindow]                            /
     *                            |                                    /
     *  (Rain Sensor)-----(max Aggregation)------\                    /
     *                                            (< Filter)---------/
     *  (Constant 5mm Double)--------------------/
     */
    /**
     * @return good weather pattern by Florian
     */
    public static Pattern getGoodWeatherPattern() {
        final Pattern pattern = new Pattern(uuid(), false, "Florian's Good Weather Pattern (c)");

        // create temp sensor & const °C input
        final String tempSensorId = uuid();
        final Attribute tempAttribute = new Attribute("outside_temperature", tempSensorId, AttributeType.DOUBLE);
        final SensorNode tempSensor = new SensorNode(tempSensorId, p, "outside temperature sensor",
                "desc", new Operand(tempAttribute));
        pattern.addInputNode(tempSensor);

        final ConstantInputNode twentyDegreeNode = new ConstantDoubleNode(uuid(), p, 20.0);
        pattern.addInputNode(twentyDegreeNode);

        // create the >= Filter
        final FilterOperatorNode tempFilter = new FilterOperatorNode(uuid(), p, Relation.GREATER_EQUAL);
        pattern.addOperatorNode(tempFilter);
        tempFilter.addInput(tempSensor);
        tempFilter.setFirstAttribute(tempAttribute);
        tempFilter.addInput(twentyDegreeNode);
        tempFilter.setSecondAttribute(twentyDegreeNode.asAttribute());

        // create the rain sensor & max aggregation
        final String rainSensorId = uuid();
        final Attribute rainAttribute = new Attribute("rain", rainSensorId, AttributeType.DOUBLE);
        final SensorNode rainSensor = new SensorNode(rainSensorId, p, "rain sensor",
                "desc", new Operand(rainAttribute));
        pattern.addInputNode(rainSensor);

        final FunctionAggregationNode maxNode = new FunctionAggregationNode(uuid(), p, AggregationFunction.MAXIUMUM);
        pattern.addOperatorNode(maxNode);
        // set the length-10 window for the max aggregation
        maxNode.setLengthWindow(new LengthWindow(10));
        maxNode.setOutputAttributeName("max_10_rain");

        maxNode.addInput(rainSensor);
        maxNode.setInputAttribute(rainAttribute);

        // create the const 5mm input & < filter
        final ConstantInputNode fiveMmNode = new ConstantDoubleNode(uuid(), p, 5.0);
        pattern.addInputNode(fiveMmNode);

        final FilterOperatorNode rainFilter = new FilterOperatorNode(uuid(), p, Relation.SMALLER);
        pattern.addOperatorNode(rainFilter);

        rainFilter.addInput(maxNode);
        rainFilter.setFirstAttribute(maxNode.getAggregationOutputAttribute());
        rainFilter.addInput(fiveMmNode);
        rainFilter.setSecondAttribute(fiveMmNode.asAttribute());

        // add logical && operator that combines rain and temperature
        final LogicalOperatorNode andNode = new LogicalOperatorNode(uuid(), p, Operation.AND);
        pattern.addOperatorNode(andNode);

        andNode.setTimeWindow(new TimeWindow(24 * 60 * 60 * 1000));
        andNode.addInput(rainFilter);
        andNode.addInput(tempFilter);

        // add mail action
        final MailActionNode mailNode = new MailActionNode(uuid(), p);
        pattern.addOutputNode(mailNode);
        mailNode.setSubject("Go Outside!");
        mailNode.setMessage("Why are you still here?");
        mailNode.setRecipientEmail("florian.pfisterer@student.kit.edu");

        andNode.setOutput(mailNode);
        
        return pattern;
    }
    
    
    /*
     *  (Temperature Sensor)-----\
     *                            (< Filter)--------\
     *  (Constant 20°C Double)---/                   \                     [20 sec TimeWindow]
     *                                                \                           |
     *                                                 \-----------------(-> Logical Operator)--------(Mail Action)
     *                    [10# LengthWindow]                            /
     *                            |                                    /
     *  (Rain Sensor)-----(avg Aggregation)------\                    /
     *                                            (< Filter)---------/
     *  (Rain Sensor)----------------------------/
     */
    /**
     * @return good weather pattern by Florian
     */
    public static Pattern getBadWeatherPattern() {
        final Pattern pattern = new Pattern(uuid(), false, "Jonas' Bad Weather Pattern (c)");
        
        // create temp sensor & const °C input
        final String tempSensorId = "temperature";
        final Attribute tempAttribute = new Attribute("outside_temperature", tempSensorId, AttributeType.DOUBLE);
        final SensorNode tempSensor = new SensorNode(tempSensorId, p, "outside temperature sensor",
                "desc", new Operand(tempAttribute));
        pattern.addInputNode(tempSensor);
        
        final ConstantInputNode twentyDegreeNode = new ConstantDoubleNode(uuid(), p, 20.0);
        pattern.addInputNode(twentyDegreeNode);
        
        // create the < Filter
        final FilterOperatorNode tempFilter = new FilterOperatorNode(uuid(), p, Relation.SMALLER);
        pattern.addOperatorNode(tempFilter);
        tempFilter.addInput(tempSensor);
        tempFilter.setFirstAttribute(tempAttribute);
        tempFilter.addInput(twentyDegreeNode);
        tempFilter.setSecondAttribute(twentyDegreeNode.asAttribute());
        
        // create the rain sensors & max aggregation
        final String rainSensorId = "rain1";
        final Attribute rainAttribute = new Attribute("rain", rainSensorId, AttributeType.INTEGER);
        final SensorNode rainSensor = new SensorNode(rainSensorId, p, "rain sensor",
                "desc", new Operand(rainAttribute));
        pattern.addInputNode(rainSensor);
        
        final String rainSensorId2 = "rain2";
        final Attribute rainAttribute2 = new Attribute("rain", rainSensorId2, AttributeType.INTEGER);
        final SensorNode rainSensor2 = new SensorNode(rainSensorId2, p, "rain sensor",
                "desc", new Operand(rainAttribute2));
        pattern.addInputNode(rainSensor2);
        
        final FunctionAggregationNode maxNode = new FunctionAggregationNode(uuid(), p, AggregationFunction.AVERAGE);
        pattern.addOperatorNode(maxNode);
        // set the length-10 window for the max aggregation
        maxNode.setLengthWindow(new LengthWindow(10));
        maxNode.setOutputAttributeName("max_10_rain");
        
        maxNode.addInput(rainSensor);
        maxNode.setInputAttribute(rainAttribute);
        
        // create the < filter
        final FilterOperatorNode rainFilter = new FilterOperatorNode(uuid(), p, Relation.SMALLER);
        pattern.addOperatorNode(rainFilter);
        
        rainFilter.addInput(maxNode);
        rainFilter.addInput(rainSensor2);
        rainFilter.setFirstAttribute(maxNode.getAggregationOutputAttribute());
        rainFilter.setSecondAttribute(rainAttribute2);
        
        // add logical && operator that combines rain and temperature
        final LogicalOperatorNode andNode = new LogicalOperatorNode(uuid(), p, Operation.SEQUENCE);
        pattern.addOperatorNode(andNode);
        
        andNode.setTimeWindow(new TimeWindow(20_000));
        andNode.addInput(rainFilter);
        andNode.addInput(tempFilter);
        andNode.setInputSequence(Arrays.asList(new String[] {tempFilter.getId(), rainFilter.getId()}));
        
        // add mail action
        final MailActionNode mailNode = new MailActionNode(uuid(), p);
        pattern.addOutputNode(mailNode);
        mailNode.setSubject("Stay at home!");
        mailNode.setMessage("It's always raining...");
        mailNode.setRecipientEmail("ufvsd@student.kit.edu");
        
        andNode.setOutput(mailNode);
        
        return pattern;
    }
    
    
    private static String uuid() {
        return UUID.randomUUID().toString();
    }
    
}
