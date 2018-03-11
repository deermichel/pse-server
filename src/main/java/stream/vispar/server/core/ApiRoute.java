package stream.vispar.server.core;

import java.util.Collection;
import java.util.Objects;

import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonArray;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonObject;
import stream.vispar.jsonconverter.types.IJsonArray;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.jsonconverter.types.IJsonObject;
import stream.vispar.model.Pattern;
import stream.vispar.model.nodes.Node;
import stream.vispar.model.nodes.inputs.PatternInputNode;
import stream.vispar.model.nodes.outputs.PatternOutputNode;
import stream.vispar.server.core.entities.Sensor;
import stream.vispar.server.core.entities.adapters.NodeVisitorAdapter;

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
                
                // get username and password (hash)
                String password = request.getAsJsonObject().get("data")
                        .getAsJsonObject().getAsJsonPrimitive("password").getAsString().toLowerCase();
                String username = request.getAsJsonObject().get("data")
                        .getAsJsonObject().getAsJsonPrimitive("username").getAsString();
                
                // login and return token
                String token = instance.getAuthMgr().login(username, password);
                IJsonObject data = new GsonJsonObject();
                data.add("token", token);
                response.add("data", data);
                    
            } catch (IllegalArgumentException e) {
                response.add(ERROR_KEY_NAME, RouteError.UNKNOWN_CREDENTIALS.getCode());
            } catch (JsonParseException | NullPointerException e) {
                instance.getLogger().logError(e.toString());
                response.add(ERROR_KEY_NAME, RouteError.INVALID_REQUEST.getCode());
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
                if (isAuthenticated(instance, request)) {
                    String token = request.getAsJsonObject().getAsJsonPrimitive("token").getAsString();
                    instance.getAuthMgr().logout(token);
                }
                
            } catch (JsonParseException | NullPointerException e) {
                instance.getLogger().logError(e.toString()); // who cares?
            }
            return new GsonJsonObject();
        }
    },
    
    /**
     * GET route to get user who is logged in (also used to check if authenticated)
     */
    GET_USERS_ME(RouteType.GET, "/users/me") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            
            // authenticated?
            if (!isAuthenticated(instance, request)) {
                response.add(ERROR_KEY_NAME, RouteError.NOT_AUTHORIZED.getCode());
                return response;
            }
            
            // return username
            IJsonObject data = new GsonJsonObject();
            try {
                data.add("username", request.getAsJsonObject().getAsJsonPrimitive("user").getAsString());
            } catch (JsonParseException e) {
                instance.getLogger().logError(e.toString()); // can't happen!
            }
            response.add("data", data);
            return response;
        }
    },
    
    /**
     * GET route to list all patterns (as proxies).
     */
    GET_PATTERNS_ALL(RouteType.GET, "/patterns/all") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            
            // authenticated?
            if (!isAuthenticated(instance, request)) {
                response.add(ERROR_KEY_NAME, RouteError.NOT_AUTHORIZED.getCode());
                return response;
            }
            
            // get patterns and convert their proxies to json
            IJsonConverter jsonConv = new GsonConverter();
            Collection<Pattern> patterns = instance.getPatternCtrl().getAll();
            IJsonArray json = new GsonJsonArray();
            for (Pattern p : patterns) {
                json.add(jsonConv.toJson(p.getAsProxy()));
            }
            
            // return data
            response.add("data", json); 
            return response;
        }
    },
    
    /**
     * GET route to get a specific pattern.
     */
    GET_PATTERNS(RouteType.GET, "/patterns") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            
            // authenticated?
            if (!isAuthenticated(instance, request)) {
                response.add(ERROR_KEY_NAME, RouteError.NOT_AUTHORIZED.getCode());
                return response;
            }
            
            try {
                    
                // get pattern
                String patternId = request.getAsJsonObject().get("params")
                        .getAsJsonObject().getAsJsonPrimitive("id").getAsString();
                Pattern pattern = instance.getPatternCtrl().getById(patternId);
                
                // return pattern if it exists
                if (pattern != null) {
                    response.add("data", new GsonConverter().toJson(pattern));
                } else {
                    response.add(ERROR_KEY_NAME, RouteError.UNKNOWN_PATTERN.getCode());
                }
                
            } catch (JsonParseException | NullPointerException e) {
                instance.getLogger().logError(e.toString());
                response.add(ERROR_KEY_NAME, RouteError.INVALID_REQUEST.getCode());
            }
            return response;
        }
    },
    
    /**
     * POST route to create or update a pattern.
     */
    POST_PATTERNS(RouteType.POST, "/patterns") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            
            // authenticated?
            if (!isAuthenticated(instance, request)) {
                response.add(ERROR_KEY_NAME, RouteError.NOT_AUTHORIZED.getCode());
                return response;
            }
            
            try {
                    
                // get pattern
                IJsonConverter jsonConv = new GsonConverter();
                Pattern pattern = jsonConv.fromJson(request.getAsJsonObject().get("data"));
                
                // update pattern
                pattern = instance.getPatternCtrl().update(pattern);
                response.add("data", jsonConv.toJson(pattern));
                
            } catch (IllegalArgumentException e) {
                response.add(ERROR_KEY_NAME, RouteError.PATTERN_EDITED.getCode());
            } catch (JsonException | NullPointerException | com.google.gson.JsonParseException e) {
                instance.getLogger().logError(e.toString());
                response.add(ERROR_KEY_NAME, RouteError.INVALID_REQUEST.getCode());
            }
            return response;
        }
    },
    
    /**
     * POST route to delete a pattern.
     */
    DELETE_PATTERNS(RouteType.POST, "/patterns/delete") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            
            // authenticated?
            if (!isAuthenticated(instance, request)) {
                response.add(ERROR_KEY_NAME, RouteError.NOT_AUTHORIZED.getCode());
                return response;
            }
            
            try {
                    
                // get pattern id
                String patternId = request.getAsJsonObject().get("data")
                        .getAsJsonObject().getAsJsonPrimitive("id").getAsString();
                
                // remove pattern
                instance.getPatternCtrl().remove(patternId);
                
            } catch (IllegalArgumentException e) {
                response.add(ERROR_KEY_NAME, RouteError.UNKNOWN_PATTERN.getCode());
            } catch (JsonException | NullPointerException e) {
                instance.getLogger().logError(e.toString());
                response.add(ERROR_KEY_NAME, RouteError.INVALID_REQUEST.getCode());
            }
            return response;
        }
    },
    
    /**
     * POST route to rename a pattern.
     */
    RENAME_PATTERNS(RouteType.POST, "/patterns/rename") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            
            // authenticated?
            if (!isAuthenticated(instance, request)) {
                response.add(ERROR_KEY_NAME, RouteError.NOT_AUTHORIZED.getCode());
                return response;
            }
            
            try {
                    
                // get pattern id and new name
                String patternId = request.getAsJsonObject().get("data")
                        .getAsJsonObject().getAsJsonPrimitive("id").getAsString();
                String newName = request.getAsJsonObject().get("data")
                        .getAsJsonObject().getAsJsonPrimitive("name").getAsString();
                
                // rename pattern
                Pattern pattern = instance.getPatternCtrl().rename(patternId, newName);
                response.add("data", new GsonConverter().toJson(pattern));
                
            } catch (IllegalArgumentException e) {
                response.add(ERROR_KEY_NAME, RouteError.UNKNOWN_PATTERN.getCode());
            } catch (JsonException | NullPointerException e) {
                instance.getLogger().logError(e.toString());
                response.add(ERROR_KEY_NAME, RouteError.INVALID_REQUEST.getCode());
            }
            return response;
        }
    },
    
    /**
     * POST route to deploy a pattern.
     */
    POST_PATTERNS_DEPLOY(RouteType.POST, "/patterns/deploy") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            
            // authenticated?
            if (!isAuthenticated(instance, request)) {
                response.add(ERROR_KEY_NAME, RouteError.NOT_AUTHORIZED.getCode());
                return response;
            }
            
            try {
                    
                // get pattern id
                String patternId = request.getAsJsonObject().get("data")
                        .getAsJsonObject().getAsJsonPrimitive("id").getAsString();
                
                // deploy pattern
                Pattern pattern = instance.getPatternCtrl().deploy(patternId);
                response.add("data", new GsonConverter().toJson(pattern));
                
            } catch (IllegalArgumentException e) {
                response.add(ERROR_KEY_NAME, RouteError.UNKNOWN_PATTERN.getCode());
            } catch (IllegalStateException e) {
                response.add(ERROR_KEY_NAME, e.getMessage());
            } catch (JsonException | NullPointerException e) {
                instance.getLogger().logError(e.toString());
                response.add(ERROR_KEY_NAME, RouteError.INVALID_REQUEST.getCode());
            }
            return response;
        }
    },
    
    /**
     * POST route to undeploy a pattern.
     */
    POST_PATTERNS_UNDEPLOY(RouteType.POST, "/patterns/undeploy") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            
            // authenticated?
            if (!isAuthenticated(instance, request)) {
                response.add(ERROR_KEY_NAME, RouteError.NOT_AUTHORIZED.getCode());
                return response;
            }
            
            try {
                    
                // get pattern id
                String patternId = request.getAsJsonObject().get("data")
                        .getAsJsonObject().getAsJsonPrimitive("id").getAsString();
                
                // undeploy pattern
                Pattern pattern = instance.getPatternCtrl().undeploy(patternId);
                response.add("data", new GsonConverter().toJson(pattern));
                
            } catch (IllegalArgumentException e) {
                response.add(ERROR_KEY_NAME, RouteError.UNKNOWN_PATTERN.getCode());
            } catch (IllegalStateException e) {
                response.add(ERROR_KEY_NAME, RouteError.PATTERN_NOT_DEPLOYED.getCode());
            } catch (JsonException | NullPointerException e) {
                instance.getLogger().logError(e.toString());
                response.add(ERROR_KEY_NAME, RouteError.INVALID_REQUEST.getCode());
            }
            return response;
        }
    },
    
    /**
     * GET route to get all available pattern input nodes.
     */
    GET_PATTERNS_INPUTNODES(RouteType.GET, "/patterns/inputnodes") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            
            // authenticated?
            if (!isAuthenticated(instance, request)) {
                response.add(ERROR_KEY_NAME, RouteError.NOT_AUTHORIZED.getCode());
                return response;
            }
            
            // get patterns and get their pattern output nodes as input nodes and convert to json
            IJsonConverter jsonConv = new GsonConverter();
            Collection<Pattern> patterns = instance.getPatternCtrl().getAll();
            IJsonArray json = new GsonJsonArray();
            for (Pattern p : patterns) {
                for (Node n : p.getAllNodes()) {
                    n.acceptVisitor(new NodeVisitorAdapter() {
                        @Override
                        public void visitPatternOutputNode(PatternOutputNode node) {
                            PatternInputNode current = node.getAsPatternInputNode();
                            if (Objects.nonNull(current)) {
                                json.add(jsonConv.toJson(node.getAsPatternInputNode()));
                            }
                        }
                    });
                }
            }
            
            // return data
            response.add("data", json); 
            return response;
        }
    },
    
    
    /**
     * GET route to list all sensors.
     */
    GET_SENSORS(RouteType.GET, "/sensors") {
        @Override
        public IJsonElement execute(ServerInstance instance, IJsonElement request) {
            IJsonObject response = new GsonJsonObject();
            
            // authenticated?
            if (!isAuthenticated(instance, request)) {
                response.add(ERROR_KEY_NAME, RouteError.NOT_AUTHORIZED.getCode());
                return response;
            }
            
            // get sensors and convert their SensorNode-representation to json
            IJsonConverter jsonConv = new GsonConverter();
            Collection<Sensor> sensors = instance.getSensorCtrl().getAll();
            IJsonArray json = new GsonJsonArray();
            for (Sensor s : sensors) {
                json.add(jsonConv.toJson(s.getSensorNode()));
            }
            
            // return data
            response.add("data", json); 
            return response;
        }
    };
    
    
    private static final String ERROR_KEY_NAME = "error";

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
     * Checks wether the request is authenticated (a user is logged in).
     * 
     * @param request
     *          the request.   
     * @param instance
     *          the currently used {@link ServerInstance}.
     * @return
     *          true if authenticated, false otherwise.
     */
    protected final boolean isAuthenticated(ServerInstance instance, IJsonElement request) {
        try {
            return request.getAsJsonObject().has("user");
        } catch (JsonParseException e) {
            instance.getLogger().logError(e.toString());
        }
        return false;
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
