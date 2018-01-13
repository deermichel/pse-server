package stream.vispar.server.cli;

import java.util.Objects;
import java.util.Scanner;

/**
 * Console implementation using the standard {@link System#out} and {@link System#in}.
 * 
 * @author Micha Hanselmann
 */
public class DefaultConsole implements IConsole {
    
    /**
     * Scanner used to read user input.
     */
    private final Scanner scanner;
    
    
    /**
     * Constructs a new {@link DefaultConsole} instance.
     */
    public DefaultConsole() {
        scanner = new Scanner(System.in); // scan standard system input
    }

    @Override
    public void println(String input) {
        System.out.println(Objects.requireNonNull(input));
    }

    @Override
    public String read() {
        return scanner.nextLine();
    }
}
