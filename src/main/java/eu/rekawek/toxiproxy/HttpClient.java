package eu.rekawek.toxiproxy;

import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

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

    public JsonObject post(String path, String name, float value) throws IOException {
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

    public String getPlain(String path) throws IOException {
        HttpURLConnection connection = getConnection(path);
        final int status = connection.getResponseCode();
        if (status < 200 || status > 299) {
            throw new IOException(readTextAndClose(connection.getErrorStream()));
        } else {
            return readTextAndClose(connection.getInputStream());
        }
    }

    public JsonObject get(String path) throws IOException {
        return get(path, JsonObject.class);
    }

    public <T> T get(String path, Class<T> clazz) throws IOException {
        HttpURLConnection connection = getConnection(path);
        return readResponse(connection, clazz);
    }

    public int post(String path) throws IOException {
        HttpURLConnection connection = getConnection(path);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.getInputStream().close();
        return connection.getResponseCode();
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

        return readResponse(connection, JsonObject.class);
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

    private static <T> T readResponse(HttpURLConnection connection, Class<T> clazz) throws IOException {
        final int status = connection.getResponseCode();
        if (status < 200 || status > 299) {
            JsonObject error = readAndClose(connection.getErrorStream(), JsonObject.class);
            String errorMsg = format("[%d] %s", error.get("status").getAsLong(), error.get("title").getAsString());
            throw new IOException(errorMsg);
        } else {
            return readAndClose(connection.getInputStream(), clazz);
        }
    }

    private static <T> T readAndClose(InputStream is, Class<T> clazz) throws IOException {
        try {
            return GSON.fromJson(new InputStreamReader(is), clazz);
        } finally {
            is.close();
        }
    }

    private static String readTextAndClose(InputStream is) throws IOException {
        char[] buffer = new char[1024];
        StringBuilder result = new StringBuilder();
        Reader r = new InputStreamReader(is);
        int len;
        while ((len = r.read(buffer)) > -1) {
            result.append(buffer, 0, len);
        }
        return result.toString();
    }
}
