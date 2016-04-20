package eu.rekawek.toxiproxy.model.toxic;

import java.io.IOException;

import com.google.gson.JsonObject;

import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.Toxic;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import eu.rekawek.toxiproxy.model.ToxicType;

public class Bandwidth extends Toxic {

    private long rate;

    public Bandwidth(HttpClient httpClient, String toxicListPath, String name, ToxicDirection stream, long rate) throws IOException {
        super(httpClient, toxicListPath, name, stream);
        this.rate = rate;
        createToxic(toxicListPath);
    }

    public Bandwidth(HttpClient httpClient, String path, JsonObject json) {
        super(httpClient, path, json);
    }

    @Override
    protected void setAttributes(JsonObject attributes) {
        rate = attributes.get("rate").getAsLong();
    }

    @Override
    protected JsonObject getAttributes() {
        JsonObject attr = new JsonObject();
        attr.addProperty("rate", rate);
        return attr;
    }

    @Override
    protected ToxicType getType() {
        return ToxicType.BANDWIDTH;
    }

    public long getRate() {
        return rate;
    }

    public Bandwidth setRate(long rate) throws IOException {
        postAttribute("rate", rate);
        return this;
    }
}