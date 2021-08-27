package ch.qarts.tattool.webplatform.intg.resolver;

import ch.qarts.tattool.core.domain.element.ElementLocator;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.intg.Page;
import ch.qarts.tattool.webplatform.locator.FastWithRetryElementResolver;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FastResolverTest extends AbstractSessionTest {

    final static Page p1 = Page.builder()
            .bodyContent("<div><input type='radio' id='axx'><button id='123' name='button'></div>")
            .scriptContent("")
            .build();


    @Test
    public void shouldResolveXpath() {
        WebPlatformSession session = buildSession(p1);
        FastWithRetryElementResolver fastWithRetryElementResolver = new FastWithRetryElementResolver(3, 5, simpleLogger);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        WebElement webElement = fastWithRetryElementResolver.resolve(ElementSuperLocator.builder().locators(
                Arrays.asList(
                        ElementLocator.builder().type("id").query("fwefwe").build(),
                        ElementLocator.builder().type("text").query("qdqwd").build(),
                        ElementLocator.builder().type("xpath").query("/html/body/div/button").build())
        ).build(), session);

        assertEquals("123", webElement.getAttribute("id"));
        assertTrue(stopWatch.stop() < 15);

    }


    @Test
    public void shouldRetryWhenFailure() {

        WebPlatformSession session = buildSession(p1);
        FastWithRetryElementResolver fastWithRetryElementResolver = new FastWithRetryElementResolver(3, 5, simpleLogger);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        WebElement webElement = null;
        try {
            webElement = fastWithRetryElementResolver.resolve(ElementSuperLocator.builder().locators(
                    Arrays.asList(
                            ElementLocator.builder().type("id").query("fwefwe").build(),
                            ElementLocator.builder().type("text").query("xy:qdqwd").build(),
                            ElementLocator.builder().type("xpath").query("/html/body/div/buttonX").build())
            ).build(), session);
        } catch (Exception e) {
            //  nothing
        }

        assertNull(webElement);
        assertTrue(stopWatch.stop() > 40);

    }


    static class StopWatch {
        private long init;

        void start() {
            this.init = System.currentTimeMillis();
        }

        long stop() {
            return (System.currentTimeMillis() - this.init) / 1000;
        }

    }

}
