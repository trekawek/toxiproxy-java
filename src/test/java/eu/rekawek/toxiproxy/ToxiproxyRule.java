package eu.rekawek.toxiproxy;

import com.arakelian.docker.junit.DockerRule;
import com.arakelian.docker.junit.model.ImmutableDockerConfig;
import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import repackaged.com.arakelian.docker.junit.com.github.dockerjava.api.model.ExposedPort;
import repackaged.com.arakelian.docker.junit.com.github.dockerjava.api.model.PortBinding;
import repackaged.com.arakelian.docker.junit.com.github.dockerjava.core.DockerClientImpl;
import repackaged.com.arakelian.docker.junit.com.github.dockerjava.okhttp.OkHttpDockerCmdExecFactory;

public class ToxiproxyRule implements TestRule {

    private final DockerRule wrappedRule;

    public ToxiproxyRule() {
        wrappedRule = new DockerRule(ImmutableDockerConfig.builder()
                .image("ghcr.io/shopify/toxiproxy:2.3.0")
                .addStartedListener(container -> {
                    container.waitForPort(ExposedPort.tcp(8474));
                })
                .addHostConfigConfigurer(hostConfig -> {
                    hostConfig.withPortBindings(PortBinding.parse("8474"));
                })
                .build());
    }

    public ToxiproxyClient getToxiproxyClient() {
        int mappedPort = wrappedRule.getContainer().getSimpleBinding(ExposedPort.tcp(8474)).getPort();
        ToxiproxyClient tp = new ToxiproxyClient("localhost", mappedPort);
        return tp;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        try {
            DockerClientImpl.getInstance().withDockerCmdExecFactory(new OkHttpDockerCmdExecFactory()).pingCmd().exec();
        } catch (Exception e) {
            Assume.assumeNoException(e);
        }
        return wrappedRule.apply(statement, description);
    }
}
