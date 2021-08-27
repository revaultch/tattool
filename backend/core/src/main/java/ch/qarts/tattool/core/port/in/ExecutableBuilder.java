package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.execution.Executable;
import ch.qarts.tattool.core.domain.execution.live.ProgressListener;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;

public interface ExecutableBuilder {

    Executable buildExecutable(String id, SessionConfiguration sessionConfiguration, ProgressListener progressListener);

}
