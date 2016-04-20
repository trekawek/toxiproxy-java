package eu.rekawek.toxiproxy.model.toxic;

import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.Toxic;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import eu.rekawek.toxiproxy.model.ToxicType;

import java.io.IOException;

public class SlowClose extends Toxic {

    private long delay;

    public SlowClose(HttpClient httpClient, String toxicListPath, String name, ToxicDirection stream, long delay) throws IOException {
        super(httpClient, toxicListPath, name, stream);
        this.delay = delay;
        createToxic(toxicListPath);
    }

    public SlowClose(HttpClient httpClient, String path, JsonObject json) {
        super(httpClient, path, json);
    }

    @Override
    protected void setAttributes(JsonObject attributes) {
        delay = attributes.get("delay").getAsLong();
    }

    @Override
    protected JsonObject getAttributes() {
        JsonObject attr = new JsonObject();
        attr.addProperty("delay", delay);
        return attr;
    }

    @Override
    protected ToxicType getType() {
        return ToxicType.SLOW_CLOSE;
    }

    public long getDelay() {
        return delay;
    }

    public SlowClose setDelay(long delay) throws IOException {
        postAttribute("delay", delay);
        return this;
    }
}