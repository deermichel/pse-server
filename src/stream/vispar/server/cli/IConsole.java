package stream.vispar.server.cli;

/**
 * Defines functionality of a console.
 * 
 * @author Micha Hanselmann
 */
public interface IConsole {

    /**
     * Prints a string on the console followed by a newline.
     * 
     * @param input
     *          the string to be printed.
     */
    void println(String input);
 
    /**
     * Reads a string from the console.
     * 
     * @return
     *          the read string.
     */
    String read();
}
