package stream.vispar.server.core;

import java.util.Objects;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Service;
import stream.vispar.jsonconverter.IJsonConverter;
import stream.vispar.jsonconverter.exceptions.JsonException;
import stream.vispar.jsonconverter.gson.GsonConverter;
import stream.vispar.jsonconverter.gson.typeadapters.GsonJsonObject;
import stream.vispar.jsonconverter.types.IJsonObject;
import stream.vispar.server.ServerApp;
import stream.vispar.server.core.entities.Event;
import stream.vispar.server.core.entities.Sensor;
import stream.vispar.server.core.entities.User;
import stream.vispar.server.localization.LocalizedString;

/**
 * Request handler implementation using Spark server framework.
 * 
 * @author Micha Hanselmann
 */
public class SparkServer implements IRequestHandler {
    
    /**
     * Server instance the handler belongs to.
     */
    private final ServerInstance instance;
    
    /**
     * Port of the server.
     */
    private final int port;
    
    /**
     * Spark service instance.
     */
    private Service http;
    
    /**
     * Json converter.
     */
    private final IJsonConverter jsonConv;
    
    
    /**
     * Constructs a new {@link SparkServer}.
     * 
     * @param instance
     *              the {@link ServerInstance} the handler belongs to.
     * @param port
     *              the (network) port the server should use.
     */
    public SparkServer(ServerInstance instance, int port) {
        
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Port number has to be between 0 and 65535.");
        }
        
        this.instance = Objects.requireNonNull(instance);
        this.port = port;
        jsonConv = new GsonConverter();
    }

    @Override
    public void start() {
        http = Service.ignite().port(port);
        
        // register error callback
        http.initExceptionHandler((exception) -> {
            instance.getLogger().logError(String.format(
                    instance.getLocalizer().get(LocalizedString.CANNOT_START_SPARK), exception.toString()));
            instance.stop();
            System.exit(1);
        });
        
        // register routes
        http.get("/", (req, res) -> "Vispar Server " + ServerApp.VERSION); // version landing page
        
        // sensor routes
        instance.getSensorCtrl().getAll().forEach(sensor ->
            http.post("/sensor/" + sensor.getEndpoint(), createSensorRoute(sensor)));
        
        // api routes
        http.path("/api", () -> {
            for (ApiRoute route : ApiRoute.values()) {
                switch (route.getType()) {
                case GET:
                    http.get(route.getEndpoint(), createApiRoute(route));
                    break;
                case POST:
                    http.post(route.getEndpoint(), createApiRoute(route));
                    break;
                default:
                    throw new IllegalStateException("Unknown route type");
                }
            }
        });
        
        http.awaitInitialization();
        
        instance.getLogger().log(
                String.format(instance.getLocalizer().get(LocalizedString.LISTENING_FOR_API_REQUESTS), 
                        http.port()));
    }

    @Override
    public void stop() {
        if (http != null) {
            http.stop();
        }
    }
    
    /**
     * Creates a Spark {@link Route} for an {@link ApiRoute}.
     * 
     * @param route
     *              the {@link ApiRoute} to be transformed.
     * @return
     *              the Spark specific {@link Route}.
     */
    private Route createApiRoute(ApiRoute route) {
        return new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                
                // parse request to json
                IJsonObject jsonReq = new GsonJsonObject();
                jsonReq.add("data", jsonConv.fromString(req.body()));
                
                // parse query params (e.g. for GET requests)
                if (!req.queryParams().isEmpty()) {
                    IJsonObject queryParams = new GsonJsonObject();
                    for (String param : req.queryParams()) {
                        queryParams.add(param, req.queryParams(param));
                    }
                    jsonReq.add("params", queryParams);
                }
                
                // inject authentificated user
                String authHeader = req.headers("Authorization");
                if (authHeader != null) {
                    String token = authHeader.replace("Bearer ", "");
                    User user = instance.getAuthMgr().authenticate(token);
                    if (user != null) {
                        jsonReq.add("user", user.getName());
                        jsonReq.add("token", token);
                    }
                }
                
                // execute route and return (send) result
                res.type("application/json");
                return route.execute(instance, jsonReq);
            }
        };
    }
    
    /**
     * Creates a Spark {@link Route} for a sensor route.
     * 
     * @param sensor
     *              the {@link Sensor} which belongs to the route.
     * @return
     *              the Spark {@link Route}.
     */
    private Route createSensorRoute(Sensor sensor) {
        return new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                IJsonObject response = new GsonJsonObject();
                try {
                    
                    // parse event
                    Event event = sensor.parseEvent(jsonConv.fromString(req.body()));
                    
                    // send to engine
                    instance.getEngine().sendEvent(event);
                    
                } catch (IllegalArgumentException | JsonException e) {
                    res.status(400);
                    response.add("error", e.getMessage());
                    instance.getLogger().logError(e.toString());
                }
                res.type("application/json");
                return response;
            }
        };
    }
}
