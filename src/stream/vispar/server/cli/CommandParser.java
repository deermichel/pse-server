package stream.vispar.server.cli;

/**
 * Parser used to parse user input to {@link Command commands}.
 * 
 * @author Micha Hanselmann
 */
public class CommandParser {

    /**
     * Constructs a new {@link CommandParser} instance.
     */
    public CommandParser() {
    }
    
    /**
     * Parses user input to a command.
     * 
     * @param input
     *          the user input to be parsed.
     * @return
     *          the parsed {@link Command}.
     * @throws IllegalArgumentException
     *          if input could not be parsed (containing an error message).
     */
    public Command parse(String input) {
        return null;
    }
}
