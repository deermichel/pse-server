package stream.vispar.server.core;

import todo.Json;

/**
 * Defines the routes for the api server provided by a {@link IRequestHandler}.
 * 
 * @author Micha Hanselmann
 */
public enum ApiRoute {

    /**
     * POST route to log in an user.
     */
    POST_LOGIN("/login") {
        @Override
        public Json execute(ServerInstance instance, Json request) {
            return null;
        }
    },
    
    /**
     * POST route to log out an user.
     */
    POST_LOGOUT("/logout") {
        @Override
        public Json execute(ServerInstance instance, Json request) {
            return null;
        }
    },
    
    /**
     * GET route to list all patterns or get a specific one.
     */
    GET_PATTERNS("/patterns") {
        @Override
        public Json execute(ServerInstance instance, Json request) {
            return null;
        }
    },
    
    /**
     * POST route to create a new pattern.
     */
    POST_PATTERNS("/patterns") {
        @Override
        public Json execute(ServerInstance instance, Json request) {
            return null;
        }
    },
    
    /**
     * PUT route to modify a pattern.
     */
    PUT_PATTERNS("/patterns") {
        @Override
        public Json execute(ServerInstance instance, Json request) {
            return null;
        }
    },
    
    /**
     * DELETE route to delete a pattern.
     */
    DELETE_PATTERNS("/patterns") {
        @Override
        public Json execute(ServerInstance instance, Json request) {
            return null;
        }
    },
    
    /**
     * POST route to deploy a pattern.
     */
    POST_PATTERNS_DEPLOY("/patterns/deploy") {
        @Override
        public Json execute(ServerInstance instance, Json request) {
            return null;
        }
    },
    
    /**
     * POST route to undeploy a pattern.
     */
    POST_PATTERNS_UNDEPLOY("/patterns/deploy") {
        @Override
        public Json execute(ServerInstance instance, Json request) {
            return null;
        }
    },
    
    /**
     * GET route to list all sensors.
     */
    GET_SENSORS("/sensors") {
        @Override
        public Json execute(ServerInstance instance, Json request) {
            return null;
        }
    };
    
    
    /**
     * Endpoint of the route.
     */
    private final String endpoint;
    
    
    /**
     * Constructs a new {@link ApiRoute}.
     * 
     * @param endpoint
     *          the endpoint of the route.
     */
    private ApiRoute(String endpoint) {
        this.endpoint = endpoint;
    }
    
    /**
     * Returns the endpoint of the route.
     * 
     * @return
     *          the endpoint.
     */
    public String getEndpoint() {
        return endpoint;
    }
    
    /**
     * Executes the route.
     * 
     * @param instance
     *          the target {@link ServerInstance} used for execution.
     * @param request
     *          the request to be processed by the route.
     * @return
     *          the response of the route.
     */
    public abstract Json execute(ServerInstance instance, Json request);
}
