package eu.rekawek.toxiproxy;

import com.arakelian.docker.junit.DockerRule;
import com.arakelian.docker.junit.model.ImmutableDockerConfig;
import com.spotify.docker.client.DefaultDockerClient;
import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ToxiproxyRule implements TestRule {

    private final DockerRule wrappedRule;

    public ToxiproxyRule() {
        wrappedRule = new DockerRule(ImmutableDockerConfig.builder()
                .image("shopify/toxiproxy:2.1.3")
                .name("text-toxiproxy")
                .ports("8474")
                .addStartedListener(container -> {
                    container.waitForPort("8474/tcp");
                })
                .alwaysRemoveContainer(true)
                .build());

    }

    public ToxiproxyClient getToxiproxyClient() {
        int mappedPort = wrappedRule.getContainer().getPortBinding("8474/tcp").getPort();
        ToxiproxyClient tp = new ToxiproxyClient("localhost", mappedPort);
        return tp;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        try {
            DefaultDockerClient client = DefaultDockerClient.fromEnv().connectTimeoutMillis(5000L).readTimeoutMillis(20000L).build();
            client.ping();
            client.close();
        } catch (Exception e) {
            Assume.assumeNoException(e);
        }

        return wrappedRule.apply(statement, description);
    }
}
