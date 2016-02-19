package eu.rekawek.toxiproxy.model.toxic;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.rekawek.toxiproxy.HttpClient;

public class Bandwidth {

    private final HttpClient httpClient;

    private final String path;

    private boolean enabled;

    private long rate;

    public Bandwidth(HttpClient httpClient, String path, JsonElement json) {
        this.httpClient = httpClient;
        this.path = path;
        if (json != null) {
            setFromJson(json.getAsJsonObject());
        }
    }

    private void setFromJson(JsonObject json) {
        enabled = json.get("enabled").getAsBoolean();
        rate = json.get("rate").getAsLong();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public long getRate() {
        return rate;
    }

    public Bandwidth enable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", true));
        return this;
    }

    public Bandwidth disable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", false));
        return this;
    }

    public Bandwidth setRate(long rate) throws IOException {
        setFromJson(httpClient.post(path, "rate", rate));
        return this;
    }
}