package ch.qarts.tattool.platform;

import ch.qarts.tattool.webplatform.platform.WebPlatform;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PreDestroy;
import java.io.Serializable;

@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WebPlatformBean extends WebPlatform implements Serializable {

    @PreDestroy
    public void onDestroy() throws Exception {
        this.clean();
    }
}
