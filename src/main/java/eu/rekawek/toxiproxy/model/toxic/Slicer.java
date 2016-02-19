package eu.rekawek.toxiproxy.model.toxic;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.rekawek.toxiproxy.HttpClient;

public class Slicer {

    private final HttpClient httpClient;

    private final String path;

    private boolean enabled;

    private long averageSize;

    private long sizeVariation;

    private long delay;

    public Slicer(HttpClient httpClient, String path, JsonElement json) {
        this.httpClient = httpClient;
        this.path = path;
        if (json != null) {
            setFromJson(json.getAsJsonObject());
        }
    }

    private void setFromJson(JsonObject json) {
        enabled = json.get("enabled").getAsBoolean();
        averageSize = json.get("average_size").getAsLong();
        sizeVariation = json.get("size_variation").getAsLong();
        delay = json.get("delay").getAsLong();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public long getAverageSize() {
        return averageSize;
    }

    public long getSizeVariation() {
        return sizeVariation;
    }

    public long getDelay() {
        return delay;
    }

    public Slicer enable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", true));
        return this;
    }

    public Slicer disable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", false));
        return this;
    }

    public Slicer setAverageSize(long averageSize) throws IOException {
        setFromJson(httpClient.post(path, "average_size", averageSize));
        return this;
    }

    public Slicer setSizeVariation(long sizeVariation) throws IOException {
        setFromJson(httpClient.post(path, "size_variation", sizeVariation));
        return this;
    }

    public Slicer setDelay(long delay) throws IOException {
        setFromJson(httpClient.post(path, "delay", delay));
        return this;
    }
}