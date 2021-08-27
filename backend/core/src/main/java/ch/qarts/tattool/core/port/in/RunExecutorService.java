package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.execution.live.ProgressListener;
import ch.qarts.tattool.core.domain.run.Run;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;

public interface RunExecutorService {

    Run runRecording(String id, SessionConfiguration sessionConfiguration, ProgressListener progressListener);

    Run runRecordingFilter(String id, SessionConfiguration sessionConfiguration, ProgressListener progressListener);

}
