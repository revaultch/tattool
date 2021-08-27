package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.execution.Executable;
import ch.qarts.tattool.core.domain.execution.ExecutableTestExecutionService;
import ch.qarts.tattool.core.domain.execution.live.ProgressListener;
import ch.qarts.tattool.core.domain.recording.Recording;
import ch.qarts.tattool.core.domain.run.Type;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.domain.transpilation.TranspilationService;
import ch.qarts.tattool.core.port.out.RecordingRepositoryPort;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static ch.qarts.tattool.core.util.FutureUtils.allOf;

@AllArgsConstructor
public class RecordingServiceImpl implements RecordingService {


    private final RecordingRepositoryPort recordingRepository;
    private final TranspilationService transpilationService;
    private final ExecutableTestExecutionService executableTestExecutionService;


    @Override
    public Recording save(Recording recording) {
        return this.recordingRepository.save(recording);
    }

    @Override
    public Recording getRecording(String id) {
        return this.recordingRepository.getOne(id);
    }

    @Override
    public Executable buildExecutable(String id, SessionConfiguration sessionConfiguration, ProgressListener progressListener) {
        var recording = recordingRepository.getOne(id);
        return Executable.builder()
                .type(Type.RECORDING)
                .title(String.format("Executable for %s (%s)", recording.getDescription(), id))
                .executionList(allOf(transpilationService.transpile(recording).stream().map(executableTestScenario -> executableTestExecutionService.execute(executableTestScenario, sessionConfiguration, progressListener)).collect(Collectors.toList())))
                .build();
    }

    @Override
    public List<Recording> getAllRecordings() {
        return recordingRepository.findAll();
    }

    @Override
    public List<Recording> getRecordingsWithTags(List<String> tags) {
        return recordingRepository.withTags(tags);
    }

    @Override
    public void deleteRecording(String id) {
        recordingRepository.delete(id);
    }


}
