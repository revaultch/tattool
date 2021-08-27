package ch.qarts.tattool.webplatform.locator;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import org.openqa.selenium.WebElement;

public interface ElementResolver {

    WebElement resolve(ElementSuperLocator elementSuperLocator, WebPlatformSession session);

    ElementResolver fallBack();

}
