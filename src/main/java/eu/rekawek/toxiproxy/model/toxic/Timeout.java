package eu.rekawek.toxiproxy.model.toxic;

import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.Toxic;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import eu.rekawek.toxiproxy.model.ToxicType;

import java.io.IOException;

public class Timeout extends Toxic {

    private long timeout;

    public Timeout(HttpClient httpClient, String toxicListPath, String name, ToxicDirection stream, long timeout) throws IOException {
        super(httpClient, toxicListPath, name, stream);
        this.timeout = timeout;
        createToxic(toxicListPath);
    }

    public Timeout(HttpClient httpClient, String path, JsonObject json) {
        super(httpClient, path, json);
    }

    @Override
    protected void setAttributes(JsonObject attributes) {
        timeout = attributes.get("timeout").getAsLong();
    }

    @Override
    protected JsonObject getAttributes() {
        JsonObject attr = new JsonObject();
        attr.addProperty("timeout", timeout);
        return attr;
    }

    @Override
    protected ToxicType getType() {
        return ToxicType.TIMEOUT;
    }

    public long getTimeout() {
        return timeout;
    }

    public Timeout setTimeout(long timeout) throws IOException {
        postAttribute("timeout", timeout);
        return this;
    }
}