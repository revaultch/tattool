package ch.qarts.tattool.webplatform.locator;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.AllArgsConstructor;
import org.openqa.selenium.WebElement;

@AllArgsConstructor
public class EndOfResolverElementResolver implements ElementResolver {

    private final SimpleLogger sessionLogger;

    @Override
    public WebElement resolve(ElementSuperLocator elementSuperLocator, WebPlatformSession session) {
        String msg = String.format("Reached end of resolver chain for %s", elementSuperLocator);
        sessionLogger.error(this.getClass(), msg, null);
        throw new IllegalStateException(msg);
    }

    @Override
    public ElementResolver fallBack() {
        return null;
    }
}
