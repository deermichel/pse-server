package stream.vispar.server.cli;

/**
 * Result of an execution of a {@link Command}.
 * 
 * @author Micha Hanselmann
 */
public interface CommandResult {

    /**
     * Returns the result as a string message.
     * 
     * @return
     *          a stringual representation of the result.
     */
    String getMessage();
}
