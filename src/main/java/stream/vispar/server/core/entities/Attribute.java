package stream.vispar.server.core.entities;

/**
 * Attributes are contained in {@link Event events} and consist of a name and a type.
 * 
 * @author Micha Hanselmann
 */
public class Attribute {

    /**
     * Attribute types.
     */
    public enum AttributeType {
        
        /**
         * Double
         */
        DOUBLE, 
        
        /**
         * Integer
         */
        INTEGER, 
        
        /**
         * String
         */
        STRING
    }
    
}
