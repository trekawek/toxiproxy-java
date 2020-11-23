package eu.rekawek.toxiproxy;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonObject;

import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.ToxicList;
import eu.rekawek.toxiproxy.model.toxic.Bandwidth;

public class Proxy {

    private final HttpClient httpClient;

    private final String path;

    private String name;

    private String listen;

    private String upstream;

    private boolean enabled;

    private ToxicList toxicList;

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
        toxicList = new ToxicList(httpClient, path + "/toxics");
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

    public ToxicList toxics() {
        return toxicList;
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

    void reset() throws IOException {
        setFromJson(httpClient.get(path));
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
                + ": name=" + this.getName()
                + ", enabled=" + this.isEnabled();
    }
}
