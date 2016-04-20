package eu.rekawek.toxiproxy.model;

import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.toxic.Bandwidth;

public enum ToxicType {

    LATENCY {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return null;
        }
    }, BANDWIDTH {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return new Bandwidth(httpClient, path, json);
        }
    }, SLOW_CLOSE {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return null;
        }
    }, TIMEOUT {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return null;
        }
    }, SLICER {
        protected Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json) {
            return null;
        }
    };

    protected abstract Toxic doCreateToxic(HttpClient httpClient, String path, JsonObject json);

    public static Toxic createToxic(HttpClient httpClient, String path, JsonObject json) {
        String type = json.get("type").getAsString();
        return ToxicType.valueOf(type.toUpperCase()).doCreateToxic(httpClient, path, json);
    }
}
