package ch.qarts.tattool.core.domain.transpilation;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.dataset.Content;
import ch.qarts.tattool.core.domain.recording.Recording;
import ch.qarts.tattool.core.domain.recording.RecordingStep;
import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;
import ch.qarts.tattool.core.port.out.RecordingRepositoryPort;
import lombok.SneakyThrows;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TranspilationServiceImpl implements TranspilationService {
    private RecordingRepositoryPort recordingRepositoryPort;

    public TranspilationServiceImpl(RecordingRepositoryPort recordingRepositoryPort) {
        this.recordingRepositoryPort = recordingRepositoryPort;
    }


    @Override
    public List<ExecutableRecordingInstance> transpile(RecordingFilter recordingFilter) {
        var recordings = recordingRepositoryPort.withTags(recordingFilter.getTags());
        return recordings.stream().map(this::transpile).flatMap(Collection::stream).collect(Collectors.toList());
    }


    @Override
    public List<ExecutableRecordingInstance> transpile(final Recording recording) {
        if (Objects.nonNull(recording.getDataset())) {
            final var content = recording.getDataset().getContent();
            return IntStream.range(0, content.getRows().size())
                    .mapToObj(rowIndex -> this.mapOneRow(recording, content, rowIndex))
                    .collect(Collectors.toList());
        } else {
            return Collections.singletonList(ExecutableRecordingInstance.builder()
                    .platform(recording.getPlatform())
                    .parentRecordingId(recording.getId())
                    .name(recording.getName())
                    .payloadList(recording.getRecordingStepList().stream().map(RecordingStep::getPayload).collect(Collectors.toList()))
                    .build());
        }
    }

    private ExecutableRecordingInstance mapOneRow(final Recording recording, final Content content, final int rowIndex) {
        return ExecutableRecordingInstance.builder()
                .payloadList(
                        recording.getRecordingStepList().stream()
                                .map(command -> mapRecordingStep(command, content, rowIndex))
                                .collect(Collectors.toList())

                )
                .name(recording.getName())
                .platform(recording.getPlatform())
                .id(UUID.randomUUID().toString())
                .parentRecordingId(recording.getId())
                .datasetRow(content.getRows().get(rowIndex))
                .build();
    }

    private Payload mapRecordingStep(final RecordingStep<?> recordingStep, final Content content, final int rowIndex) {
        Payload payload = SerializationUtils.clone(recordingStep.getPayload());
        for (var key : recordingStep.getMappings().keySet()) {
            mapValue(payload, key, content.getValue(recordingStep.getMappings().get(key), rowIndex));
        }
        return payload;
    }

    @SneakyThrows
    private void mapValue(Payload payload, final String property, final String value) {
        Arrays.stream(payload.getClass().getDeclaredMethods())
                .filter(f -> Objects.nonNull(f) && f.getName().equals(setterName(property)))
                .findFirst()
                .orElseThrow(IllegalStateException::new).invoke(payload, value);
    }

    private String setterName(final String property) {
        return "set" + StringUtils.capitalize(property);
    }


}
