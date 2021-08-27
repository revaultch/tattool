package ch.qarts.tattool.core.domain.execution.client.validation;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.execution.reporting.PayloadInstanceExecutionReport;
import ch.qarts.tattool.core.domain.recording.RecordingStep;
import lombok.Data;

import java.util.List;

@Data
public class ValidationResult {
    private boolean success;
    private List<PayloadInstanceExecutionReport> payloadInstanceExecutionReportList;
    private List<RecordingStep<? extends Payload>> validatedList;
}
