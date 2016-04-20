package eu.rekawek.toxiproxy.model;

import com.google.gson.JsonObject;
import eu.rekawek.toxiproxy.HttpClient;

import java.io.IOException;

public abstract class Toxic {

    private HttpClient httpClient;

    private String path;

    private String name;

    private ToxicDirection stream;

    private float toxicity;

    public Toxic(HttpClient httpClient, String toxicListPath, String name, ToxicDirection stream) {
        this.httpClient = httpClient;
        this.path = toxicListPath + "/" + name;
        this.name = name;
        this.stream = stream;
        this.toxicity = 1f;
    }

    public Toxic(HttpClient httpClient, String path, JsonObject json) {
        this.httpClient = httpClient;
        this.path = path;
        setFromJson(json);
    }

    protected abstract void setAttributes(JsonObject attributes);

    protected abstract JsonObject getAttributes();

    protected abstract ToxicType getType();

    protected void createToxic(String toxicListPath) throws IOException {
        JsonObject toxicJson = new JsonObject();
        toxicJson.addProperty("name", name);
        toxicJson.addProperty("stream", stream.name().toLowerCase());
        toxicJson.addProperty("toxicity", toxicity);
        toxicJson.addProperty("type", getType().name().toLowerCase());
        toxicJson.add("attributes", getAttributes());
        setFromJson(httpClient.post(toxicListPath, toxicJson));
    }

    protected void postAttribute(String name, long value) throws IOException {
        JsonObject toxicJson = new JsonObject();
        JsonObject attr = new JsonObject();
        attr.addProperty(name, value);
        toxicJson.add("attributes", attr);
        setFromJson(httpClient.post(path, toxicJson));
    }

    private void setFromJson(JsonObject json) {
        name = json.get("name").getAsString();
        stream = ToxicDirection.valueOf(json.get("stream").getAsString().toUpperCase());
        toxicity = json.get("toxicity").getAsFloat();
        setAttributes(json.getAsJsonObject("attributes"));
    }

    public void setToxicity(float toxicity) throws IOException {
        setFromJson(httpClient.post(path, "toxicity", toxicity));
    }

    public String getName() {
        return name;
    }

    public ToxicDirection getStream() {
        return stream;
    }

    public float getToxicity() {
        return toxicity;
    }

    public void remove() throws IOException {
        httpClient.delete(path);
    }
}
