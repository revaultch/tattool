package ch.qarts.tattool.webplatform.command.impl.select;

import ch.qarts.tattool.core.domain.command.ContextData;
import ch.qarts.tattool.core.domain.command.descriptor.Metadata;
import ch.qarts.tattool.core.domain.command.descriptor.Renderer;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.command.WebPlatformExecutableCommand;
import ch.qarts.tattool.webplatform.locator.FastWithRetryElementResolver;
import ch.qarts.tattool.webplatform.locator.QartsUUIDElementResolver;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.qarts.tattool.webplatform.locator.Utils.completeIfNeeded;
import static ch.qarts.tattool.webplatform.locator.Utils.moveTo;
import static ch.qarts.tattool.webplatform.platform.WebPlatform.FAST_IMPLICIT_WAIT_IN_SECONDS;
import static ch.qarts.tattool.webplatform.platform.WebPlatform.NB_OF_RETRIES;

@EqualsAndHashCode(callSuper = true)
@Metadata(name = "select", description = "selects text from html selector")
@Renderer("select")
public class Select extends WebPlatformExecutableCommand<SelectPayload> {

    @Override
    public boolean isEligible(WebPlatformSession session) {
        return session.getAnalyzerSnapshot().getSelectable().size() > 0;
    }

    @Override
    public SelectPayload execute(WebPlatformSession session, SelectPayload payload, SimpleLogger sessionLogger) {
        session.waitUntilComplete();
        payload.getSelection().setElementSuperLocator(completeIfNeeded(session, payload.getSelection().getElementSuperLocator()));
        WebElement webElement = moveTo(session.getWebDriver(), new FastWithRetryElementResolver(NB_OF_RETRIES, FAST_IMPLICIT_WAIT_IN_SECONDS, sessionLogger).resolve(payload.getSelection().getElementSuperLocator(), session));
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(webElement);
        select.selectByVisibleText(payload.getSelection().getOption());
        return payload;
    }


    private List<String> options(WebPlatformSession session, List<ElementSuperLocator> elementSuperLocatorList, String id) {
        ElementSuperLocator superLocator = elementSuperLocatorList.stream().filter(elementSuperLocator -> elementSuperLocator.getId().equals(id)).findFirst().orElseThrow();
        WebElement webElement = new QartsUUIDElementResolver().resolve(superLocator, session);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(webElement);
        return select.getOptions().stream().map(WebElement::getText).collect(Collectors.toList());
    }


    @Override
    public ContextData contextData(WebPlatformSession session) {
        ContextData contextData = new ContextData();
        List<ElementSuperLocator> elementSuperLocatorList = session.getAnalyzerSnapshot().getSelectable();
        var options = new HashMap<>();
        for (ElementSuperLocator elementSuperLocator : elementSuperLocatorList) {
            options.put(elementSuperLocator.getId(), options(session, elementSuperLocatorList, elementSuperLocator.getId()));
        }
        contextData.put("selection", Map.of("options", options, "elementSuperLocatorList", elementSuperLocatorList));
        return contextData;
    }


}
