package stream.vispar.server.localization;

/**
 * Enum holding all the available localized strings.
 * 
 * @author Micha Hanselmann
 */
public enum LocalizedString {
    
    /**
     * A string.
     */
    STRING1("string1");
    
    
    /**
     * Key of the localized string.
     */
    private final String key;
    
    /**
     * Constructs a new {@link LocalizedString}.
     * 
     * @param key
     *          the key for the localized string.
     */
    private LocalizedString(String key) {
        this.key = key;
    }
    
    /**
     * Returns the key of the localized string.
     * 
     * @return
     *          the key.
     */
    public String getKey() {
        return key;
    }
}
