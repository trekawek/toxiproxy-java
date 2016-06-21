package eu.rekawek.toxiproxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static java.util.Collections.newSetFromMap;
import static java.util.Collections.synchronizedSet;

public class ToxiproxyClient {

    private final HttpClient httpClient;

    private final Set<Proxy> proxies = synchronizedSet(newSetFromMap(new WeakHashMap<Proxy, Boolean>()));

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
        return newProxyInstance(name, result);
    }

    public Proxy getProxy(String name) throws IOException {
        JsonObject result = httpClient.get("/proxies/" + name);
        return newProxyInstance(name, result);
    }

    private Proxy newProxyInstance(String name, JsonObject json) {
        Proxy p = new Proxy(httpClient, "/proxies/" + name, json);
        proxies.add(p);
        return p;
    }

    public void reset() throws IOException {
        httpClient.post("/reset");
        for (Proxy p : proxies) {
            p.reset();
        }
    }

    public String version() throws IOException {
        return httpClient.getPlain("/version").trim();
    }
}
