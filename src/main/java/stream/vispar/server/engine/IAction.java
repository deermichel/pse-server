package stream.vispar.server.engine;

import stream.vispar.model.Pattern;

/**
 * Defines functionality of an action used in a {@link Pattern}.
 * 
 * @author Micha Hanselmann
 */
public interface IAction {

    /**
     * Executes the action.
     */
    void execute();
}
