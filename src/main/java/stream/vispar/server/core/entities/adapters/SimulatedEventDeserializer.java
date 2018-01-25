package stream.vispar.server.core.entities.adapters;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import stream.vispar.jsonconverter.gson.adapters.JsonAttributeAdapter;
import stream.vispar.model.nodes.Attribute;
import stream.vispar.server.core.entities.Sensor;
import stream.vispar.server.core.entities.SimulatedEvent;

/**
 * A {@link JsonDeserializer} that performs input validation when parsing a
 * {@link SimulatedEvent} from JSON.
 * 
 * @author Micha Hanselmann
 */
public class SimulatedEventDeserializer implements JsonDeserializer<SimulatedEvent> {

    @Override
    public SimulatedEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        if (!json.isJsonObject()) {
            throw new JsonParseException("Not a valid JSON object.");
        }
        JsonObject jsonObj = json.getAsJsonObject();

        // validate simulated event json
        if (!jsonObj.getAsJsonPrimitive("time").getAsString().matches("[0-9]+")) {
            throw new JsonParseException("Time has to match regex [0-9]+");
        } else if (!jsonObj.getAsJsonPrimitive("sensor").getAsString().matches("[a-zA-Z0-9]+")) {
            throw new JsonParseException("Sensor has to match regex [a-zA-Z0-9]+");
        }
        
        for (Entry<String, JsonElement> data : jsonObj.getAsJsonObject("data").entrySet()) {
            if (!data.getKey().matches("[a-zA-Z0-9]+")) {
                throw new JsonParseException("Attribute names have to match regex [a-zA-Z0-9]+");
            }
            JsonObject value = data.getValue().getAsJsonObject();
            if (!(value.has("fixed") || value.has("range") || value.has("drange") || value.has("random"))) {
                throw new JsonParseException("Value has to be either fixed, range, drange or random");
            }
            if (value.has("fixed") && !value.get("fixed").isJsonPrimitive()) {
                throw new JsonParseException("Fixed value has to be a primitive");
            } else if (value.has("range")) {
                if (value.getAsJsonArray("range").size() != 2) {
                    throw new JsonParseException("Range has to be an array with size 2");
                } else if (!value.getAsJsonArray("range").get(0).getAsString().matches("[0-9]+")) {
                    throw new JsonParseException("Range minimum has to match regex [0-9]+");
                } else if (!value.getAsJsonArray("range").get(1).getAsString().matches("[0-9]+")) {
                    throw new JsonParseException("Range maximum has to match regex [0-9]+");
                }
            } else if (value.has("drange")) {
                if (value.getAsJsonArray("drange").size() != 2) {
                    throw new JsonParseException("Double range has to be an array with size 2");
                } else if (!value.getAsJsonArray("drange").get(0).getAsString().matches("[0-9\\.]+")) {
                    throw new JsonParseException("Double range minimum has to match regex [0-9\\.]+");
                } else if (!value.getAsJsonArray("drange").get(1).getAsString().matches("[0-9\\.]+")) {
                    throw new JsonParseException("Double range maximum has to match regex [0-9\\.]+");
                }
            } else if (value.has("random") && !value.get("random").isJsonArray()) {
                throw new JsonParseException("Random value has to offer an array of options");
            }
        }

        // all checks successful - do conversion
        return new GsonBuilder().create().fromJson(jsonObj, typeOfT);
    }
}