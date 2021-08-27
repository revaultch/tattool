package ch.qarts.tattool.core.domain.execution.live;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunTerminatedExecutionEvent implements ExecutionEvent {
    private String runId;
}
