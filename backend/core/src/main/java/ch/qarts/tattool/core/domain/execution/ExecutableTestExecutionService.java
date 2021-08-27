package ch.qarts.tattool.core.domain.execution;

import ch.qarts.tattool.core.domain.execution.live.ProgressListener;
import ch.qarts.tattool.core.domain.execution.reporting.ExecutableRecordingInstanceExecutionReport;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.domain.transpilation.ExecutableRecordingInstance;

import java.util.concurrent.CompletableFuture;

public interface ExecutableTestExecutionService {

    CompletableFuture<ExecutableRecordingInstanceExecutionReport> execute(ExecutableRecordingInstance executableTestScenario, SessionConfiguration sessionConfiguration, ProgressListener progressListener);

}
