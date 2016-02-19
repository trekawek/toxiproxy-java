package eu.rekawek.toxiproxy.model.toxic;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.rekawek.toxiproxy.HttpClient;

public class SlowClose {

    private final HttpClient httpClient;

    private final String path;

    private boolean enabled;

    private long delay;

    public SlowClose(HttpClient httpClient, String path, JsonElement json) {
        this.httpClient = httpClient;
        this.path = path;
        if (json != null) {
            setFromJson(json.getAsJsonObject());
        }
    }

    private void setFromJson(JsonObject json) {
        enabled = json.get("enabled").getAsBoolean();
        delay = json.get("delay").getAsLong();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public long getDelay() {
        return delay;
    }

    public SlowClose enable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", true));
        return this;
    }

    public SlowClose disable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", false));
        return this;
    }

    public SlowClose setDelay(long delay) throws IOException {
        setFromJson(httpClient.post(path, "delay", delay));
        return this;
    }
}