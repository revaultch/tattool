package ch.qarts.tattool.webplatform;


import ch.qarts.tattool.core.domain.session.DefaultExecutionLogger;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.intg.Page;
import ch.qarts.tattool.webplatform.platform.WebPlatform;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;

public abstract class AbstractSessionTest {


    protected WebPlatform webPlatform;

    protected SimpleLogger simpleLogger = new DefaultExecutionLogger();


    @BeforeEach
    public void setup() {
        webPlatform = new WebPlatform();
    }

    @AfterEach
    public void tearDown() {
        webPlatform.clean();
    }


    @SneakyThrows
    protected WebPlatformSession buildSession(Path path) {
        SessionConfiguration sessionConfiguration = new SessionConfiguration();
        sessionConfiguration.setDebug(true);
        var session = (WebPlatformSession) webPlatform.createSession(sessionConfiguration);
        session.execute(webPlatform.payloadFactory().create(String.format("{\"verb\" : \"navigateTo\", \"url\" : \"%s\"}", path.toUri())), simpleLogger);
        return session;
    }


    @SneakyThrows
    protected WebPlatformSession buildSession(Page page) {
        SessionConfiguration sessionConfiguration = new SessionConfiguration();
        sessionConfiguration.setDebug(true);
        var session = (WebPlatformSession) webPlatform.createSession(sessionConfiguration);
        session.execute(webPlatform.payloadFactory().create(String.format("{\"verb\" : \"navigateTo\", \"url\" : \"%s\"}", page.generateAsDataUrl())), simpleLogger);
        return session;
    }


    @SneakyThrows
    protected WebPlatformSession buildSession(String bodyContent, String scriptContent) {
        Page page = Page.builder()
                .bodyContent(bodyContent)
                .scriptContent(scriptContent)
                .build();
        WebPlatformSession session = buildSession(page);
        session.forceAnalyzerSnapshot(simpleLogger);
        return session;
    }

}
