package eu.rekawek.toxiproxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import eu.rekawek.toxiproxy.ToxiproxyClient;
import eu.rekawek.toxiproxy.model.Proxy;
import eu.rekawek.toxiproxy.model.toxic.Bandwidth;
import eu.rekawek.toxiproxy.model.toxic.Latency;
import eu.rekawek.toxiproxy.model.toxic.Slicer;
import eu.rekawek.toxiproxy.model.toxic.SlowClose;
import eu.rekawek.toxiproxy.model.toxic.Timeout;
import eu.rekawek.toxiproxy.model.toxic.Toxics;

@RunWith(Parameterized.class)
public class ToxicsTest {

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { "downstream" }, { "upstream" } });
    }

    private final ToxiproxyClient tp = new ToxiproxyClient();

    private final String stream;

    private Proxy proxy;

    private Toxics toxics;

    public ToxicsTest(String stream) {
        this.stream = stream;
    }

    @BeforeClass
    public static void toxiproxyEnabled() {
        ToxiproxyClientTest.toxiproxyEnabled();
    }

    @Before
    public void createProxy() throws IOException {
        proxy = tp.createProxy("test-proxy", "127.0.0.1:26379", "localhost:6379");
        if ("downstream".equals(stream)) {
            toxics = proxy.downstream();
        } else {
            toxics = proxy.upstream();
        }
    }

    @After
    public void cleanup() throws IOException {
        proxy.delete();
    }

    @Test
    public void testBandwidth() throws IOException {
        Bandwidth bandwidth = toxics.bandwidth();

        bandwidth.enable().setRate(100);
        assertTrue(bandwidth.isEnabled());
        assertEquals(100, bandwidth.getRate());

        bandwidth.disable();
        assertFalse(bandwidth.isEnabled());
    }

    @Test
    public void testLatency() throws IOException {
        Latency latency = toxics.latency();

        latency.enable().setLatency(100).setJitter(15);
        assertTrue(latency.isEnabled());
        assertEquals(100, latency.getLatency());
        assertEquals(15, latency.getJitter());

        latency.disable();
        assertFalse(latency.isEnabled());
    }

    @Test
    public void testSlicer() throws IOException {
        Slicer slicer = toxics.slicer();

        slicer.enable().setAverageSize(128).setSizeVariation(64).setDelay(100);
        assertTrue(slicer.isEnabled());
        assertEquals(128, slicer.getAverageSize());
        assertEquals(64, slicer.getSizeVariation());
        assertEquals(100, slicer.getDelay());

        slicer.disable();
        assertFalse(slicer.isEnabled());
    }

    @Test
    public void testSlowClose() throws IOException {
        SlowClose slowClose = toxics.slowClose();

        slowClose.enable().setDelay(100);
        assertTrue(slowClose.isEnabled());
        assertEquals(100, slowClose.getDelay());

        slowClose.disable();
        assertFalse(slowClose.isEnabled());
    }

    @Test
    public void testTimeout() throws IOException {
        Timeout timeout = toxics.timeout();

        timeout.enable().setTimeout(100);
        assertTrue(timeout.isEnabled());
        assertEquals(100, timeout.getTimeout());

        timeout.disable();
        assertFalse(timeout.isEnabled());
    }
}
