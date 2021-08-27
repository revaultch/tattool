package ch.qarts.tattool.webplatform.intg.resolver;

import ch.qarts.tattool.core.domain.element.ElementLocator;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.intg.Page;
import ch.qarts.tattool.webplatform.locator.FastWithRetryElementResolver;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElementVisibleTextElementResolverTest extends AbstractSessionTest {

    private void doTest(String subPage, String query, String expectedTagName) {
        Page page = Page.builder()
                .bodyContent(String.format("<div id='div1'><button id='someIdHere'></button><button></button>%s</div>", subPage))
                .build();
        var resolver = new FastWithRetryElementResolver(3, 1, simpleLogger);
        ElementSuperLocator elementSuperLocator = ElementSuperLocator.builder().id(null)
                .locators(Arrays.asList(ElementLocator.builder().type("id").query("noValidId").build(),
                        ElementLocator.builder().type("text").query(query)
                                .build()))
                .screenshot("somescreenshotdata").build();
        WebElement webElement = resolver.resolve(elementSuperLocator, buildSession(page));
        assertEquals(expectedTagName, webElement.getTagName());

    }

    @Test
    public void shouldResolveInput() {
        doTest("<input type='text' value='hello'></input>", "input:hello", "input");
    }

    @Test
    public void shouldResolveSelect() {
        doTest("<select><option>1</option><option>1</option><option selected='selected'>whatever</option></select>", "select:whatever", "select");
    }

    @Test
    public void shouldResolveSelectWNoSelection() {
        doTest("<select><option>1</option><option>1</option><option selected='selected'>whatever</option></select>", "select:whatever", "select");
    }

    @Disabled // TODO fix issue with wrapping divs
    @Test
    public void shouldResolveDIV() {
        doTest("<div>some</div>", "div:some", "div");
    }

    @Test
    public void shouldResolveP() {
        doTest("<p>some</p>", "p:some", "p");
    }

    @Test
    public void shouldResolveLI() {
        doTest("<li>some</li>", "li:some", "li");
    }

    @Test
    public void shouldResolveSPAN() {
        doTest("<span>some</span>", "span:some", "span");
    }

    @Test
    public void shouldResolveBUTTON() {
        doTest("<button>I agree</button>", "button:I agree", "button");
    }

    @Test
    public void shouldResolveBUTTON2() {
        doTest("<button><div>I agree</div></button>", "button:I agree", "button");
    }


}
