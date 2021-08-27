package ch.qarts.tattool.repository.data;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.recording.RecordingStep;
import ch.qarts.tattool.repository.Mapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommandDTO<T extends Payload> extends Mapper<CommandDTO<T>, RecordingStep<T>> {

    //TODO id required here ?
    private String id;
    private T payload;
    private Map<String, String> mappings = new ConcurrentHashMap<>();


    @Override
    public CommandDTO<T> toDTO(RecordingStep<T> recordingStep) {
        return CommandDTO.<T>builder()
                .id(recordingStep.getId())
                .payload(recordingStep.getPayload())
                .mappings(recordingStep.getMappings())
                .build();
    }

    @Override
    public RecordingStep<T> toModel() {
        return RecordingStep.<T>builder()
                .id(this.getId())
                .payload(this.getPayload())
                .mappings(this.getMappings())
                .build();

    }
}
