package eu.rekawek.toxiproxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import eu.rekawek.toxiproxy.model.ToxicDirection;
import eu.rekawek.toxiproxy.model.toxic.LimitData;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ToxiproxyClientTest {

    @ClassRule
    public static ToxiproxyRule toxiproxyRule = new ToxiproxyRule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ToxiproxyClient tp;

    @Before
    public void setup() {
        tp = toxiproxyRule.getToxiproxyClient();
    }

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

        assertNull(tp.getProxyOrNull("invalid-proxy"));

        expectedException.expect(IOException.class);
        tp.getProxy("invalid-proxy");
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

    @Test
    public void testReset() throws IOException {
        Proxy proxy = tp.createProxy("test-proxy", "127.0.0.1:26379", "localhost:6379");
        proxy.toxics().latency("latency-toxic-down", ToxicDirection.DOWNSTREAM, 10);
        proxy.toxics().latency("latency-toxic-up", ToxicDirection.UPSTREAM, 10);
        proxy.disable();

        tp.reset();

        assertTrue(proxy.toxics().getAll().isEmpty());
        assertTrue(proxy.isEnabled());
    }

    @Test
    public void testErrorCodeAndMessage() throws Exception {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("[409] proxy already exists");

        tp.createProxy("test-proxy", "127.0.0.1:26379", "localhost:6379");
        tp.createProxy("test-proxy", "127.0.0.1:26379", "localhost:6379");
    }


    @Test
    public void testToxicLimitData() throws IOException {
        Proxy proxy = tp.createProxy("test-proxy", "127.0.0.1:26379", "localhost:6379");

        proxy.toxics().limitData("limit-data-down", ToxicDirection.DOWNSTREAM, 10);
        proxy.toxics().limitData("limit-data-up", ToxicDirection.UPSTREAM, 10);

        proxy = tp.getProxy("test-proxy");

        LimitData toxicUp = (LimitData)proxy.toxics().get("limit-data-up");
        LimitData toxicDown = (LimitData)proxy.toxics().get("limit-data-down");

        assertEquals(toxicUp.getBytes(), 10);
        assertEquals(toxicUp.getStream(), ToxicDirection.UPSTREAM);

        assertEquals(toxicDown.getBytes(), 10);
        assertEquals(toxicDown.getStream(), ToxicDirection.DOWNSTREAM);
    }


}