package ch.qarts.tattool.core.domain.recordingfilter;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RecordingFilter {
    private String id;
    private String name;
    private String description;
    private List<String> tags;
}
