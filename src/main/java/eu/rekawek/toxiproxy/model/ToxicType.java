package eu.rekawek.toxiproxy.model;

import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.toxic.Bandwidth;
import eu.rekawek.toxiproxy.model.toxic.Latency;
import eu.rekawek.toxiproxy.model.toxic.Slicer;
import eu.rekawek.toxiproxy.model.toxic.SlowClose;
import eu.rekawek.toxiproxy.model.toxic.Timeout;
import eu.rekawek.toxiproxy.model.toxic.LimitData;

public enum ToxicType {

    LATENCY {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return new Latency(httpClient, path, json);
        }
    }, BANDWIDTH {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return new Bandwidth(httpClient, path, json);
        }
    }, SLOW_CLOSE {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return new SlowClose(httpClient, path, json);
        }
    }, TIMEOUT {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return new Timeout(httpClient, path, json);
        }
    }, SLICER {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return new Slicer(httpClient, path, json);
        }
    }, LIMIT_DATA {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return new LimitData(httpClient, path, json);
        }
    };

    protected abstract Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json);

    public static Toxic createToxic(HttpClient httpClient, String path, JsonObject json) {
        String type = json.get("type").getAsString();
        return ToxicType.valueOf(type.toUpperCase()).doCreateToxic(httpClient, path, json);
    }
}
