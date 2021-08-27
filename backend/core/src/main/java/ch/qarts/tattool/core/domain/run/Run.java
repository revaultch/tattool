package ch.qarts.tattool.core.domain.run;

import ch.qarts.tattool.core.domain.execution.reporting.ExecutableRecordingInstanceExecutionReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Run {
    private String id;
    private Type type;
    private String title;
    private RunStatus status;
    private List<ExecutableRecordingInstanceExecutionReport> executableRecordingInstanceExecutionReportList;
}

