package ch.qarts.tattool.core.domain.recording;


import ch.qarts.tattool.core.domain.command.Payload;
import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class RecordingStep<T extends Payload> {

    @NonNull
    private String id;

    private T payload;

    private Map<String, String> mappings = new ConcurrentHashMap<>();

}
