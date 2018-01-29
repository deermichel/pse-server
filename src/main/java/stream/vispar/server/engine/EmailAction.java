package stream.vispar.server.engine;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Action implementation that sends a email with predefined address, subject and message.
 * Requires email server (SMTP server - port 25) running on localhost.
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
        
        // set host and get session
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        Session session = Session.getInstance(props);
        
        try {
            
            // create message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("VisparServer@" + InetAddress.getLocalHost().getHostName()));
            msg.setRecipient(RecipientType.TO, new InternetAddress(recipient));
            msg.setSubject(subject);
            msg.setText(message);
            msg.setSentDate(new Date());
            
            // send
            Transport.send(msg);
            
        } catch (MessagingException | UnknownHostException e) {
            System.out.println("Mail error: " + e.toString());
        }
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
