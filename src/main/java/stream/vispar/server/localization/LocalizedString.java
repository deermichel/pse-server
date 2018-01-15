package stream.vispar.server.localization;

/**
 * Enum holding all the available localized strings.
 * 
 * @author Micha Hanselmann
 */
public enum LocalizedString {
    
    /**
     * TEST
     */
    TEST("test"),
    
    /**
     * ENTER_VALID_COMMAND
     */
    ENTER_VALID_COMMAND("enter_valid_command"),
    
    /**
     * AVAILABLE_COMMANDS
     */
    AVAILABLE_COMMANDS("available_commands"),
    
    /**
     * SERVER_STOPPED
     */
    SERVER_STOPPED("server_stopped"),
    
    /**
     * STARTING_SERVER
     */
    STARTING_SERVER("starting_server"),
    
    /**
     * STOPPING_SERVER
     */
    STOPPING_SERVER("stopping_server");
    
    
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
    LocalizedString(String key) {
        this.key = key;
    }
    
    /**
     * Returns the unique key of the localized string.
     * 
     * @return
     *          the key.
     */
    public String getKey() {
        return key;
    }
}
