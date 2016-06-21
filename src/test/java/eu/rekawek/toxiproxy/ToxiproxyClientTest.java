package eu.rekawek.toxiproxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.rekawek.toxiproxy.ToxiproxyClient;
import eu.rekawek.toxiproxy.model.Proxy;

public class ToxiproxyClientTest {

    private final ToxiproxyClient tp = new ToxiproxyClient();

    @BeforeClass
    public static void toxiproxyEnabled() {
        try {
            new ToxiproxyClient().getProxies();
        } catch (IOException e) {
            Assume.assumeNoException(e);
        }
    }

    @Before
    @After
    public void cleanup() throws IOException {
        for (Proxy proxy : tp.getProxies()) {
            if (proxy.getName().startsWith("test-")) {
                proxy.delete();
            }
        }
    }

    @Test
    public void testCreateProxy() throws IOException {
        Proxy proxy = tp.createProxy("test-proxy", "127.0.0.1:26379", "localhost:6379");
        assertTrue(proxy.isEnabled());
        assertEquals("test-proxy", proxy.getName());
        assertEquals("127.0.0.1:26379", proxy.getListen());
        assertEquals("localhost:6379", proxy.getUpstream());
    }

    @Test
    public void testGetProxy() throws IOException {
        tp.createProxy("test-proxy", "127.0.0.1:26379", "localhost:6379");
        Proxy proxy = tp.getProxy("test-proxy");
        assertTrue(proxy.isEnabled());
        assertEquals("test-proxy", proxy.getName());
        assertEquals("127.0.0.1:26379", proxy.getListen());
        assertEquals("localhost:6379", proxy.getUpstream());
    }

    @Test
    public void testListProxy() throws IOException {
        for (int i = 0; i < 3; i++) {
            tp.createProxy("test-proxy" + i, "127.0.0.1:" + (i + 26379), "localhost:" + (i + 6379));
        }
        List<Proxy> proxies = tp.getProxies();
        for (int i = 0; i < 3; i++) {
            Proxy proxy = proxies.get(i);
            assertTrue(proxy.isEnabled());
            assertEquals("test-proxy" + i, proxy.getName());
            assertEquals("127.0.0.1:" + (i + 26379), proxy.getListen());
            assertEquals("localhost:" + (i + 6379), proxy.getUpstream());
        }
    }

    @Test
    public void testDeleteProxy() throws IOException {
        Proxy proxy = tp.createProxy("test-proxy", "127.0.0.1:26379", "localhost:6379");
        assertFalse(tp.getProxies().isEmpty());
        proxy.delete();
        assertTrue(tp.getProxies().isEmpty());
    }

    @Test
    public void testEnableDisableProxy() throws IOException {
        Proxy proxy = tp.createProxy("test-proxy", "127.0.0.1:26379", "localhost:6379");
        assertTrue(proxy.isEnabled());
        proxy.disable();
        assertFalse(proxy.isEnabled());
        proxy.enable();
        assertTrue(proxy.isEnabled());
    }

    @Test
    public void testVersion() throws IOException {
        assertFalse(tp.version().isEmpty());
    }
}