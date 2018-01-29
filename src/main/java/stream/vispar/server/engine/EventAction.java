package stream.vispar.server.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import stream.vispar.server.core.entities.Event;

/**
 * Action implementation that sends an {@link Event} to an {@link IEngine}.
 * 
 * @author Micha Hanselmann
 */
public class EventAction implements IAction {
    
    /**
     * Event sent by the action.
     */
    private final Event event;
    
    /**
     * Target engine of the action.
     */
    private final IEngine engine;
    
    
    /**
     * Constructs a new {@link EventAction}.
     * 
     * @param engine
     *          target {@link IEngine} of the action.
     * @param event
     *          the {@link Event} to be sent by the action.
     */
    public EventAction(IEngine engine, Event event) {
        this.engine = Objects.requireNonNull(engine);
        this.event = Objects.requireNonNull(event);
    }

    @Override
    public void execute() {
        engine.sendEvent(event);
    }
    
    @Override
    public String toString() {
        Map<String, String> output = new HashMap<>();
        output.put("event", event.toString());
        return output.toString();
    }
}
