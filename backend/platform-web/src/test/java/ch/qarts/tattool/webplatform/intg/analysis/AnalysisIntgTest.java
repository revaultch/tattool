package ch.qarts.tattool.webplatform.intg.analysis;

import ch.qarts.tattool.core.domain.element.ElementLocator;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.intg.Page;
import ch.qarts.tattool.webplatform.locator.FastWithRetryElementResolver;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalysisIntgTest extends AbstractSessionTest {


    @Test
    public void shouldFindById() throws Exception {
        Page page = Page.builder()
                .bodyContent("<div id='someIdHere'></div>")
                .build();

        var resolver = new FastWithRetryElementResolver(3, 1, simpleLogger);
        ElementSuperLocator elementSuperLocator = ElementSuperLocator.builder().id(null).screenshot("somescreenshotdata").locators(Arrays.asList(ElementLocator.builder().type("id").query("someIdHere").build(), ElementLocator.builder().type("xpath").query("//DIV[@id=\"div1\"]/BUTTON[1]").build())).build();
        WebElement webElement = resolver.resolve(elementSuperLocator, buildSession(page));
        assertEquals("someIdHere", webElement.getAttribute("id"));
    }

    @Test
    public void shouldFindByText() throws Exception {
        Page page = Page.builder()
                .bodyContent("<button id='someIdHere'>buttonText</button>")
                .build();
        var resolver = new FastWithRetryElementResolver(3, 1, simpleLogger);
        ElementSuperLocator elementSuperLocator = ElementSuperLocator.builder().id(null).screenshot("somescreenshotdata").locators(Arrays.asList(ElementLocator.builder().type("id").query("noValidId").build(), ElementLocator.builder().type("text").query("button:buttonText").build())).build();
        WebElement webElement = resolver.resolve(elementSuperLocator, buildSession(page));
        assertEquals("someIdHere", webElement.getAttribute("id"));
    }


    @Test
    public void shouldFindByXPath() throws Exception {
        Page page = Page.builder()
                .bodyContent("<div id='div1'><button id='someIdHere'></button><button></button></div>")
                .build();
        var resolver = new FastWithRetryElementResolver(3, 1, simpleLogger);
        ElementSuperLocator elementSuperLocator = ElementSuperLocator.builder().id(null).screenshot("somescreenshotdata").locators(Arrays.asList(ElementLocator.builder().type("id").query("noValidId").build(), ElementLocator.builder().type("xpath").query("//DIV[@id=\"div1\"]/BUTTON[1]").build())).build();
        WebElement webElement = resolver.resolve(elementSuperLocator, buildSession(page));
        assertEquals("someIdHere", webElement.getAttribute("id"));
    }


}
