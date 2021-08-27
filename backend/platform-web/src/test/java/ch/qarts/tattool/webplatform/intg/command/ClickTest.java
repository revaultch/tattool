package ch.qarts.tattool.webplatform.intg.command;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.command.impl.click.Click;
import ch.qarts.tattool.webplatform.command.impl.click.ClickPayload;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClickTest extends AbstractSessionTest {

    @Test
    public void shouldBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("<button>hello</button>", "");
        assertTrue(new Click().isEligible(webPlatformSession));
    }

    @Test
    public void shouldNotBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("", "");
        assertFalse(new Click().isEligible(webPlatformSession));
    }

    @Test
    public void shouldExecuteCommand() {
        WebPlatformSession webPlatformSession = buildSession("<button qarts-uuid='xx-yy-zz' id='hello' onclick='document.getElementById(`text`).innerHTML=`done`'>Hello</button><div id='text'></div>",
                "");
        ClickPayload payload = ClickPayload.builder()
                .verb("click")
                .elementSuperLocator(ElementSuperLocator.builder()
                        .id("xx-yy-zz")
                        .build())
                .build();
        new Click().execute(webPlatformSession, payload, simpleLogger);
        var textDiv = webPlatformSession.getWebDriver().findElement(By.id("text"));
        assertEquals("done", textDiv.getText());
    }

    @Test
    public void shouldReturnContext() {
        WebPlatformSession webPlatformSession = buildSession("<button id='hello'>Hello</button>",
                "");
        var contextData = new Click().contextData(webPlatformSession);
        assertTrue(contextData.containsKey("elementSuperLocator"));
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
