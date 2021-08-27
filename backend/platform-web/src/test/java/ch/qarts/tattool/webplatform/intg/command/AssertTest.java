package ch.qarts.tattool.webplatform.intg.command;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.command.impl.assertt.AssertPayload;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssertTest extends AbstractSessionTest {

    @Test
    public void shouldBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("<input>", "");
        assertTrue(new ch.qarts.tattool.webplatform.command.impl.assertt.Assert().isEligible(webPlatformSession));
    }

    @Test
    public void shouldNotBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("", "");
        assertFalse(new ch.qarts.tattool.webplatform.command.impl.assertt.Assert().isEligible(webPlatformSession));
    }

    @Test
    public void shouldExecuteCommand() {
        WebPlatformSession webPlatformSession = buildSession("<input qarts-uuid='xx-yy-zz' id='hello' value='someValue'>",
                "");
        AssertPayload payload = AssertPayload.builder()
                .verb("assert")
                .elementSuperLocator(ElementSuperLocator.builder()
                        .id("xx-yy-zz")
                        .build())
                .assertFunction(ch.qarts.tattool.webplatform.command.impl.assertt.Assert.AssertFunction.equals)
                .expression("someValue")
                .build();
        new ch.qarts.tattool.webplatform.command.impl.assertt.Assert().execute(webPlatformSession, payload, simpleLogger);
    }

    @Test
    public void shouldReturnContext() {
        WebPlatformSession webPlatformSession = buildSession("<input>",
                "");
        var contextData = new ch.qarts.tattool.webplatform.command.impl.assertt.Assert().contextData(webPlatformSession);
        assertTrue(contextData.containsKey("elementSuperLocator"));
        assertEquals(1, ((List<?>) contextData.get("elementSuperLocator")).size());
        assertTrue(contextData.containsKey("assertFunction"));
    }

    @Test
    public void shouldReturnEmptyContext() {
        WebPlatformSession webPlatformSession = buildSession("<option id='text'></option>",
                "");
        var contextData = new ch.qarts.tattool.webplatform.command.impl.assertt.Assert().contextData(webPlatformSession);
        assertTrue(contextData.containsKey("elementSuperLocator"));
        assertEquals(0, ((List<?>) contextData.get("elementSuperLocator")).size());
        assertTrue(contextData.containsKey("assertFunction"));
    }


}
