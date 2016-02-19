package eu.rekawek.toxiproxy.model;

import java.io.IOException;

import com.google.gson.JsonObject;

import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.toxic.Toxics;

public class Proxy {

    private final HttpClient httpClient;

    private final String path;

    private String name;

    private String listen;

    private String upstream;

    private boolean enabled;

    private Toxics upstreamToxics;

    private Toxics downstreamToxics;

    public Proxy(HttpClient httpClient, String path, JsonObject json) {
        this.httpClient = httpClient;
        this.path = path;
        setFromJson(json);
    }

    private void setFromJson(JsonObject json) {
        name = json.get("name").getAsString();
        listen = json.get("listen").getAsString();
        upstream = json.get("upstream").getAsString();
        enabled = json.get("enabled").getAsBoolean();
        upstreamToxics = new Toxics(httpClient, path + "/upstream/toxics", json.get("upstream_toxics"));
        downstreamToxics = new Toxics(httpClient, path + "/downstream/toxics", json.get("downstream_toxics"));
    }

    public String getName() {
        return name;
    }

    public String getListen() {
        return listen;
    }

    public String getUpstream() {
        return upstream;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Toxics upstream() {
        return upstreamToxics;
    }

    public Toxics downstream() {
        return downstreamToxics;
    }

    public void setListen(String listen) throws IOException {
        setFromJson(httpClient.post(path, "listen", listen));
    }

    public void setUpstream(String upstream) throws IOException {
        setFromJson(httpClient.post(path, "upstream", upstream));
    }

    public void enable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", true));
    }

    public void disable() throws IOException {
        setFromJson(httpClient.post(path, "enabled", false));
    }
    
    public void delete() throws IOException {
        httpClient.delete(path);
    }
}
