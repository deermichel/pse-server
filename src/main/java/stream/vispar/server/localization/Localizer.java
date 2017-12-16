package stream.vispar.server.localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Used to get localized text.
 * 
 * @author Micha Hanselmann
 */
public class Localizer {

    /**
     * Resource bundle containing the strings for the specific language.
     */
    private final ResourceBundle strings;
    
    
    /**
     * Constructs a new {@link Localizer}.
     * 
     * @param locale
     *          the {@link Locale} determining the language to be used. 
     * @throws MissingResourceException
     *          if the locale is not available.
     */
    public Localizer(Locale locale) {
        strings = ResourceBundle.getBundle("Strings", locale);
    }
    
    /**
     * Localizes and returns a string.
     * 
     * @param string
     *          the {@link LocalizedString} to be localized.
     * @return
     *          the resulting localized string.
     */
    public String get(LocalizedString string) {
        return strings.getString(string.getKey());
    }
}
