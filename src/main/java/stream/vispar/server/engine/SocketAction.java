package stream.vispar.server.engine;

import java.util.Objects;

import stream.vispar.server.core.ISocketHandler;

/**
 * Action implementation that sends a predefined message over a websocket.
 * 
 * @author Micha Hanselmann
 */
public class SocketAction implements IAction {
    
    /**
     * Socket handler used by the action.
     */
    private final ISocketHandler sockHandler;
    
    /**
     * Message sent by the action.
     */
    private final String message;
    
    
    /**
     * Constructs a new {@link SocketAction}.
     * 
     * @param sockHandler
     *          the {@link ISocketHandler} to be used.
     * @param message
     *          the message to be sent.
     */
    public SocketAction(ISocketHandler sockHandler, String message) {
        this.sockHandler = Objects.requireNonNull(sockHandler);
        this.message = Objects.requireNonNull(message);
    }

    @Override
    public void execute() {
        sockHandler.sendMessage(message);
    }
}
