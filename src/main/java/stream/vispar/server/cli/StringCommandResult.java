package stream.vispar.server.cli;

/**
 * Result expressed as a string of an execution of a {@link Command}.
 * 
 * @author Micha Hanselmann
 */
public class StringCommandResult extends CommandResult {
    
    /**
     * String message.
     */
    private final String message;
    
    
    /**
     * Constructs a new {@link StringCommandResult}.
     * 
     * @param message 
     *              the result message.
     */
    public StringCommandResult(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
