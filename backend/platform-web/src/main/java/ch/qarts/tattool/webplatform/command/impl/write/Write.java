package ch.qarts.tattool.webplatform.command.impl.write;

import ch.qarts.tattool.core.domain.command.ContextData;
import ch.qarts.tattool.core.domain.command.descriptor.Metadata;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.command.WebPlatformExecutableCommand;
import ch.qarts.tattool.webplatform.locator.FastWithRetryElementResolver;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.openqa.selenium.WebElement;

import static ch.qarts.tattool.webplatform.locator.Utils.completeIfNeeded;
import static ch.qarts.tattool.webplatform.locator.Utils.moveTo;
import static ch.qarts.tattool.webplatform.platform.WebPlatform.FAST_IMPLICIT_WAIT_IN_SECONDS;
import static ch.qarts.tattool.webplatform.platform.WebPlatform.NB_OF_RETRIES;


@EqualsAndHashCode(callSuper = true)
@Metadata(name = "write", description = "writes text into element")
public class Write extends WebPlatformExecutableCommand<WritePayload> {

    @Override
    public boolean isEligible(WebPlatformSession session) {
        return session.getAnalyzerSnapshot().getWritable().size() > 0;
    }

    @Override
    @SneakyThrows
    public WritePayload execute(WebPlatformSession session, WritePayload payload, SimpleLogger sessionLogger) {
        session.waitUntilComplete();
        payload.setElementSuperLocator(completeIfNeeded(session, payload.getElementSuperLocator()));
        WebElement webElement = moveTo(session.getWebDriver(), new FastWithRetryElementResolver(NB_OF_RETRIES, FAST_IMPLICIT_WAIT_IN_SECONDS, sessionLogger).resolve(payload.getElementSuperLocator(), session));
        webElement.clear();
        webElement.sendKeys(payload.getText());
        return payload;
    }

    @Override
    public ContextData contextData(WebPlatformSession session) {
        ContextData contextData = new ContextData();
        contextData.put("elementSuperLocator", session.getAnalyzerSnapshot().getWritable());
        return contextData;
    }


}
