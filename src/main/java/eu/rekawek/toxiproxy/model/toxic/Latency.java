package eu.rekawek.toxiproxy.model.toxic;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.rekawek.toxiproxy.HttpClient;

public class Latency {

    private final HttpClient httpClient;

    private final String path;

    private boolean enabled;

    private long latency;

    private long jitter;

    public Latency(HttpClient httpClient, String path, JsonElement json) {
        this.httpClient = httpClient;
        this.path = path;
        if (json != null) {
            setFromJson(json.getAsJsonObject());
        }
    }

    private void setFromJson(JsonObject json) {
        enabled = json.get("enabled").getAsBoolean();
        latency = json.get("latency").getAsLong();
        jitter = json.get("jitter").getAsLong();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public long getLatency() {
        return latency;
    }

    public long getJitter() {
        return jitter;
    }

    public Latency enable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", true));
        return this;
    }

    public Latency disable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", false));
        return this;
    }

    public Latency setLatency(long latency) throws IOException {
        setFromJson(httpClient.post(path, "latency", latency));
        return this;
    }

    public Latency setJitter(long jitter) throws IOException {
        setFromJson(httpClient.post(path, "jitter", jitter));
        return this;
    }
}