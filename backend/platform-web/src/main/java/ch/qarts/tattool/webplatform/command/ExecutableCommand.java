package ch.qarts.tattool.webplatform.command;

import ch.qarts.tattool.core.domain.command.ContextData;
import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.session.SessionProxy;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import lombok.ToString;

/**
 * Executes payload against session. Returns reviewed payload if needed (i.e. complete with locators for non design mode)
 * <p>
 *
 * @param <S>
 * @param <P>
 */
@ToString
public abstract class ExecutableCommand<S extends SessionProxy, P extends Payload> {

    public abstract boolean isEligible(S session);

    public abstract P execute(S session, P payload, SimpleLogger sessionLogger);

    public abstract ContextData contextData(S session);

    public abstract Class<P> getPayloadType();

}
