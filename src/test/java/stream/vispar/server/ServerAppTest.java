/**
 * 
 */
package stream.vispar.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

/**
 * Tests for {@link ServerApp}.
 * 
 * @author Micha Hanselmann
 */
public class ServerAppTest {

    /**
     * Test method for {@link ServerApp#main(java.lang.String[])}.
     */
    @Test
    public void testMain() {
        System.clearProperty("noshell");
        InputStream stream = new ByteArrayInputStream("stop".getBytes());
        InputStream stdIn = System.in;
        System.setIn(stream);
        
        // forbid System.exit call
        SecurityManager secManager = new SecurityManager() {
            public void checkPermission(java.security.Permission perm) {
                if (perm.getName().equals("exitVM.0")) {
                    throw new SecurityException("all right");
                }
            };
        };
        System.setSecurityManager(secManager);
        
        try {
            System.setProperty("requestport", "9080");
            System.setProperty("socketport", "9081");
            System.setProperty("configpath", "./src/test/resources/sensors");
            ServerApp.main(null); // should terminate
        } catch (SecurityException e) {
            // all right
        }
        
        // restore
        System.setSecurityManager(null);
        System.setIn(stdIn);
    }
}
