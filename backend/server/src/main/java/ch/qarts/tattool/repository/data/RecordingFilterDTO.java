package ch.qarts.tattool.repository.data;

import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;
import ch.qarts.tattool.repository.Mapper;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "RecordingFilter")
public class RecordingFilterDTO extends Mapper<RecordingFilterDTO, RecordingFilter> {

    @Id
    private String id;
    private String name;
    private String description;
    private List<String> tags;


    @Override
    public RecordingFilterDTO toDTO(RecordingFilter recordingFilter) {
        return
                recordingFilter != null ?
                        RecordingFilterDTO.builder()
                                .id(recordingFilter.getId())
                                .name(recordingFilter.getName())
                                .description(recordingFilter.getDescription())
                                .tags(recordingFilter.getTags())
                                .build() : null;

    }

    @Override
    public RecordingFilter toModel() {
        return
                RecordingFilter.builder()
                        .id(this.getId())
                        .name(this.getName())
                        .description(this.getDescription())
                        .tags(this.getTags())
                        .build();
    }


}
