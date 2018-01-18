package stream.vispar.server.core;

/**
 * Defines the errors for the api routes.
 * 
 * @author Micha Hanselmann
 */
public enum RouteError {

    // 1xxx - General
    
    /**
     * Invalid request format.
     */
    INVALID_REQUEST(1000),
    
    /**
     * Not authorized.
     */
    NOT_AUTHORIZED(1001),
    
    // 2xxx - Users
    
    /**
     * User not found or password incorrect.
     */
    UNKNOWN_CREDENTIALS(2000),
    
    // 3xxx - Patterns
    
    /**
     * Pattern not found.
     */
    UNKNOWN_PATTERN(3000),
    
    /**
     * Pattern previously edited by another user.
     */
    PATTERN_EDITED(3001),
    
    /**
     * Pattern already deployed.
     */
    PATTERN_ALREADY_DEPLOYED(3002),
    
    /**
     * Pattern not deployed.
     */
    PATTERN_NOT_DEPLOYED(3003);
    
    
    /**
     * Code of the error.
     */
    private final int code;
    
    
    /**
     * Constructs a new {@link RouteError}.
     * 
     * @param code
     *          the code of the error.
     */
    RouteError(int code) {
        this.code = code;
    }
    
    /**
     * Returns the code of the error.
     * 
     * @return
     *          the code.
     */
    public int getCode() {
        return code;
    }
}
