package stream.vispar.server;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import java.util.Objects;

import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.exceptions.JsonSyntaxException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.model.Pattern;
import stream.vispar.model.nodes.inputs.SensorNode;
import stream.vispar.server.cli.Command;
import stream.vispar.server.cli.CommandResult;
import stream.vispar.server.cli.DefaultConsole;
import stream.vispar.server.cli.IConsole;
import stream.vispar.server.core.MongoDBConnector;
import stream.vispar.server.core.ServerConfig;
import stream.vispar.server.core.ServerInstance;
import stream.vispar.server.core.entities.Sensor;
import stream.vispar.server.core.entities.User;
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
        List<String> argsList = Arrays.asList(args);
        new ServerApp(!argsList.contains("-norepl"));
    }
    
    /**
     * Constructs a new {@link ServerApp} instance.
     * 
     * @param repl
     *          if true, repl is enabled
     */
    private ServerApp(boolean repl) {
       
//        console = new DefaultConsole();
//        MultiLogger logger = new MultiLogger();
//        ServerConfig config = new ServerConfig(8080, 8081, Locale.US, logger, "localhost", "mo");
//        instance = new ServerInstance(config);
//        MongoDBConnector conn = new MongoDBConnector(instance, "localhost");
//        conn.connect();
//        Pattern pat = new Pattern("1473847", false, "Hello");
//        System.out.println(new GsonConverter().toJson(pat).toString());
//        System.exit(1);
        
        // use system default console
        console = new DefaultConsole();
        
        // setup logging
        MultiLogger logger = new MultiLogger();
        logger.addLogger(new ConsoleLogger(console, true));
        logger.addLogger(new FileLogger("log.log", true));
        
        // create server config
        ServerConfig config = new ServerConfig(8080, 8081, Locale.US, logger, "localhost", "mo");
        
        // setup server instance
        instance = new ServerInstance(config);
        instance.start();
        
        
        Pattern pat = new Pattern("1473847", false, "Hello");
        instance.getPatternCtrl().update(pat);
        
        // command REPL
        if (repl) {
            commandREPL();
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
                        .map((command) -> command.handle(instance, userInput))
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
