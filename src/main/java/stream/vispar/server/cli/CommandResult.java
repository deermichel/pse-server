package stream.vispar.server.cli;

/**
 * Result of an execution of a {@link Command}.
 * 
 * @author Micha Hanselmann
 */
public abstract class CommandResult {

    /**
     * Returns the result as a string message.
     * 
     * @return
     *          a stringual representation of the result.
     */
    protected abstract String getMessage();
}
