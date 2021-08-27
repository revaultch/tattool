package ch.qarts.tattool.webplatform.command.impl.assertt;

import ch.qarts.tattool.core.domain.command.ContextData;
import ch.qarts.tattool.core.domain.command.descriptor.Metadata;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.command.WebPlatformExecutableCommand;
import ch.qarts.tattool.webplatform.core.CommandExecutionFailedException;
import ch.qarts.tattool.webplatform.locator.FastWithRetryElementResolver;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

import static ch.qarts.tattool.webplatform.locator.Utils.completeIfNeeded;
import static ch.qarts.tattool.webplatform.locator.Utils.moveTo;
import static ch.qarts.tattool.webplatform.platform.WebPlatform.FAST_IMPLICIT_WAIT_IN_SECONDS;
import static ch.qarts.tattool.webplatform.platform.WebPlatform.NB_OF_RETRIES;

@EqualsAndHashCode(callSuper = true)
@Metadata(name = "assert", description = "assert command")
public class Assert extends WebPlatformExecutableCommand<AssertPayload> {

    @Override
    public boolean isEligible(WebPlatformSession session) {
        return session.getAnalyzerSnapshot().getAssertable().size() > 0;
    }

    @Override
    public AssertPayload execute(WebPlatformSession session, AssertPayload payload, SimpleLogger sessionLogger) {
        session.waitUntilComplete();
        payload.setElementSuperLocator(completeIfNeeded(session, payload.getElementSuperLocator()));
        WebElement webElement = moveTo(session.getWebDriver(), new FastWithRetryElementResolver(NB_OF_RETRIES, FAST_IMPLICIT_WAIT_IN_SECONDS, sessionLogger).resolve(payload.getElementSuperLocator(), session));
        String elementText = "";
        if (webElement.getTagName().equals("input") || webElement.getTagName().equals("textarea")) {
            elementText = webElement.getAttribute("value");
        } else if (webElement.getTagName().equals("select")) {
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(webElement);
            elementText = select.getFirstSelectedOption().getText();
        } else {
            elementText = webElement.getText();
        }
        if (StringUtils.isEmpty(elementText)) throw new CommandExecutionFailedException("Found empty text");
        final String expression = payload.getExpression();
        final AssertFunction assertFunction = payload.getAssertFunction();
        switch (assertFunction) {
            case contains:
                assertTrue(String.format("%s contains '%s'", elementText, expression), elementText.contains(expression));
                break;
            case equals:
                assertTrue(String.format("%s is '%s'", elementText, expression), elementText.equals(expression));
                break;
            case startsWith:
                assertTrue(String.format("%s startsWith '%s'", elementText, expression), elementText.startsWith(expression));
                break;
            case endsWith:
                assertTrue(String.format("%s endsWith '%s'", elementText, expression), elementText.endsWith(expression));
                break;
            default:
                throw new CommandExecutionFailedException(String.format("No assertFunction %s found", assertFunction));
        }
        return payload;
    }

    private void assertTrue(String assertionExpr, boolean assertion) {
        if (!assertion) throw new CommandExecutionFailedException(String.format("Assertion %s failed", assertionExpr));
    }


    @Override
    public ContextData contextData(WebPlatformSession session) {
        ContextData contextData = new ContextData();
        contextData.put("elementSuperLocator", session.getAnalyzerSnapshot().getAssertable());
        contextData.put("assertFunction", AssertFunction.values());
        return contextData;
    }


    public enum AssertFunction {
        contains("contains"),
        equals("equals"),
        startsWith("starts with"),
        endsWith("ends with");

        private final String value;

        AssertFunction(String value) {
            this.value = value;
        }

        static AssertFunction fromValue(String value) {
            return Arrays.stream(AssertFunction.values()).filter(af -> af.value.equals(value)).findFirst().orElseThrow(IllegalStateException::new);
        }
    }

}
