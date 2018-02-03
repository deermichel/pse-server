package stream.vispar.server.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import stream.vispar.jsonconverter.exceptions.JsonParseException;
import stream.vispar.jsonconverter.types.IJsonElement;
import stream.vispar.jsonconverter.types.IJsonObject;

/**
 * In-memory implementation of {@link IDatabaseConnector} for testing purposes.
 */
public class DBConnectorMock implements IDatabaseConnector {

    private Map<String, Collection<IJsonElement>> collections = new HashMap<>();

    @Override
    public void connect() {
        collections.put("patterns", new LinkedList<>());
        // nothing to do here
    }

    @Override
    public void disconnect() {
        // nothing to do here
    }

    @Override
    public void createCollection(String name) {
        if (!collections.containsKey(name)) {
            collections.put(name, new LinkedList<>());
        }
    }

    @Override
    public void dropCollection(String name) {
        Optional.ofNullable(collections.remove(name)).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public IJsonElement insert(String collection, IJsonElement data) {
        Optional.ofNullable(collections.get(collection)).orElseThrow(IllegalArgumentException::new).add(data);
        return data;
    }

    @Override
    public IJsonElement find(String collection, String key, String value) {
        Collection<IJsonElement> col =
                Optional.ofNullable(collections.get(collection)).orElseThrow(IllegalArgumentException::new);

        for (IJsonElement e : col) {
            if (e.isJsonObject()) {
                IJsonObject o;
                try {
                    o = e.getAsJsonObject();
                } catch (JsonParseException e1) {
                    throw new IllegalStateException();
                }
                try {
                    if (o.has(key) && o.get(value).getAsJsonPrimitive().getAsString().equals(value)) {
                        // found entry
                        return o;
                    }
                } catch (JsonParseException e1) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public Collection<IJsonElement> getAll(String collection) {
        return Optional.ofNullable(collections.get(collection)).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public IJsonElement update(String collection, String key, String value, IJsonElement data) {
        Collection<IJsonElement> col = collections.get(collection);
        if (Objects.isNull(col)) {
            throw new IllegalArgumentException();
        }
        IJsonElement toUpdate = this.find(collection, key, value);
        if (Objects.isNull(toUpdate)) {
            throw new IllegalArgumentException();
        }
        col.remove(toUpdate);
        col.add(data);
        return data;
    }

    @Override
    public void delete(String collection, String key, String value) {
        IJsonElement toDelete = this.find(collection, key, value);
        if (Objects.isNull(toDelete)) {
            throw new IllegalArgumentException();
        }

        collections.get(collection).remove(toDelete);
    }
}
