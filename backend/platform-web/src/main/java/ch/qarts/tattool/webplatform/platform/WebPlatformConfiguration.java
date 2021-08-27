package ch.qarts.tattool.webplatform.platform;

import lombok.Data;

@Data
public class WebPlatformConfiguration {

    private String sessionProvider;

    private ProviderConfig providerConfig;

}
