package eu.rekawek.toxiproxy.model.toxic;

import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.Toxic;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import eu.rekawek.toxiproxy.model.ToxicType;

import java.io.IOException;

public class ResetPeer extends Toxic {

    private static final String ATTRIBUTE = "timeout";
	private long timeout;

    public ResetPeer(HttpClient httpClient, String toxicListPath, String name, ToxicDirection stream, long timeout) throws IOException {
        super(httpClient, toxicListPath, name, stream);
        this.timeout = timeout;
        createToxic(toxicListPath);
    }

    public ResetPeer(HttpClient httpClient, String path, JsonObject json) {
        super(httpClient, path, json);
    }

    @Override
    protected void setAttributes(JsonObject attributes) {
        timeout = attributes.get(ATTRIBUTE).getAsLong();
    }

    @Override
    protected JsonObject getAttributes() {
        JsonObject attr = new JsonObject();
        attr.addProperty(ATTRIBUTE, timeout);
        return attr;
    }

    @Override
    protected ToxicType getType() {
        return ToxicType.RESET_PEER;
    }

    public long getTimeout() {
        return timeout;
    }

    public ResetPeer setTimeout(long timeout) throws IOException {
        postAttribute(ATTRIBUTE, timeout);
        return this;
    }
}