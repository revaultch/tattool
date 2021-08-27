package ch.qarts.tattool.webplatform.intg.command;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.command.impl.click.Click;
import ch.qarts.tattool.webplatform.command.impl.press.Press;
import ch.qarts.tattool.webplatform.command.impl.press.PressPayload;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PressTest extends AbstractSessionTest {

    @Test
    public void shouldBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("<input>", "");
        assertTrue(new Press().isEligible(webPlatformSession));
    }

    @Test
    public void shouldNotBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("", "");
        assertFalse(new Press().isEligible(webPlatformSession));
    }

    @Test
    public void shouldExecuteCommand() {
        WebPlatformSession webPlatformSession = buildSession("<input qarts-uuid='xx-yy-zz' id='hello' value='123'>",
                "");
        PressPayload payload = PressPayload.builder()
                .verb("click")
                .elementSuperLocator(ElementSuperLocator.builder()
                        .id("xx-yy-zz")
                        .build())
                .key("BACK_SPACE")
                .build();
        new Press().execute(webPlatformSession, payload, simpleLogger);
        var input = webPlatformSession.getWebDriver().findElement(By.id("hello"));
        assertEquals("12", input.getAttribute("value"));
    }

    @Test
    public void shouldReturnContext() {
        WebPlatformSession webPlatformSession = buildSession("<input id='hello'>",
                "");
        var contextData = new Press().contextData(webPlatformSession);
        assertTrue(contextData.containsKey("elementSuperLocator"));
        assertTrue(contextData.containsKey("key"));
        assertEquals(1, ((List<?>) contextData.get("elementSuperLocator")).size());
    }

    @Test
    public void shouldReturnEmptyContext() {
        WebPlatformSession webPlatformSession = buildSession("<div id='text'></div>",
                "");
        var contextData = new Click().contextData(webPlatformSession);
        assertTrue(contextData.containsKey("elementSuperLocator"));
        assertEquals(0, ((List<?>) contextData.get("elementSuperLocator")).size());
    }


}
