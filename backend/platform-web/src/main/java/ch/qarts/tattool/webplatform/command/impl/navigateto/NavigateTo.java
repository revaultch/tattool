package ch.qarts.tattool.webplatform.command.impl.navigateto;

import ch.qarts.tattool.core.domain.command.ContextData;
import ch.qarts.tattool.core.domain.command.descriptor.Metadata;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.command.WebPlatformExecutableCommand;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Metadata(name = "navigateTo", description = "navigates to url")
public class NavigateTo extends WebPlatformExecutableCommand<NavigateToPayload> {

    @Override
    public boolean isEligible(WebPlatformSession session) {
        return true;
    }

    @Override
    public NavigateToPayload execute(WebPlatformSession session, NavigateToPayload payload, SimpleLogger sessionLogger) {
        session.waitUntilComplete();
        var driver = session.getWebDriver();
        driver.navigate().to(payload.getUrl());
        return payload;
    }

    @Override
    public ContextData contextData(WebPlatformSession session) {
        return null;
    }


}
