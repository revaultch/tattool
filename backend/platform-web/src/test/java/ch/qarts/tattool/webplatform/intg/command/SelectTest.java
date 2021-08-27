package ch.qarts.tattool.webplatform.intg.command;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.command.impl.select.Select;
import ch.qarts.tattool.webplatform.command.impl.select.SelectPayload;
import ch.qarts.tattool.webplatform.command.impl.select.SelectedItem;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SelectTest extends AbstractSessionTest {

    @Test
    public void shouldBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("<select></select>", "");
        assertTrue(new Select().isEligible(webPlatformSession));
    }

    @Test
    public void shouldNotBeEligibleCommand() {
        WebPlatformSession webPlatformSession = buildSession("", "");
        assertFalse(new Select().isEligible(webPlatformSession));
    }

    @Test
    public void shouldExecuteCommand() {
        WebPlatformSession webPlatformSession = buildSession("<select qarts-uuid='xx-yy-zz-11'><option>1</option><option>2</option><option>3</option></select><select id='s2' qarts-uuid='xx-yy-zz-22'><option>11</option><option>22</option><option>33</option></select>",
                "");
        SelectPayload payload = SelectPayload.builder()
                .verb("select")
                .selection(SelectedItem.builder()
                        .option("22")
                        .elementSuperLocator(ElementSuperLocator.builder()
                                .id("xx-yy-zz-22")
                                .build())
                        .build())
                .build();
        new Select().execute(webPlatformSession, payload, simpleLogger);
        var webElement = webPlatformSession.getWebDriver().findElement(By.id("s2"));
        org.openqa.selenium.support.ui.Select select2 = new org.openqa.selenium.support.ui.Select(webElement);
        assertEquals(1, select2.getAllSelectedOptions().size());
        assertEquals("22", select2.getAllSelectedOptions().get(0).getText());
    }

    @Test
    public void shouldReturnContext() {
        WebPlatformSession webPlatformSession = buildSession("<select id='s1'><option>1</option><option>2</option></select><select id='s2'><option>4</option><option>5</option><option>6</option></select>",
                "");
        var contextData = new Select().contextData(webPlatformSession);

        assertTrue(contextData.containsKey("selection"));
        var elementSuperLocatorList = (List<?>) ((Map<?, ?>) contextData.get("selection")).get("elementSuperLocatorList");

        assertEquals(2, elementSuperLocatorList.size());

        var options = (Map<?, ?>) ((Map<?, ?>) contextData.get("selection")).get("options");

        var options1 = (List<?>) options.get(((ElementSuperLocator) elementSuperLocatorList.get(0)).getId());

        var options2 = (List<?>) options.get(((ElementSuperLocator) elementSuperLocatorList.get(1)).getId());

        assertEquals(2, options1.size());
        assertEquals(3, options2.size());

    }

    @Test
    public void shouldReturnEmptyContext() {
        WebPlatformSession webPlatformSession = buildSession("<div id='text'></div>",
                "");

        var contextData = new Select().contextData(webPlatformSession);
        assertTrue(contextData.containsKey("selection"));
        var options = (Map<?, ?>) ((Map<?, ?>) contextData.get("selection")).get("options");
        var elementSuperLocatorList = (List<?>) ((Map<?, ?>) contextData.get("selection")).get("elementSuperLocatorList");

        assertEquals(0, options.size());
        assertEquals(0, elementSuperLocatorList.size());

    }


}
