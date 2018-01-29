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
    
    /** INV_LISTPATTERNS_SYNTAX */
    INV_LISTPATTERNS_SYNTAX("inv_listpatterns_syntax"),
    
    /** USERS **/
    USERS("users"),
    
    /** PATTERNS **/
    PATTERNS("patterns"),
    
    /** DEPLOYED **/
    DEPLOYED("deployed"),
    
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
    
    /** PATTERN_RECOGNIZED */
    PATTERN_RECOGNIZED("pattern_recognized"),
    
    /** READING_SENSOR_CONFIGS */
    READING_SENSOR_CONFIGS("reading_sensor_configs"),
    
    /** SENSOR_REGISTERED */
    SENSOR_REGISTERED("sensor_registered"),
    
    /** CONFIG_PATH_NOT_VALID */
    CONFIG_PATH_NOT_VALID("config_path_not_valid"),
    
    /** SKIP_INVALID_CONFIG */
    SKIP_INVALID_CONFIG("skip_invalid_config"),
    
    /** SOCKET_OPENED */
    SOCKET_OPENED("socket_opened"),
    
    /** CONNECTED_TO_SOCKET */
    CONNECTED_TO_SOCKET("connected_to_socket"),
    
    /** DISCONNECTED_FROM_SOCKET */
    DISCONNECTED_FROM_SOCKET("disconnected_from_socket"),
    
    /** RECEIVED_EVENT */
    RECEIVED_EVENT("received_event"),
    
    /** INV_SIMULATE_SYNTAX */
    INV_SIMULATE_SYNTAX("inv_simulate_syntax"),
    
    /** RUNNING_SIMULATION */
    RUNNING_SIMULATION("running_simulation"),
    
    /** SIMULATION_FILE_INVALID */
    SIMULATION_FILE_INVALID("simulation_file_invalid"),
    
    /** SIDDHI_ENGINE_STARTED */
    SIDDHI_ENGINE_STARTED("siddhi_engine_started"),
    
    /** SIDDHI_ENGINE_STOPPED */
    SIDDHI_ENGINE_STOPPED("siddhi_engine_stopped"),
    
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
