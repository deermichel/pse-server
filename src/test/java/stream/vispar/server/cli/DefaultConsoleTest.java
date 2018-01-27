/**
 * 
 */
package stream.vispar.server.cli;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * Tests for {@link DefaultConsole}.
 * 
 * @author Micha Hanselmann
 */
public class DefaultConsoleTest {
    
    private PrintStream stdOut;
    private InputStream stdIn;
    
    
    /**
     * Restore system std console.
     */
    @After
    public void tearDown() {
        System.setOut(stdOut);
        System.setIn(stdIn);
    }
    
    /**
     * Save system std console.
     */
    @Before
    public void setUp() {
        stdOut = System.out;
        stdIn = System.in;
    }

    /**
     * Test method for {@link DefaultConsole#println(java.lang.String)}.
     */
    @Test
    public void testPrintln() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        DefaultConsole console = new DefaultConsole();
        
        console.println("TestPrint");
        assertThat(stream.toString(), equalTo("TestPrint\n"));
        console.println("Second");
        assertThat(stream.toString(), equalTo("TestPrint\nSecond\n"));
    }

    /**
     * Test method for {@link DefaultConsole#println(java.lang.String)}.
     * 
     * Checks behavior on null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testPrintlnNull() {
        new DefaultConsole().println(null);
    }

    /**
     * Test method for {@link DefaultConsole#read()}.
     */
    @Test
    public void testRead() {
        InputStream stream = new ByteArrayInputStream("TestInput".getBytes());
        System.setIn(stream);
        DefaultConsole console = new DefaultConsole();
        
        assertThat(console.read(), equalTo("TestInput"));
    }
}
