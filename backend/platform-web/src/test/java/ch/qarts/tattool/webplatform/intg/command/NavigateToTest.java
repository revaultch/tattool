package ch.qarts.tattool.webplatform.intg.command;

import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.command.impl.navigateto.NavigateTo;
import ch.qarts.tattool.webplatform.command.impl.navigateto.NavigateToPayload;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

public class NavigateToTest extends AbstractSessionTest {

    @Test
    public void shouldBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("", "");
        assertTrue(new NavigateTo().isEligible(webPlatformSession));
    }

    @Test
    public void shouldExecuteCommand() {
        WebPlatformSession webPlatformSession = buildSession("",
                "");
        NavigateToPayload payload = NavigateToPayload.builder()
                .verb("navigateTo")
                .url("data:text/html,<div id='text'>Hello World</div>")
                .build();
        new NavigateTo().execute(webPlatformSession, payload, simpleLogger);
        var textDiv = webPlatformSession.getWebDriver().findElement(By.id("text"));
        assertEquals("Hello World", textDiv.getText());
    }

    @Test
    public void shouldReturnContext() {
        WebPlatformSession webPlatformSession = buildSession("",
                "");
        var contextData = new NavigateTo().contextData(webPlatformSession);
        assertNull(contextData);
    }

}
