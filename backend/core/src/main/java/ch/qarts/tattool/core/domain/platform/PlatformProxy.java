package ch.qarts.tattool.core.domain.platform;

import ch.qarts.tattool.core.domain.command.PayloadFactory;
import ch.qarts.tattool.core.domain.command.descriptor.CommandDescriptor;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.domain.session.SessionProxy;

import java.util.List;

public interface PlatformProxy {

    String getId();

    String getDescription();

    List<CommandDescriptor> getAvailableCommands();

    /**
     * Responsible for creating a session. Will throw a runtime exception in case of failure
     *
     * @param sessionConfiguration
     * @return
     * @throws ch.qarts.tattool.core.domain.session.SessionCreationException
     */
    SessionProxy createSession(SessionConfiguration sessionConfiguration);

    SessionProxy getSession(String sessionId);

    PayloadFactory payloadFactory();

    void clean();

    void destroySession(String sessionId);

}
