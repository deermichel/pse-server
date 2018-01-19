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
    
    /** USER_REMOVED */
    USER_REMOVED("user_removed"),
    
    /** USER_NOT_EXISTS */
    USER_NOT_EXISTS("user_not_exists"),
    
    /** PATTERN_NOT_EXISTS */
    PATTERN_NOT_EXISTS("pattern_not_exists"),
    
    /** INV_REMOVEUSER_SYNTAX */
    INV_REMOVEUSER_SYNTAX("inv_removeuser_syntax"),
    
    /** USER_LOGGED_IN */
    USER_LOGGED_IN("user_logged_in"),
    
    /** USER_LOGGED_OUT */
    USER_LOGGED_OUT("user_logged_out"),
    
    /** INV_STOP_SYNTAX */
    INV_STOP_SYNTAX("inv_stop_syntax"),
    
    /** INV_HELP_SYNTAX */
    INV_HELP_SYNTAX("inv_help_syntax"),
    
    /** INV_LISTUSERS_SYNTAX */
    INV_LISTUSERS_SYNTAX("inv_listusers_syntax"),
    
    /** USERS **/
    USERS("users"),
    
    /** PATTERN_CREATED */
    PATTERN_CREATED("pattern_created"),
    
    /** PATTERN_UPDATED */
    PATTERN_UPDATED("pattern_updated"),
    
    /** PATTERN_DELETED */
    PATTERN_DELETED("pattern_deleted"),
    
    /** PATTERN_DEPLOYED */
    PATTERN_DEPLOYED("pattern_deployed"),
    
    /** PATTERN_UNDEPLOYED */
    PATTERN_UNDEPLOYED("pattern_undeployed"),
    
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
