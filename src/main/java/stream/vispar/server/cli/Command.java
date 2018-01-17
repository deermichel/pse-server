package stream.vispar.server.cli;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.entities.User;
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
    ADD_USER("adduser", "adduser ([^ ]+) ([^ ]+)") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            Matcher matcher = getMatcher(input);
            if (matcher.matches()) {
                
                User user = new User(matcher.group(1), matcher.group(2));
                try {
                    instance.getUserCtrl().add(user);
                    return new StringCommandResult(instance.getLocalizer().get(LocalizedString.OK));
                } catch (IllegalArgumentException e) {
                    return new StringCommandResult(e.getMessage());
                }
                
            } else {
                return new StringCommandResult(
                        instance.getLocalizer().get(LocalizedString.INV_ADDUSER_SYNTAX));
            }
        }
    },
    
    /**
     * Command to remove an user.
     */
    REMOVE_USER("removeuser", "removeuser ([^ ]+)") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            Matcher matcher = getMatcher(input);
            if (matcher.matches()) {
                
                User user = new User(matcher.group(1), "");
                try {
                    instance.getUserCtrl().remove(user);
                    return new StringCommandResult(instance.getLocalizer().get(LocalizedString.OK));
                } catch (IllegalArgumentException e) {
                    return new StringCommandResult(e.getMessage());
                }
                
            } else {
                return new StringCommandResult(
                        instance.getLocalizer().get(LocalizedString.INV_REMOVEUSER_SYNTAX));
            }
        }
    },
    
    /**
     * Command to list all users.
     */
    LIST_USERS("listusers", "listusers") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            if (getMatcher(input).matches()) {
                List<String> usernames = instance.getUserCtrl().getAll()
                        .stream().map(user -> user.getName()).collect(Collectors.toList());
                String result = String.format(instance.getLocalizer().get(LocalizedString.USERS), 
                        String.join(", ", usernames));
                return new StringCommandResult(result);
            } else {
                return new StringCommandResult(
                        instance.getLocalizer().get(LocalizedString.INV_LISTUSERS_SYNTAX));
            }
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
    STOP_SERVER("stop", "stop") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            if (getMatcher(input).matches()) {
                instance.stop();
                return new StringCommandResult(
                        instance.getLocalizer().get(LocalizedString.SERVER_STOPPED));
            } else {
                return new StringCommandResult(
                        instance.getLocalizer().get(LocalizedString.INV_STOP_SYNTAX));
            }
        }
    },
    
    /**
     * Command to print all available commands.
     */
    HELP("help", "help") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            if (getMatcher(input).matches()) {
                return new StringCommandResult(
                        instance.getLocalizer().get(LocalizedString.AVAILABLE_COMMANDS));
            } else {
                return new StringCommandResult(
                        instance.getLocalizer().get(LocalizedString.INV_HELP_SYNTAX));
            }
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
