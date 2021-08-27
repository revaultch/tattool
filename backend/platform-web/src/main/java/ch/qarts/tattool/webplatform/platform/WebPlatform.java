package ch.qarts.tattool.webplatform.platform;

import ch.qarts.tattool.core.domain.command.PayloadFactory;
import ch.qarts.tattool.core.domain.command.descriptor.CommandDescriptor;
import ch.qarts.tattool.core.domain.platform.PlatformProxy;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.domain.session.SessionNotFoundException;
import ch.qarts.tattool.core.domain.session.SessionProxy;
import ch.qarts.tattool.core.util.RetryUtils;
import ch.qarts.tattool.webplatform.commandfactory.CommandDescriptorMapper;
import ch.qarts.tattool.webplatform.commandfactory.CommandListHolder;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Slf4j
public class WebPlatform implements PlatformProxy {
    public final static int NB_OF_RETRIES = 3;
    public final static long FAST_IMPLICIT_WAIT_IN_SECONDS = 1;
    public final static long IMPLICIT_WAIT_IN_SECONDS = 5;
    public final static long SCRIPT_TIMEOUT_IN_SECONDS = 5;
    public final static long PAGE_LOAD_TIMEOUT_IN_SECONDS = 30;

    private final List<SessionProxy> sessionList = new ArrayList<>();

    private final PayloadFactory payloadFactory = new WebPlatformPayloadFactory();

    private WebPlatformConfiguration webPlatformConfiguration;

    public WebPlatform() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        Yaml yaml = new Yaml(new Constructor(WebPlatformConfiguration.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("platform-web.yaml");
        this.webPlatformConfiguration = yaml.load(inputStream);
    }


    @Override
    public String getId() {
        return "web-chrome-headless";
    }

    @Override
    public String getDescription() {
        return "Web chrome headless platform";
    }

    @Override
    public List<CommandDescriptor> getAvailableCommands() {
        return CommandListHolder.getAvailableCommands().stream().map(CommandDescriptorMapper::map).collect(Collectors.toList());
    }


    @SneakyThrows
    @Override
    public SessionProxy createSession(@NonNull SessionConfiguration sessionConfiguration) {
        WebPlatformSessionProvider sessionProvider = (WebPlatformSessionProvider) Class.forName(this.webPlatformConfiguration.getSessionProvider()).getConstructor().newInstance();
        var webPlatformSession = sessionProvider.provide(this.webPlatformConfiguration.getProviderConfig(), sessionConfiguration);
        this.sessionList.add(webPlatformSession);
        log.info(String.format("Created session with id %s ", webPlatformSession.getId()));
        log.info(String.format("Current session list is  %s ", sessionList.stream().map(SessionProxy::getId).collect(Collectors.joining(","))));
        return webPlatformSession;
    }


    @Override
    public SessionProxy getSession(String sessionId) {
        return this.sessionList.stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElseThrow(SessionNotFoundException::new);
    }

    @Override
    public PayloadFactory payloadFactory() {
        return this.payloadFactory;
    }

    @Override
    public void clean() {
        this.sessionList.forEach(SessionProxy::close);
    }

    @Override
    public void destroySession(String sessionId) {
        final SessionProxy session = this.getSession(sessionId);
        // trying to destroy underlying session "best effort" (async so no impact on run duration) - good idea  ? - not sure
        if (session != null) {
            CompletableFuture.runAsync(() -> {
                RetryUtils.retry(() -> {
                    session.close();
                    this.sessionList.remove(session);
                    return null;
                }, 5, 5000);
            });
        }
    }


}
