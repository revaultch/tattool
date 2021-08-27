package ch.qarts.tattool.webplatform.command.impl.click;

import ch.qarts.tattool.core.domain.command.ContextData;
import ch.qarts.tattool.core.domain.command.descriptor.Metadata;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.command.WebPlatformExecutableCommand;
import ch.qarts.tattool.webplatform.locator.FastWithRetryElementResolver;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static ch.qarts.tattool.webplatform.locator.Utils.completeIfNeeded;
import static ch.qarts.tattool.webplatform.locator.Utils.moveTo;
import static ch.qarts.tattool.webplatform.platform.WebPlatform.FAST_IMPLICIT_WAIT_IN_SECONDS;
import static ch.qarts.tattool.webplatform.platform.WebPlatform.NB_OF_RETRIES;

@EqualsAndHashCode(callSuper = true)
@Metadata(name = "click", description = "clicks element")
public class Click extends WebPlatformExecutableCommand<ClickPayload> {


    @Override
    public boolean isEligible(WebPlatformSession session) {
        return session.getAnalyzerSnapshot().getClickable().size() > 0;
    }

    @Override
    public ClickPayload execute(WebPlatformSession session, ClickPayload clickPayload, SimpleLogger sessionLogger) {
        session.waitUntilComplete();
        clickPayload.setElementSuperLocator(completeIfNeeded(session, clickPayload.getElementSuperLocator()));
        WebElement webElement = moveTo(session.getWebDriver(), new FastWithRetryElementResolver(NB_OF_RETRIES, FAST_IMPLICIT_WAIT_IN_SECONDS, sessionLogger).resolve(clickPayload.getElementSuperLocator(), session));
        Actions builder = new Actions(session.getWebDriver());
        builder.click(webElement).perform();
        return clickPayload;
    }

    @Override
    public ContextData contextData(WebPlatformSession session) {
        ContextData contextData = new ContextData();
        contextData.put("elementSuperLocator", session.getAnalyzerSnapshot().getClickable());
        return contextData;
    }


}
