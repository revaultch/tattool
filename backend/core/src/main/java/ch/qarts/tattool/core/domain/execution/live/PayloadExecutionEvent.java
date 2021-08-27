package ch.qarts.tattool.core.domain.execution.live;

import ch.qarts.tattool.core.domain.execution.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadExecutionEvent implements ExecutionEvent {

    private String platformId;

    private String recordingId;

    private String recordingDisplayName;

    private String recordingInstanceId;

    private String id;

    private String displayName;

    private Object payload;

    private Status status;

    private Long durationInMs;

    private List<String> datasetRow;

}