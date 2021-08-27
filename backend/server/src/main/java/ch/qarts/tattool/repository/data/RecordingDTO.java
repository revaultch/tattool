package ch.qarts.tattool.repository.data;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.recording.Recording;
import ch.qarts.tattool.core.domain.recording.RecordingStep;
import ch.qarts.tattool.repository.Mapper;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "Recording")
public class RecordingDTO extends Mapper<RecordingDTO, Recording> {

    @Id
    private String id;
    private String name;
    private String description;
    private String platform;
    private List<String> tags;

    @DBRef
    private DatasetDTO dataset;

    private List<CommandDTO<?>> recordingStepList = new ArrayList<>();

    @Override
    public RecordingDTO toDTO(Recording recording) {
        return recording != null ? RecordingDTO.builder().
                id(recording.getId())
                .name(recording.getName())
                .description(recording.getDescription())
                .platform(recording.getPlatform())
                .tags(recording.getTags())
                .dataset(DatasetDTO.builder().build().toDTO(recording.getDataset()))
                .recordingStepList(recording.getRecordingStepList() != null ?
                        recording.getRecordingStepList().stream()
                                .map(this::commandToDTO)
                                .collect(Collectors.toList()) : null)
                .build() : null;

    }

    private CommandDTO<?> commandToDTO(RecordingStep<?> recordingStep) {
        return CommandDTO.builder().build().toDTO((RecordingStep<Payload>) recordingStep);
    }

    @Override
    public Recording toModel() {
        return Recording.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .platform(this.getPlatform())
                .tags(this.getTags())
                .dataset(Objects.nonNull(this.getDataset()) ? this.getDataset().toModel() : null)
                .recordingStepList(this.recordingStepList != null ? this.recordingStepList.stream().map(CommandDTO::toModel).collect(Collectors.toList()) : null)
                .build();

    }


}
