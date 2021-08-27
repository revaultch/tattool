package ch.qarts.tattool.webplatform.locator;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.domain.session.DefaultExecutionLogger;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@Slf4j
public class QartsUUIDElementResolver implements ElementResolver {

    @Override
    public WebElement resolve(ElementSuperLocator elementSuperLocator, WebPlatformSession session) {
        try {
            String query = String.format("//*[@qarts-uuid = '%s']", elementSuperLocator.getId());
            if (!Strings.isNullOrEmpty(query)) {
                List<WebElement> elements = session.getWebDriver().findElements(By.xpath(query));// new Shadow(session.getWebDriver()).findElementsByXPath(query);//session.getWebDriver().findElements(By.xpath(query));
                if (elements.size() == 1) {
                    return elements.get(0);
                } else {
                    throw new IllegalStateException(String.format("Failed by qarts-uuid nb of results : %d", elements.size()));
                }
            } else {
                throw new IllegalStateException(String.format("Cannot query with qarts-uuid : %s", query));
            }
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            return fallBack().resolve(elementSuperLocator, session);
        }
    }

    @Override
    public ElementResolver fallBack() {
        return new EndOfResolverElementResolver(new DefaultExecutionLogger());
    }
}
