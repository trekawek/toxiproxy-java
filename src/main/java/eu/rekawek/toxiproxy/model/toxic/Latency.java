package eu.rekawek.toxiproxy.model.toxic;

import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.Toxic;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import eu.rekawek.toxiproxy.model.ToxicType;

import java.io.IOException;

public class Latency extends Toxic {

    private long latency;

    private long jitter;

    public Latency(HttpClient httpClient, String toxicListPath, String name, ToxicDirection stream, long latency) throws IOException {
        super(httpClient, toxicListPath, name, stream);
        this.latency = latency;
        createToxic(toxicListPath);
    }

    public Latency(HttpClient httpClient, String path, JsonObject json) {
        super(httpClient, path, json);
    }

    @Override
    protected void setAttributes(JsonObject attributes) {
        latency = attributes.get("latency").getAsLong();
        jitter = attributes.get("jitter").getAsLong();
    }

    @Override
    protected JsonObject getAttributes() {
        JsonObject attr = new JsonObject();
        attr.addProperty("latency", latency);
        attr.addProperty("jitter", jitter);
        return attr;
    }

    @Override
    protected ToxicType getType() {
        return ToxicType.LATENCY;
    }

    public long getLatency() {
        return latency;
    }

    public long getJitter() {
        return jitter;
    }

    public Latency setLatency(long latency) throws IOException {
        postAttribute("latency", latency);
        return this;
    }

    public Latency setJitter(long jitter) throws IOException {
        postAttribute("jitter", jitter);
        return this;
    }
}