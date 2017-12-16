package stream.vispar.server;

import java.util.Arrays;
import java.util.Optional;
import java.util.Objects;

import stream.vispar.server.cli.Command;
import stream.vispar.server.cli.CommandResult;
import stream.vispar.server.cli.DefaultConsole;
import stream.vispar.server.cli.IConsole;
import stream.vispar.server.core.ServerInstance;
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
        logger.addLogger(new FileLogger("log.log", true));
        
        // setup server instance
        instance = new ServerInstance(null);
        instance.start();
        
        // command REPL
        commandREPL();
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
                        .map((command) -> command.handle(instance, userInput))
                        .filter(Objects::nonNull)
                        .findFirst();
                if (result.isPresent()) {
                    // executed -> print result
                } else {
                    // no command matched
                }
            } catch (IllegalArgumentException e) {
                instance.getLogger().logError(e.getMessage());
            }
        }
    }
}
