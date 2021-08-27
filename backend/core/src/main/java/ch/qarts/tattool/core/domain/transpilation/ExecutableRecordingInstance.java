package ch.qarts.tattool.core.domain.transpilation;

import ch.qarts.tattool.core.domain.command.Payload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutableRecordingInstance {

    private String id;

    private String parentRecordingId;

    private String platform;

    private String name;

    private List<String> datasetRow;

    private List<Payload> payloadList;

}
