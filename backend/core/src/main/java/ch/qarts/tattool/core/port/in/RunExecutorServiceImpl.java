package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.execution.Executable;
import ch.qarts.tattool.core.domain.execution.live.ProgressListener;
import ch.qarts.tattool.core.domain.execution.live.RunTerminatedExecutionEvent;
import ch.qarts.tattool.core.domain.run.Run;
import ch.qarts.tattool.core.domain.run.RunStatus;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.port.out.RunRepositoryPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RunExecutorServiceImpl implements RunExecutorService {

    private RunService runService;

    private RecordingService recordingService;

    private RecordingFilterService recordingFilterService;

    private RunRepositoryPort runRepositoryPort;


    private Run doRun(Executable executable, ProgressListener progressListener) {
        Run run = Run.builder()
                .build();
        try {
            run.setExecutableRecordingInstanceExecutionReportList(executable.getExecutionList().get());
            run.setTitle(executable.getTitle());
            run.setType(executable.getType());
            run.setStatus(RunStatus.FINISHED);
        } catch (Throwable t) {
            run.setStatus(RunStatus.ERROR);
        } finally {
            run = this.runRepositoryPort.save(run);
        }
        progressListener.onDone(RunTerminatedExecutionEvent.builder()
                .runId(run.getId())
                .build());
        return run;
    }

    @Override
    public Run runRecording(String id, SessionConfiguration sessionConfiguration, ProgressListener progressListener) {
        return doRun(this.recordingService.buildExecutable(id, sessionConfiguration, progressListener), progressListener);
    }


    @Override
    public Run runRecordingFilter(String id, SessionConfiguration sessionConfiguration, ProgressListener progressListener) {
        return doRun(this.recordingFilterService.buildExecutable(id, sessionConfiguration, progressListener), progressListener);
    }


}
