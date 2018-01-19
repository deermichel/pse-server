package stream.vispar.server.core;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.JsonObject;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.exceptions.JsonSyntaxException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonArray;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonObject;
import stream.vispar.jsonconverter.types.IJsonArray;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.jsonconverter.types.IJsonObject;
import stream.vispar.model.Pattern;
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
            IJsonObject response = new GsonJsonObject();
            try {
                // parse json
                String password = request.getAsJsonObject().get("data")
                        .getAsJsonObject().getAsJsonPrimitive("password").getAsString();
                String username = request.getAsJsonObject().get("data")
                        .getAsJsonObject().getAsJsonPrimitive("username").getAsString();
                
                // get user and check password
                User user = instance.getUserCtrl().getByName(username);
                if (user != null && user.checkPassword(password)) {
                    String token = instance.getAuthMgr().login(user);
                    response.add("token", token);
                } else {
                    response.add("error", RouteError.UNKNOWN_CREDENTIALS.getCode());
                }
            } catch (JsonParseException | NullPointerException e) {
                instance.getLogger().logError(e.toString());
                response.add("error", RouteError.INVALID_REQUEST.getCode());
            }
            return response;
        }
    },
    
    /**
     * POST route to log out an user.
     */
    POST_LOGOUT(RouteType.POST, "/auth/logout") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            try {
                // logout if user was logged in
                if (request.getAsJsonObject().has("user")) {
                    String token = request.getAsJsonObject().getAsJsonPrimitive("token").getAsString();
                    instance.getAuthMgr().logout(token);
                }
            } catch (JsonParseException e) {
                instance.getLogger().logError(e.toString()); // who cares?
            }
            return new GsonJsonObject();
        }
    },
    
    /**
     * GET route to list all patterns (as proxies).
     */
    GET_PATTERNS_ALL(RouteType.GET, "/patterns/all") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            try {
                // authenticated?
                if (request.getAsJsonObject().has("user")) {
                    
                    // get patterns and convert their proxies to json
                    IJsonConverter jsonConv = new GsonConverter();
                    Collection<Pattern> patterns = instance.getPatternCtrl().getAll();
                    IJsonArray json = new GsonJsonArray();
                    for (Pattern p : patterns) {
                        json.add(jsonConv.toJson(p));
                    }
                    
                    response.add("data", json); // add to response
                } else {
                    response.add("error", RouteError.NOT_AUTHORIZED.getCode());
                }
            } catch (JsonParseException e) {
                instance.getLogger().logError(e.toString());
                response.add("error", RouteError.INVALID_REQUEST.getCode());
            }
            return response;
        }
    },
    
    /**
     * GET route to get a specific pattern.
     */
    GET_PATTERNS(RouteType.GET, "/patterns") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            return null;
        }
    },
    
    /**
     * POST route to create or update a pattern.
     */
    POST_PATTERNS(RouteType.POST, "/patterns") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            return null;
        }
    },
    
    /**
     * POST route to delete a pattern.
     */
    DELETE_PATTERNS(RouteType.POST, "/patterns/delete") {
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
    POST_PATTERNS_UNDEPLOY(RouteType.POST, "/patterns/undeploy") {
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
