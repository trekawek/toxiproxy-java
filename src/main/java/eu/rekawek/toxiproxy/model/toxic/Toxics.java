package eu.rekawek.toxiproxy.model.toxic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.rekawek.toxiproxy.HttpClient;

public class Toxics {

    private final HttpClient httpClient;

    private final String path;

    private Latency latency;

    private Bandwidth bandwidth;

    private SlowClose slowClose;

    private Timeout timeout;

    private Slicer slicer;

    public Toxics(HttpClient httpClient, String path, JsonElement json) {
        this.httpClient = httpClient;
        this.path = path;
        if (json != null) {
            setFromJson(json.getAsJsonObject());
        }
    }

    private void setFromJson(JsonObject json) {
        latency = new Latency(httpClient, path + "/latency", json.get("latency"));
        bandwidth = new Bandwidth(httpClient, path + "/bandwidth", json.get("bandwidth"));
        slowClose = new SlowClose(httpClient, path + "/slow_close", json.get("slow_close"));
        timeout = new Timeout(httpClient, path + "/timeout", json.get("timeout"));
        slicer = new Slicer(httpClient, path + "/slicer", json.get("slicer"));
    }

    public Latency latency() {
        return latency;
    }

    public Bandwidth bandwidth() {
        return bandwidth;
    }

    public SlowClose slowClose() {
        return slowClose;
    }

    public Timeout timeout() {
        return timeout;
    }

    public Slicer slicer() {
        return slicer;
    }
}
