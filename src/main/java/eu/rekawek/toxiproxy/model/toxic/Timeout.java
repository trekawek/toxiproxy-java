package eu.rekawek.toxiproxy.model.toxic;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.rekawek.toxiproxy.HttpClient;

public class Timeout {

    private final HttpClient httpClient;

    private final String path;

    private boolean enabled;

    private long timeout;

    public Timeout(HttpClient httpClient, String path, JsonElement json) {
        this.httpClient = httpClient;
        this.path = path;
        if (json != null) {
            setFromJson(json.getAsJsonObject());
        }
    }

    private void setFromJson(JsonObject json) {
        enabled = json.get("enabled").getAsBoolean();
        timeout = json.get("timeout").getAsLong();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public long getTimeout() {
        return timeout;
    }

    public Timeout enable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", true));
        return this;
    }

    public Timeout disable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", false));
        return this;
    }

    public Timeout setTimeout(long timeout) throws IOException {
        setFromJson(httpClient.post(path, "timeout", timeout));
        return this;
    }
}