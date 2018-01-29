package stream.vispar.server.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Action implementation that sends a email with predefined address, subject and message.
 * 
 * @author Micha Hanselmann
 */
public class EmailAction implements IAction {
    
    /**
     * Recipient address of the email.
     */
    private final String recipient;
    
    /**
     * Subject of the email.
     */
    private final String subject;
    
    /**
     * Message of the email.
     */
    private final String message;
    
    
    /**
     * Constructs a new {@link EmailAction}.
     * 
     * @param recipient
     *          the email address of the recipient.
     * @param subject
     *          the subject of the email.
     * @param message
     *          the message of the email.
     */
    public EmailAction(String recipient, String subject, String message) {
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
    }

    @Override
    public void execute() {
        
    }
    
    @Override
    public String toString() {
        Map<String, String> output = new HashMap<>();
        output.put("recipient", recipient);
        output.put("subject", subject);
        output.put("message", message);
        return output.toString();
    }
}
