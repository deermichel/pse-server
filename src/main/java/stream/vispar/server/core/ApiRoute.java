package stream.vispar.server.core;

import com.google.gson.JsonObject;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.exceptions.JsonSyntaxException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonObject;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.jsonconverter.types.IJsonObject;
import stream.vispar.server.core.entities.User;

/**
 * Defines the routes for the api server provided by a {@link IRequestHandler}.
 * 
 * @author Micha Hanselmann
 */
public enum ApiRoute {

    /**
     * POST route to log in an user.
     */
    POST_LOGIN(RouteType.POST, "/auth/login") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            try {
                String password = request.getAsJsonObject().getAsJsonPrimitive("password").getAsString();
                String username = request.getAsJsonObject().getAsJsonPrimitive("username").getAsString();
                User user = instance.getUserCtrl().getByName(username);
                if (user != null && user.checkPassword(password)) {
                    String token = instance.getAuthMgr().login(user);
                    IJsonObject response = new GsonJsonObject();
                    response.add("token", token);
                    return response;
                }
            } catch (JsonParseException e) {
                instance.getLogger().logError(e.toString());
            }
            return null;
        }
    },
    
    /**
     * POST route to log out an user.
     */
    POST_LOGOUT(RouteType.POST, "/auth/logout") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            return null;
        }
    },
    
    /**
     * GET route to list all patterns or get a specific one.
     */
    GET_PATTERNS(RouteType.GET, "/patterns") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            return null;
        }
    },
    
    /**
     * POST route to create a new pattern.
     */
    POST_PATTERNS(RouteType.POST, "/patterns") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            return null;
        }
    },
    
    /**
     * PUT route to modify a pattern.
     */
    PUT_PATTERNS(RouteType.PUT, "/patterns") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            return null;
        }
    },
    
    /**
     * DELETE route to delete a pattern.
     */
    DELETE_PATTERNS(RouteType.DELETE, "/patterns") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            return null;
        }
    },
    
    /**
     * POST route to deploy a pattern.
     */
    POST_PATTERNS_DEPLOY(RouteType.POST, "/patterns/deploy") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            return null;
        }
    },
    
    /**
     * POST route to undeploy a pattern.
     */
    POST_PATTERNS_UNDEPLOY(RouteType.POST, "/patterns/deploy") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            return null;
        }
    },
    
    /**
     * GET route to list all sensors.
     */
    GET_SENSORS(RouteType.GET, "/sensors") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            return null;
        }
    };
    
    
    /**
     * Endpoint of the route.
     */
    private final String endpoint;
    
    /**
     * Type of the route.
     */
    private final RouteType type;
    
    /**
     * Gson converter.
     */
    protected final IJsonConverter jsonConv;
    
    
    /**
     * Constructs a new {@link ApiRoute}.
     * 
     * @param type
     *          the {@link RouteType} of the route.
     * @param endpoint
     *          the endpoint of the route.
     */
    ApiRoute(RouteType type, String endpoint) {
        this.endpoint = endpoint;
        this.type = type;
        jsonConv = new GsonConverter();
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
     * Returns the type of the route.
     * 
     * @return
     *          the type.
     */
    public RouteType getType() {
        return type;
    }
    
    /**
     * Executes the route.
     * 
     * @param instance
     *          the target {@link ServerInstance} used for execution.
     * @param request
     *          the request to be processed by the route wrapped as {@link IJsonElement}.
     * @return
     *          the response of the route wrapped as {@link IJsonElement}.
     */
    public abstract IJsonElement execute(ServerInstance instance, IJsonElement request);
}
