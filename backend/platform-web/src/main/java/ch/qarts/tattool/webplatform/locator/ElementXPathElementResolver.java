package ch.qarts.tattool.webplatform.locator;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class ElementXPathElementResolver implements ElementResolver {
    public static final String TYPE = "xpath";
    private final SimpleLogger sessionLogger;

    @Override
    public WebElement resolve(ElementSuperLocator elementSuperLocator, WebPlatformSession session) {
        try {
            String query = elementSuperLocator.getElementLocatorByType(TYPE).orElseThrow(ElementLocatorNotFoundException::new).getQuery();
            sessionLogger.info(this.getClass(), String.format("Finding by xpath %s", query));
            List<WebElement> webElementList = session.getWebDriver().findElements(By.xpath(query));
            if (webElementList.size() == 1) {
                return webElementList.get(0);
            } else {
                final String failMsg = String.format("Failed finding by xpath %s with element size = %d", query, webElementList.size());
                sessionLogger.warning(this.getClass(), failMsg, null);
                throw new IllegalStateException(failMsg);
            }

        } catch (Throwable t) {
            sessionLogger.error(this.getClass(), "Failed with xpath", t);
            return fallBack().resolve(elementSuperLocator, session);
        }
    }

    @Override
    public ElementResolver fallBack() {
        return new EndOfResolverElementResolver(sessionLogger);
    }

}
