package stream.vispar.server.cli;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.localization.LocalizedString;

/**
 * Defines the commands for the cli that can perform a specific execution on a {@link ServerInstance}.
 * 
 * @author Micha Hanselmann
 */
public enum Command {
    
    /**
     * Command to add new user.
     */
    ADD_USER("adduser", "") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            return null;
        }
    },
    
    /**
     * Command to remove an user.
     */
    REMOVE_USER("removeuser", "") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            return null;
        }
    },
    
    /**
     * Command to list all users.
     */
    LIST_USERS("listusers", "") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            return null;
        }
    },
    
    /**
     * Command to list all patterns.
     */
    LIST_PATTERNS("listpatterns", "") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            return null;
        }
    },
    
    /**
     * Command to start a simulation.
     */
    SIMULATE("simulate", "") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            return null;
        }
    },
    
    /**
     * Command to stop the server.
     */
    STOP_SERVER("stop", "") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            return null;
        }
    },
    
    /**
     * Command to print all available commands.
     */
    HELP("help", "help") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            return new CommandResult() {
                @Override
                public String getMessage() {
                    return instance.getLocalizer().get(LocalizedString.AVAILABLE_COMMANDS);
                }
            };
        }
    };

    
    /**
     * Keyword of the command.
     */
    private final String keyword;
    
    /**
     * Regular expression for parsing the command (precompiled).
     */
    private final Pattern pattern;
    
    
    /**
     * Constructs a new {@link Command}.
     * 
     * @param keyword
     *          the keyword of the command.
     * @param regex
     *          the regular expression for parsing the command.
     */
    Command(String keyword, String regex) {
        this.keyword = keyword;
        this.pattern = Pattern.compile(regex);
    }
    
    /**
     * Tries to handle an input. This will first check whether this command is meant by the input.
     * If not, null will be returned; else the command will be executed with the input on the
     * instance.
     * 
     * @param instance
     *          the target {@link ServerInstance} used for execution.
     * @param input
     *          the (mostly user) input to be parsed.
     * @return
     *          null if input does not match command; the {@link CommandResult} otherwise.
     */
    public CommandResult handle(ServerInstance instance, String input) {
        return (input.trim().toLowerCase().startsWith(keyword)) ? execute(instance, input) : null;
    }
    
    /**
     * Returns a matcher using the regular expression of the command.
     * 
     * @param input
     *          the text to be matched.
     * @return
     *          the {@link Matcher}.
     */
    protected Matcher getMatcher(String input) {
        return pattern.matcher(input);
    }
    
    /**
     * Executes the command.
     * 
     * @param instance
     *          the target {@link ServerInstance} used for execution.
     * @param input
     *          the command input to be parsed.
     * @return
     *          the {@link CommandResult result} of the execution.
     */
    protected abstract CommandResult execute(ServerInstance instance, String input);
}
