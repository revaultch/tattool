package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.execution.client.validation.ValidationResult;
import ch.qarts.tattool.core.domain.execution.reporting.PayloadInstanceExecutionReport;
import ch.qarts.tattool.core.domain.recording.RecordingStep;
import ch.qarts.tattool.core.domain.session.SessionProxy;

import java.util.List;

public interface PayloadExecutionService {

    PayloadInstanceExecutionReport execute(SessionProxy session, Payload payload);

    ValidationResult validateBatch(SessionProxy session, List<RecordingStep<? extends Payload>> recordingStepList);
}
