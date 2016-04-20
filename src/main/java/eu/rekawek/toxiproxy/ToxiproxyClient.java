package eu.rekawek.toxiproxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.rekawek.toxiproxy.model.Proxy;

public class ToxiproxyClient {

    private final HttpClient httpClient;

    public ToxiproxyClient() {
        this("localhost", 8474);
    }

    public ToxiproxyClient(String host, int port) {
        httpClient = new HttpClient(host, port);
    }

    public List<Proxy> getProxies() throws IOException {
        List<Proxy> proxies = new ArrayList<Proxy>();
        JsonObject object = httpClient.get("/proxies");
        for (Entry<String, JsonElement> e : object.entrySet()) {
            proxies.add(new Proxy(httpClient, "/proxies/" + e.getKey(), e.getValue().getAsJsonObject()));
        }
        return proxies;
    }

    public Proxy createProxy(String name, String listen, String upstream) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("listen", listen);
        json.addProperty("upstream", upstream);

        JsonObject result = httpClient.post("/proxies", json);
        return new Proxy(httpClient, "/proxies/" + name, result);
    }

    public Proxy getProxy(String name) throws IOException {
        JsonObject result = httpClient.get("/proxies/" + name);
        return new Proxy(httpClient, "/proxies/" + name, result);
    }

    public void reset() throws IOException {
        httpClient.get("/reset");
    }

    public String version() throws IOException {
        return httpClient.getPlain("/version").trim();
    }
}
