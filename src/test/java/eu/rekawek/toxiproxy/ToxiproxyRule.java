package eu.rekawek.toxiproxy;

import org.junit.rules.ExternalResource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

public class ToxiproxyRule extends ExternalResource {

    private static final int PORT = 8474;

    private final GenericContainer<?> container;

    public ToxiproxyRule() {
        container = new GenericContainer<>("ghcr.io/shopify/toxiproxy:2.8.0")
                .waitingFor(new HostPortWaitStrategy().forPorts(PORT))
                .withExposedPorts(PORT);
    }

    protected void before() {
        container.start();
    }

    protected void after() {
        container.stop();
    }

    public ToxiproxyClient getToxiproxyClient() {
        return new ToxiproxyClient(container.getHost(), container.getMappedPort(PORT));
    }
}
