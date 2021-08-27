package ch.qarts.tattool.core.domain.execution;

import ch.qarts.tattool.core.domain.execution.reporting.ExecutableRecordingInstanceExecutionReport;
import ch.qarts.tattool.core.domain.run.Type;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
@Builder
public class Executable {
    private Type type;
    private String title;
    private CompletableFuture<List<ExecutableRecordingInstanceExecutionReport>> executionList;
}
