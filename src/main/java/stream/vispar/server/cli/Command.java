package stream.vispar.server.cli;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;

import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.entities.Simulation;
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
                
                // create user
                String hashedPassword = DigestUtils.sha512Hex(matcher.group(2)).toLowerCase();
                User user = new User(matcher.group(1), hashedPassword);
                
                // add user if not exists
                try {
                    instance.getUserCtrl().add(user);
                    return new StringCommandResult(
                            instance.getLocalizer().get(LocalizedString.OK));
                } catch (IllegalArgumentException e) {
                    return new StringCommandResult(
                            instance.getLocalizer().get(LocalizedString.USER_ALREADY_EXISTS));
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
                
                // remove user if exists
                try {
                    instance.getUserCtrl().remove(matcher.group(1));
                    return new StringCommandResult(instance.getLocalizer().get(LocalizedString.OK));
                } catch (IllegalArgumentException e) {
                    return new StringCommandResult(
                            instance.getLocalizer().get(LocalizedString.USER_NOT_EXISTS));
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
                
                // get and concat all usernames
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
    LIST_PATTERNS("listpatterns", "listpatterns") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            if (getMatcher(input).matches()) {
                
                // get and concat all pattern names (with their deployment status)
                List<String> patternnames = instance.getPatternCtrl().getAll().stream()
                        .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                        .map(pattern -> {
                            if (pattern.isDeployed()) {
                                return pattern.getName() + " "
                                        + instance.getLocalizer().get(LocalizedString.DEPLOYED);
                            } else {
                                return pattern.getName();
                            }
                        })
                        .collect(Collectors.toList());
                String result = String.format(instance.getLocalizer().get(LocalizedString.PATTERNS), 
                        String.join(", ", patternnames));
                
                return new StringCommandResult(result);
                
            } else {
                return new StringCommandResult(
                        instance.getLocalizer().get(LocalizedString.INV_LISTPATTERNS_SYNTAX));
            }
        }
    },
    
    /**
     * Command to start a simulation.
     */
    SIMULATE("simulate", "simulate ([^ ]+)") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            Matcher matcher = getMatcher(input);
            if (matcher.matches()) {
                
                // run simulation if exists
                try {
                    Simulation simulation = new Simulation(matcher.group(1));
                    simulation.simulate(instance);
                    return new StringCommandResult(String.format(instance.getLocalizer()
                            .get(LocalizedString.RUNNING_SIMULATION), matcher.group(1)));
                } catch (IllegalArgumentException e) {
                    return new StringCommandResult(String.format(instance.getLocalizer()
                            .get(LocalizedString.SIMULATION_FILE_INVALID), e.toString()));
                }
                
            } else {
                return new StringCommandResult(
                        instance.getLocalizer().get(LocalizedString.INV_SIMULATE_SYNTAX));
            }
        }
    },
    
    /**
     * Command to stop the server.
     */
    STOP_SERVER("stop", "stop") {
        @Override
        protected CommandResult execute(ServerInstance instance, String input) {
            if (getMatcher(input).matches()) {
                
                // stop server instance
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
                
                // get and concat all commands
                List<String> commands = Arrays.asList(Command.values()).stream()
                        .map(c -> c.toString())
                        .sorted((c1, c2) -> c1.compareToIgnoreCase(c2))
                        .collect(Collectors.toList());
                return new StringCommandResult(String.format(
                        instance.getLocalizer().get(LocalizedString.AVAILABLE_COMMANDS), 
                        String.join(", ", commands)));
                
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
    
    @Override
    public String toString() {
        return keyword;
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
