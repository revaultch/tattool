package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.execution.Executable;
import ch.qarts.tattool.core.domain.execution.ExecutableTestExecutionService;
import ch.qarts.tattool.core.domain.execution.live.ProgressListener;
import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;
import ch.qarts.tattool.core.domain.run.Type;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.domain.transpilation.TranspilationService;
import ch.qarts.tattool.core.port.out.RecordingFilterRepositoryPort;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static ch.qarts.tattool.core.util.FutureUtils.allOf;

@AllArgsConstructor
public class RecordingFilterServiceImpl implements RecordingFilterService {

    private final RecordingFilterRepositoryPort recordingFilterRepositoryPort;

    private final TranspilationService transpilationService;

    private final ExecutableTestExecutionService executableTestExecutionService;

    @Override
    public List<RecordingFilter> getRecordingFilters() {
        return recordingFilterRepositoryPort.findAll();
    }

    @Override
    public RecordingFilter getRecordingFilter(String id) {
        return recordingFilterRepositoryPort.getOne(id);
    }

    /**
     * @param
     */
    @Override
    public Executable buildExecutable(String id, SessionConfiguration sessionConfiguration, ProgressListener progressListener) {
        var recordingFilter = recordingFilterRepositoryPort.getOne(id);
        return Executable.builder()
                .type(Type.RECORDINGFILTER)
                .title(String.format("Executable for %s (%s)", recordingFilter.getDescription(), id))
                .executionList(allOf(transpilationService.transpile(recordingFilter).stream().map(executableTestScenario -> executableTestExecutionService.execute(executableTestScenario, sessionConfiguration, progressListener)).collect(Collectors.toList())))
                .build();
    }

    @Override
    public RecordingFilter save(RecordingFilter recordingFilter) {
        return this.recordingFilterRepositoryPort.save(recordingFilter);
    }

    @Override
    public void delete(String id) {
        recordingFilterRepositoryPort.deleteById(id);
    }


}
