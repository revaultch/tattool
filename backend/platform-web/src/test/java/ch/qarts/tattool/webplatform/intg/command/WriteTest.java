package ch.qarts.tattool.webplatform.intg.command;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.command.impl.write.Write;
import ch.qarts.tattool.webplatform.command.impl.write.WritePayload;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WriteTest extends AbstractSessionTest {

    @Test
    public void shouldBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("<input>", "");
        assertTrue(new Write().isEligible(webPlatformSession));
    }

    @Test
    public void shouldNotBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("", "");
        assertFalse(new Write().isEligible(webPlatformSession));
    }

    @Test
    public void shouldExecuteCommand() {
        WebPlatformSession webPlatformSession = buildSession("<input qarts-uuid='xx-yy-zz' id='hello' value=''>",
                "");
        WritePayload payload = WritePayload.builder()
                .verb("write")
                .elementSuperLocator(ElementSuperLocator.builder()
                        .id("xx-yy-zz")
                        .build())
                .text("someText")
                .build();
        new Write().execute(webPlatformSession, payload, simpleLogger);
        var input = webPlatformSession.getWebDriver().findElement(By.id("hello"));
        assertEquals("someText", input.getAttribute("value"));
    }

    @Test
    public void shouldReturnContext() {
        WebPlatformSession webPlatformSession = buildSession("<input>",
                "");
        var contextData = new Write().contextData(webPlatformSession);
        assertTrue(contextData.containsKey("elementSuperLocator"));
        assertEquals(1, ((List<?>) contextData.get("elementSuperLocator")).size());
    }

    @Test
    public void shouldReturnEmptyContext() {
        WebPlatformSession webPlatformSession = buildSession("<div id='text'></div>",
                "");
        var contextData = new Write().contextData(webPlatformSession);
        assertTrue(contextData.containsKey("elementSuperLocator"));
        assertEquals(0, ((List<?>) contextData.get("elementSuperLocator")).size());
    }


}
