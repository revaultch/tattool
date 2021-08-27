package ch.qarts.tattool.webplatform.platform;

import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;

public interface WebPlatformSessionProvider {

    WebPlatformSession provide(ProviderConfig providerConfiguration, SessionConfiguration sessionConfiguration);

}
