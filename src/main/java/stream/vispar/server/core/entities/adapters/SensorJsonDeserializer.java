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

/**
 * A {@link JsonDeserializer} that performs input validation when parsing a
 * {@link Sensor} from JSON.
 * 
 * @author Nico Weidmann
 */
public class SensorJsonDeserializer implements JsonDeserializer<Sensor> {

    @Override
    public Sensor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        if (!json.isJsonObject()) {
            throw new JsonParseException("Configuration file has to contain a JSON object.");
        }
        JsonObject jsonObj = json.getAsJsonObject();

        // validate sensor config json
        if (!jsonObj.getAsJsonPrimitive("name").getAsString().matches("[a-zA-Z0-9]+")) {
            throw new JsonParseException("Name has to match regex [a-zA-Z0-9]+");
        } else if (!jsonObj.getAsJsonPrimitive("endpoint").getAsString().matches("[a-zA-Z0-9]+")) {
            throw new JsonParseException("Endpoint has to match regex [a-zA-Z0-9]+");
        }

        String attrRegex = "[a-zA-Z0-9]+(\\[[0-9]+\\])?(\\.[a-zA-Z0-9]+(\\[[0-9]+\\])?)*";

        for (Entry<String, JsonElement> attr : jsonObj.getAsJsonObject("attributes").entrySet()) {
            if (!attr.getKey().matches(attrRegex)) {
                throw new JsonParseException("Attributes have to match regex " + attrRegex);
            } else if (!attr.getValue().getAsJsonObject().getAsJsonPrimitive("name").getAsString()
                    .matches("[a-zA-Z0-9]+")) {
                throw new JsonParseException("Attribute names have to match regex [a-zA-Z0-9]+");
            } else if (!attr.getValue().getAsJsonObject().getAsJsonPrimitive("type").getAsString()
                    .matches("INTEGER|DOUBLE|STRING")) {
                throw new JsonParseException("Attribute types have to be INTEGER, DOUBLE or STRING");
            }
        }

        // all checks successful - do conversion
        return new GsonBuilder().registerTypeAdapter(Attribute.class, new JsonAttributeAdapter()).create()
                .fromJson(jsonObj, typeOfT);
    }

}
