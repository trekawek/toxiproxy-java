package eu.rekawek.toxiproxy;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class HttpClient {

    private static final Gson GSON = new Gson();

    private final String host;

    private final int port;

    HttpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public JsonObject post(String path, String name, long value) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty(name, value);
        return post(path, data);
    }

    public JsonObject post(String path, String name, boolean value) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty(name, value);
        return post(path, data);
    }

    public JsonObject post(String path, String name, String value) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty(name, value);
        return post(path, data);
    }

    public JsonObject get(String path) throws IOException {
        HttpURLConnection connection = getConnection(path);
        return readResponse(connection);
    }

    public JsonObject post(String path, JsonObject data) throws IOException {
        HttpURLConnection connection = getConnection(path);
        connection.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        try {
            GSON.toJson(data, writer);
        } finally {
            writer.close();
        }

        return readResponse(connection);
    }

    public int delete(String path) throws IOException {
        HttpURLConnection connection = getConnection(path);
        connection.setDoOutput(true);
        connection.setRequestMethod("DELETE");
        connection.getInputStream().close();
        return connection.getResponseCode();
    }

    private HttpURLConnection getConnection(String path) throws IOException {
        final URL url = new URL(String.format("http://%s:%d%s", host, port, path));
        return (HttpURLConnection) url.openConnection();
    }

    private static JsonObject readResponse(HttpURLConnection connection) throws IOException {
        final int status = connection.getResponseCode();
        if (status < 200 || status > 299) {
            JsonObject error = readAndClose(connection.getErrorStream());
            String errorMsg = format("[%d] %s", error.get("status").getAsLong(), error.get("title").getAsString());
            throw new IOException(errorMsg);
        } else {
            return readAndClose(connection.getInputStream());
        }
    }

    private static JsonObject readAndClose(InputStream is) throws IOException {
        JsonObject json = GSON.fromJson(new InputStreamReader(is), JsonObject.class);
        is.close();
        return json;
    }
}
