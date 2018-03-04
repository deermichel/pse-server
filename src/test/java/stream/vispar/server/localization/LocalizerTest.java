/**
 * 
 */
package stream.vispar.server.localization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Locale;
import org.junit.Test;

/**
 * Tests for {@link Localizer}.
 * 
 * @author Micha Hanselmann
 */
public class LocalizerTest {

    /**
     * Test method for {@link Localizer#Localizer(Locale)}.
     * 
     * Checks behavior on null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testLocalizerNull() {
        new Localizer(null);
    }

    /**
     * Test method for {@link Localizer#get(LocalizedString)}.
     */
    @Test
    public void testGet() {
        Locale previousDefault = Locale.getDefault();
        Locale.setDefault(Locale.US);
        assertThat(new Localizer(Locale.US).get(LocalizedString.TEST), equalTo("Test_Default"));
        assertThat(new Localizer(Locale.GERMANY).get(LocalizedString.TEST), equalTo("Test_DE"));
        Locale.setDefault(previousDefault);
    }

    /**
     * Test method for {@link Localizer#get(LocalizedString)}.
     * 
     * Checks behavior on null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testGetNull() {
        new Localizer(Locale.US).get(null);
    }
}
