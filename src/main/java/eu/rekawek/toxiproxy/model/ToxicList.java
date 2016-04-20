package eu.rekawek.toxiproxy.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.toxic.Bandwidth;

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

    /*
    public Latency addLatency(String name, ToxicDirection direction, long latency) {
        return add(new Latency(httpClient, getToxicPath(name), name, direction, latency));
    }

    public Slicer addSlicer(String name, ToxicDirection direction, long averageSize, long delay) {
        return add(new Slicer(httpClient, getToxicPath(name), name, direction, averageSize, delay));
    }

    public SlowClose addSlowClose(String name, ToxicDirection direction, long delay) {
        return add(new SlowClose(httpClient, getToxicPath(name), name, direction, delay));
    }

    public Timeout addTimeout(String name, ToxicDirection direction, long timeout) {
        return add(new Timeout(httpClient, getToxicPath(name), name, direction, timeout));
    }*/

    private String getToxicPath(String name) {
        return path + "/" + name;
    }
}
