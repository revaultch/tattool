package ch.qarts.tattool.webplatform.command;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;

import java.lang.reflect.ParameterizedType;

public abstract class WebPlatformExecutableCommand<P extends Payload> extends ExecutableCommand<WebPlatformSession, P> {

    public Class<P> getPayloadType() {
        return (Class<P>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }


}
