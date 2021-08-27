package ch.qarts.tattool.core.domain.recording;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.dataset.Dataset;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class Recording {

    private String id;
    private String name;
    private String description;
    private String platform;
    private List<String> tags;
    private Dataset dataset;

    private List<RecordingStep<? extends Payload>> recordingStepList = new ArrayList<>();
}
