package ch.qarts.tattool.webplatform.locator;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class ElementIdElementResolver implements ElementResolver {
    public static final String TYPE = "id";
    private final SimpleLogger sessionLogger;

    @Override
    public WebElement resolve(ElementSuperLocator elementSuperLocator, WebPlatformSession session) {
        try {
            String query = elementSuperLocator.getElementLocatorByType(TYPE).orElseThrow(ElementLocatorNotFoundException::new).getQuery();
            sessionLogger.info(this.getClass(), String.format("Finding element with id %s", query));
            if (!Strings.isNullOrEmpty(query)) {
                List<WebElement> elements = session.getWebDriver().findElements(By.id(query)); // new Shadow(session.getWebDriver()).findElementsByXPath(String.format("//*[@id='%s']", query)); //;
                if (elements.size() == 1) {
                    return elements.get(0);
                } else {
                    final String msg = String.format("Failed by id nb of results : %d", elements.size());
                    sessionLogger.warning(this.getClass(), msg, null);
                    throw new IllegalStateException(msg);
                }
            } else {
                final String msg = String.format("Cannot query with id : %s -> skipping", query);
                sessionLogger.warning(this.getClass(), msg, null);
                throw new IllegalStateException(msg);
            }
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            return fallBack().resolve(elementSuperLocator, session);
        }
    }

    @Override
    public ElementResolver fallBack() {
        return new ElementVisibleTextElementResolver(sessionLogger);
    }
}
