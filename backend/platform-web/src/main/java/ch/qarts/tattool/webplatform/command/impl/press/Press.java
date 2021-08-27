package ch.qarts.tattool.webplatform.command.impl.press;

import ch.qarts.tattool.core.domain.command.ContextData;
import ch.qarts.tattool.core.domain.command.descriptor.Metadata;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.command.WebPlatformExecutableCommand;
import ch.qarts.tattool.webplatform.locator.FastWithRetryElementResolver;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static ch.qarts.tattool.webplatform.locator.Utils.completeIfNeeded;
import static ch.qarts.tattool.webplatform.locator.Utils.moveTo;
import static ch.qarts.tattool.webplatform.platform.WebPlatform.FAST_IMPLICIT_WAIT_IN_SECONDS;
import static ch.qarts.tattool.webplatform.platform.WebPlatform.NB_OF_RETRIES;

@EqualsAndHashCode(callSuper = true)
@Metadata(name = "press", description = "press key on element")
public class Press extends WebPlatformExecutableCommand<PressPayload> {


    @Override
    public boolean isEligible(WebPlatformSession session) {
        return session.getAnalyzerSnapshot().getWritable().size() > 0;
    }

    @Override
    public PressPayload execute(WebPlatformSession session, PressPayload pressPayload, SimpleLogger sessionLogger) {
        session.waitUntilComplete();
        pressPayload.setElementSuperLocator(completeIfNeeded(session, pressPayload.getElementSuperLocator()));
        WebElement webElement = moveTo(session.getWebDriver(), new FastWithRetryElementResolver(NB_OF_RETRIES, FAST_IMPLICIT_WAIT_IN_SECONDS, sessionLogger).resolve(pressPayload.getElementSuperLocator(), session));
        webElement.sendKeys(Keys.valueOf(pressPayload.getKey()).toString());
        session.waitUntilComplete();
        return pressPayload;
    }

    @Override
    public ContextData contextData(WebPlatformSession session) {
        ContextData contextData = new ContextData();
        contextData.put("elementSuperLocator", session.getAnalyzerSnapshot().getWritable());
        contextData.put("key", Keys.values());
        return contextData;
    }


}
