package stream.vispar.server.localization;

/**
 * Enum holding all the available localized strings.
 * 
 * @author Micha Hanselmann
 */
public enum LocalizedString {
    
    /** TEST */
    TEST("test"),
    
    /** ENTER_VALID_COMMAND */
    ENTER_VALID_COMMAND("enter_valid_command"),
    
    /** AVAILABLE_COMMANDS */
    AVAILABLE_COMMANDS("available_commands"),
    
    /** SERVER_STOPPED */
    SERVER_STOPPED("server_stopped"),
    
    /** STARTING_SERVER */
    STARTING_SERVER("starting_server"),
    
    /** STOPPING_SERVER */
    STOPPING_SERVER("stopping_server"),

    /** CANNOT_START_SPARK */
    CANNOT_START_SPARK("cannot_start_spark"),
    
    /** CONNECTED_TO_DATABASE */
    CONNECTED_TO_DATABASE("connected_to_database"),
    
    /** CANNOT_CONNECT_DATABASE */
    CANNOT_CONNECT_DATABASE("cannot_connect_database"),
    
    /** LISTENING_FOR_API_REQUESTS */
    LISTENING_FOR_API_REQUESTS("listening_for_api_requests"),
    
    /** USER_ALREADY_EXISTS */
    USER_ALREADY_EXISTS("user_already_exists"),
    
    /** USER_ADDED */
    USER_ADDED("user_added"),
    
    /** OK */
    OK("ok"),
    
    /** INV_ADDUSER_SYNTAX */
    INV_ADDUSER_SYNTAX("inv_adduser_syntax");
    
    
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
