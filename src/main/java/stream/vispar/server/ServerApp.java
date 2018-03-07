package stream.vispar.server;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import java.util.Objects;

import stream.vispar.server.cli.Command;
import stream.vispar.server.cli.CommandResult;
import stream.vispar.server.cli.DefaultConsole;
import stream.vispar.server.cli.IConsole;
import stream.vispar.server.core.ServerConfig;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.localization.LocalizedString;
import stream.vispar.server.logger.ConsoleLogger;
import stream.vispar.server.logger.FileLogger;
import stream.vispar.server.logger.MultiLogger;

/**
 * Main class representing the entry point of the Vispar server.
 * 
 * @author Micha Hanselmann
 * @version 1.0.0
 */
public final class ServerApp {
    
    /**
     * Version string.
     */
    public static final String VERSION = "1.0.0";
    
    /**
     * Console used for user interaction and output.
     */
    private final IConsole console;
    
    /**
     * Server instance held by the program.
     */
    private final ServerInstance instance;
    

    /**
     * Entry point of the program invoked with cli args.
     * 
     * @param args 
     *          the cli args.
     */
    public static void main(String[] args) {
        new ServerApp();
    }
    
    /**
     * Constructs a new {@link ServerApp} instance.
     */
    private ServerApp() {
        
        // use system default console
        console = new DefaultConsole();
        
        // setup logging
        MultiLogger logger = new MultiLogger();
        logger.addLogger(new ConsoleLogger(console, true));
        String logName = "Vispar_" + new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date()) + ".log";
        logger.addLogger(new FileLogger(logName, true));
        
        // retrieve startup args or set to default
        int requestPort = 0;
        int socketPort = 0;
        try {
            requestPort = Integer.valueOf(System.getProperty("requestport", "8080"));
            socketPort = Integer.valueOf(System.getProperty("socketport", "8081"));
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Port number must be an integer: " + e.toString());
            System.exit(1);
        }
        String databaseUrl = System.getProperty("database", "localhost");
        String configPath = System.getProperty("configpath", "sensors");
        
        // create server config
        ServerConfig config = new ServerConfig(requestPort, socketPort, Locale.US, logger, databaseUrl, configPath);
        
        // setup server instance
        instance = new ServerInstance(config);
        instance.start();
        
        // command REPL
        if (System.getProperty("noshell") == null) {
            commandREPL();
            
            // stop (else scheduled simulations might continue sending their events)
            System.exit(0);
            
        } else {
            instance.getLogger().log(instance.getLocalizer().get(LocalizedString.NOSHELL));
        }
    }
    
    /**
     * REPL loop handling user input / commands.
     */
    private void commandREPL() {
        // loop
        while (instance.isRunning()) {
            String userInput = console.read();
            
            // try to parse and execute command (else log error)
            try {
                Optional<CommandResult> result = Arrays.stream(Command.values())
                        .map(command -> command.handle(instance, userInput))
                        .filter(Objects::nonNull)
                        .findFirst();
                if (result.isPresent()) {
                    // executed -> print result
                    console.println(result.get().getMessage());
                } else {
                    // no command matched -> print help
                    console.println(instance.getLocalizer().get(LocalizedString.ENTER_VALID_COMMAND));
                }
            } catch (IllegalArgumentException e) {
                instance.getLogger().logError(e.getMessage());
            }
        }
    }
}
