package ch.qarts.tattool.webplatform.intg.shadow;

import ch.qarts.tattool.core.domain.element.ElementLocator;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.intg.Page;
import ch.qarts.tattool.webplatform.locator.FastWithRetryElementResolver;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

// Note for futur shadow dom support
@Disabled
public class ShadowDomTest extends AbstractSessionTest {

    @SneakyThrows
    private Path getDocumentPath() {
        var filePath = Page.class.getClassLoader().getResource("html/shadow/index.html");
        assert filePath != null;
        return Path.of(filePath.toURI());
    }

    @Test
    public void shouldDoSomething() {
        var resolver = new FastWithRetryElementResolver(3, 1, simpleLogger);
        ElementSuperLocator elementSuperLocator = ElementSuperLocator.builder().id(null)
                .locators(List.of(
                        ElementLocator.builder().type("text").query("input:hola")
                                .build()))
                .screenshot("somescreenshotdata").build();
        var session = buildSession(getDocumentPath());
        session.forceAnalyzerSnapshot(simpleLogger);
        WebElement webElement = resolver.resolve(elementSuperLocator, session);
        assertNotNull(webElement);
    }
}