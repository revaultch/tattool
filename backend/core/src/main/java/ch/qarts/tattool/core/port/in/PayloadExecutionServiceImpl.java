package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.execution.Status;
import ch.qarts.tattool.core.domain.execution.client.validation.ValidationResult;
import ch.qarts.tattool.core.domain.execution.reporting.PayloadInstanceExecutionReport;
import ch.qarts.tattool.core.domain.recording.RecordingStep;
import ch.qarts.tattool.core.domain.session.DefaultExecutionLogger;
import ch.qarts.tattool.core.domain.session.SessionProxy;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
public class PayloadExecutionServiceImpl implements PayloadExecutionService {


    @Override
    public PayloadInstanceExecutionReport execute(SessionProxy session, Payload payload) {
        SimpleLogger executionLogger = new DefaultExecutionLogger();
        PayloadInstanceExecutionReport payloadInstanceExecutionReport = new PayloadInstanceExecutionReport();
        payloadInstanceExecutionReport.setId(UUID.randomUUID().toString());
        payloadInstanceExecutionReport.setPayload(payload);
        payloadInstanceExecutionReport.setStatus(Status.RUNNING);
        payloadInstanceExecutionReport.setHumanReadable(payload.humanReadable());
        long start = System.currentTimeMillis();
        var debug = session.getSessionConfiguration() != null && session.getSessionConfiguration().isDebug();
        try {
            if (debug) {
                try {
                    payloadInstanceExecutionReport.setScreenshotBefore(session.getScreenshot());
                } catch (Exception e) {
                    executionLogger.warning(this.getClass(), "Unable to take screenshot before", null);
                }
            }
            payloadInstanceExecutionReport.setUpdatedPayload(session.execute(payload, executionLogger));
            payloadInstanceExecutionReport.setStatus(Status.SUCCESS);
        } catch (Exception e) {
            executionLogger.error(this.getClass(), e.getCause().toString(), e.getCause());
            payloadInstanceExecutionReport.setStatus(Status.FAILURE);
        } finally {
            if (debug) {
                try {
                    payloadInstanceExecutionReport.setScreenshotAfter(session.getScreenshot());
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }
            payloadInstanceExecutionReport.setDurationInMs(System.currentTimeMillis() - start);
            payloadInstanceExecutionReport.setLog(executionLogger.getLog());

        }
        return payloadInstanceExecutionReport;
    }

    @Override
    public ValidationResult validateBatch(SessionProxy session, List<RecordingStep<? extends Payload>> recordingStepList) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setSuccess(true);
        List<RecordingStep<? extends Payload>> validatedList = new ArrayList<>();
        List<PayloadInstanceExecutionReport> payloadInstanceExecutionReportList = new ArrayList<>();
        for (RecordingStep<? extends Payload> recordingStep : recordingStepList) {
            var payloadInstanceExecutionReport = execute(session, recordingStep.getPayload());
            payloadInstanceExecutionReportList.add(payloadInstanceExecutionReport);
            if (payloadInstanceExecutionReport.getStatus().equals(Status.SUCCESS)) {
                validatedList.add(recordingStep);
            } else {
                validationResult.setSuccess(false);
                validationResult.setPayloadInstanceExecutionReportList(payloadInstanceExecutionReportList);
                break;
            }
        }
        validationResult.setValidatedList(validatedList);
        validationResult.setPayloadInstanceExecutionReportList(payloadInstanceExecutionReportList);
        return validationResult;
    }
}
