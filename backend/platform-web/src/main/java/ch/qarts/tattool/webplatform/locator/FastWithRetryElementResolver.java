package ch.qarts.tattool.webplatform.locator;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;

import java.time.Duration;

import static ch.qarts.tattool.webplatform.platform.WebPlatform.IMPLICIT_WAIT_IN_SECONDS;


@Slf4j
@AllArgsConstructor
public class FastWithRetryElementResolver implements ElementResolver {

    private final int nbOfRetries;
    private final long implicitTimeoutInSeconds;
    private final SimpleLogger sessionLogger;

    @Override
    public WebElement resolve(ElementSuperLocator elementSuperLocator, WebPlatformSession session) {
        int retried = 0;
        while (retried < nbOfRetries) {
            try {
                session.getWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitTimeoutInSeconds));
                return new ElementIdElementResolver(sessionLogger).resolve(elementSuperLocator, session);
            } catch (Exception e) {
                sessionLogger.warning(this.getClass(), "Resolve failed, retrying", e);
                retried++;
                if (retried == nbOfRetries) {
                    sessionLogger.warning(this.getClass(), "Resolve failed, reached max nb of retries", e);
                    throw e;
                }
            } finally {
                session.getWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_IN_SECONDS));
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public ElementResolver fallBack() {
        throw new IllegalStateException("No fallback available");
    }
}
