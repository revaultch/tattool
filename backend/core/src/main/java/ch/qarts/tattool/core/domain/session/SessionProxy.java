package ch.qarts.tattool.core.domain.session;

import ch.qarts.tattool.core.domain.command.ContextData;
import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.descriptor.CommandDescriptor;

import java.util.List;

public interface SessionProxy {

    void close();

    String getId();

    Payload execute(Payload payload, SimpleLogger sessionLogger);

    ContextData commandContextData(String commandName);

    String getScreenshot();

    SessionConfiguration getSessionConfiguration();

    /**
     * Returns available commands for current session context
     **/
    List<CommandDescriptor> getAvailableCommands();
}
