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
public class ElementVisibleTextElementResolver implements ElementResolver {
    public static final String TYPE = "text";

    private final SimpleLogger sessionLogger;

    private String buildQuery(String tagName, String text) {
        if (tagName.equalsIgnoreCase("select")) {
            return String.format("//select[option[@selected][text()='%s']]", text);
        } else if (tagName.equalsIgnoreCase("input") || tagName.equalsIgnoreCase("textarea")) {
            return String.format("//input[@value='%s']", text);
        } else {
            return String.format("//%s[descendant-or-self::text()[starts-with(.,'%s')]]", tagName, text);
        }
    }


    @Override
    public WebElement resolve(ElementSuperLocator elementSuperLocator, WebPlatformSession session) {
        try {
            String query = elementSuperLocator.getElementLocatorByType(TYPE).orElseThrow(ElementLocatorNotFoundException::new).getQuery();
            if (!Strings.isNullOrEmpty(query) && query.contains(":") && query.split(":").length == 2) {
                sessionLogger.info(this.getClass(), String.format("Finding by text %s", query));
                final String tagName = query.split(":")[0];
                final String text = query.split(":")[1];
                final String xpath = buildQuery(tagName, text);
//                Shadow shadow = new Shadow(session.getWebDriver());

                List<WebElement> elements = session.getWebDriver().findElements(By.xpath(xpath));// shadow.findElementsByXPath(xpath); //;
                if (elements.size() == 1) {
                    return elements.get(0);
                } else {
                    final String failMsg = String.format("Failed finding by text %s with element size = %d", query, elements.size());
                    sessionLogger.warning(this.getClass(), failMsg, null);
                    throw new IllegalStateException(failMsg);
                }
            } else {
                throw new IllegalStateException(String.format("Cannot query with text : %s", query));
            }
        } catch (Throwable t) {
            sessionLogger.error(this.getClass(), "Failed resolving", t);
            return fallBack().resolve(elementSuperLocator, session);
        }

    }


    @Override
    public ElementResolver fallBack() {
        return new ElementXPathElementResolver(sessionLogger);
    }
}
