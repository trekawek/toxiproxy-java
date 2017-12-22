package eu.rekawek.toxiproxy.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.toxic.Bandwidth;
import eu.rekawek.toxiproxy.model.toxic.Latency;
import eu.rekawek.toxiproxy.model.toxic.Slicer;
import eu.rekawek.toxiproxy.model.toxic.SlowClose;
import eu.rekawek.toxiproxy.model.toxic.Timeout;
import eu.rekawek.toxiproxy.model.toxic.LimitData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToxicList {

    private final HttpClient httpClient;

    private final String path;

    public ToxicList(HttpClient httpClient, String path) {
        this.httpClient = httpClient;
        this.path = path;
    }

    public List<? extends Toxic> getAll() throws IOException {
        List<Toxic> toxics = new ArrayList<Toxic>();
        for (JsonElement json : httpClient.get(path, JsonArray.class)) {
            JsonObject toxicJson = json.getAsJsonObject();
            String toxicName = toxicJson.get("name").getAsString();
            Toxic toxic = ToxicType.createToxic(httpClient, getToxicPath(toxicName), toxicJson);
            toxics.add(toxic);
        }
        return toxics;
    }

    public Toxic get(String name) throws IOException {
        String toxicPath = getToxicPath(name);
        JsonObject json = httpClient.get(toxicPath);
        return ToxicType.createToxic(httpClient, toxicPath, json);
    }

    public <T extends Toxic> T get(String name, Class<T> type) throws IOException {
        Toxic toxic = get(name);
        if (!type.isInstance(toxic)) {
            throw new IllegalArgumentException(String.format("Invalid type for the toxic %s. It should be a %s.", name, toxic.getClass().getSimpleName()));
        }
        return (T) toxic;
    }

    public Bandwidth bandwidth(String name, ToxicDirection direction, long rate) throws IOException {
        return new Bandwidth(httpClient, path, name, direction, rate);
    }

    public Latency latency(String name, ToxicDirection direction, long latency) throws IOException {
        return new Latency(httpClient, path, name, direction, latency);
    }

    public Slicer slicer(String name, ToxicDirection direction, long averageSize, long delay) throws IOException {
        return new Slicer(httpClient, path, name, direction, averageSize, delay);
    }

    public SlowClose slowClose(String name, ToxicDirection direction, long delay) throws IOException {
        return new SlowClose(httpClient, path, name, direction, delay);
    }

    public Timeout timeout(String name, ToxicDirection direction, long timeout) throws IOException {
        return new Timeout(httpClient, path, name, direction, timeout);
    }

    public LimitData limitData(String name, ToxicDirection direction, long bytes) throws IOException {
        return new LimitData(httpClient, path, name, direction, bytes);
    }

    private String getToxicPath(String name) {
        return path + "/" + name;
    }
}
