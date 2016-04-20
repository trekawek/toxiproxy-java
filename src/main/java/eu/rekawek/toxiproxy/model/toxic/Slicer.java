package eu.rekawek.toxiproxy.model.toxic;

import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;
import eu.rekawek.toxiproxy.model.Toxic;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import eu.rekawek.toxiproxy.model.ToxicType;

import java.io.IOException;

public class Slicer extends Toxic {

    private long averageSize;

    private long sizeVariation;

    private long delay;

    public Slicer(HttpClient httpClient, String toxicListPath, String name, ToxicDirection stream, long averageSize, long delay) throws IOException {
        super(httpClient, toxicListPath, name, stream);
        this.averageSize = averageSize;
        this.delay = delay;
        createToxic(toxicListPath);
    }

    public Slicer(HttpClient httpClient, String path, JsonObject json) {
        super(httpClient, path, json);
    }

    @Override
    protected void setAttributes(JsonObject attributes) {
        averageSize = attributes.get("average_size").getAsLong();
        sizeVariation = attributes.get("size_variation").getAsLong();
        delay = attributes.get("delay").getAsLong();
    }

    @Override
    protected JsonObject getAttributes() {
        JsonObject attr = new JsonObject();
        attr.addProperty("average_size", averageSize);
        attr.addProperty("size_variation", sizeVariation);
        attr.addProperty("delay", delay);
        return attr;
    }

    @Override
    protected ToxicType getType() {
        return ToxicType.SLICER;
    }

    public long getAverageSize() {
        return averageSize;
    }

    public long getSizeVariation() {
        return sizeVariation;
    }

    public long getDelay() {
        return delay;
    }

    public Slicer setAverageSize(long averageSize) throws IOException {
        postAttribute("average_size", averageSize);
        return this;
    }

    public Slicer setSizeVariation(long sizeVariation) throws IOException {
        postAttribute("size_variation", sizeVariation);
        return this;
    }

    public Slicer setDelay(long delay) throws IOException {
        postAttribute("delay", delay);
        return this;
    }
}