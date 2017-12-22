package eu.rekawek.toxiproxy.model.toxic;

import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.Toxic;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import eu.rekawek.toxiproxy.model.ToxicType;

import java.io.IOException;

public class LimitData extends Toxic {

    private long bytes;

    public LimitData(HttpClient httpClient, String toxicListPath, String name, ToxicDirection stream, long bytes) throws IOException {
        super(httpClient, toxicListPath, name, stream);
        this.bytes = bytes;
        createToxic(toxicListPath);
    }

    public LimitData(HttpClient httpClient, String path, JsonObject json) {
        super(httpClient, path, json);
    }

    @Override
    protected void setAttributes(JsonObject attributes) {
        bytes = attributes.get("bytes").getAsLong();
    }

    @Override
    protected JsonObject getAttributes() {
        JsonObject attr = new JsonObject();
        attr.addProperty("bytes", bytes);
        return attr;
    }

    @Override
    protected ToxicType getType() {
        return ToxicType.LIMIT_DATA;
    }

    public long getBytes() {
        return bytes;
    }

    public LimitData setBytes(long bytes) throws IOException {
        postAttribute("bytes", bytes);
        return this;
    }
}