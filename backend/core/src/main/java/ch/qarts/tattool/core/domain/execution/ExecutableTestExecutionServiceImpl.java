package ch.qarts.tattool.core.domain.execution;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.execution.live.PayloadExecutionEvent;
import ch.qarts.tattool.core.domain.execution.live.ProgressListener;
import ch.qarts.tattool.core.domain.execution.reporting.ExecutableRecordingInstanceExecutionReport;
import ch.qarts.tattool.core.domain.platform.PlatformProxy;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.domain.session.SessionProxy;
import ch.qarts.tattool.core.domain.transpilation.ExecutableRecordingInstance;
import ch.qarts.tattool.core.port.in.PayloadExecutionService;
import ch.qarts.tattool.core.port.in.PlatformService;
import ch.qarts.tattool.core.util.RetryUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@AllArgsConstructor
public class ExecutableTestExecutionServiceImpl implements ExecutableTestExecutionService {

    private final PlatformService platformService;
    private final PayloadExecutionService payloadExecutionService;

    @Override
    public CompletableFuture<ExecutableRecordingInstanceExecutionReport> execute(final ExecutableRecordingInstance executableRecordingInstance, final SessionConfiguration sessionConfiguration, ProgressListener progressListener) {
        return CompletableFuture.supplyAsync(() -> {
            ExecutableRecordingInstanceExecutionReport executableTestExecutionReport = new ExecutableRecordingInstanceExecutionReport();
            executableTestExecutionReport.setId(UUID.randomUUID().toString());
            executableTestExecutionReport.setParentRecordingId(executableRecordingInstance.getParentRecordingId());
            executableTestExecutionReport.setParentRecordingName(executableRecordingInstance.getName());
            executableTestExecutionReport.setExecutableRecordingInstanceId(executableRecordingInstance.getId());
            executableTestExecutionReport.setDatasetRow(executableRecordingInstance.getDatasetRow());
            final PlatformProxy platform = platformService.getById(executableRecordingInstance.getPlatform());
            SessionProxy session = null;
            try {
                session = RetryUtils.retry(() -> platform.createSession(sessionConfiguration), sessionConfiguration.getSessionCreationRetries(), sessionConfiguration.getSessionCreationRetryDelayInSeconds() * 1000L);
                executableTestExecutionReport.setStatus(Status.RUNNING);
                executableTestExecutionReport.setPlatformId(platform.getId());
                long startTest = System.currentTimeMillis();
                for (Payload payload : executableRecordingInstance.getPayloadList()) {
                    var uuid = UUID.randomUUID().toString();
                    progressListener.onProgress(PayloadExecutionEvent.builder()
                            .platformId(executableRecordingInstance.getPlatform())
                            .recordingId(executableRecordingInstance.getParentRecordingId())
                            .recordingInstanceId(executableRecordingInstance.getId())
                            .recordingDisplayName(executableRecordingInstance.getName())
                            .datasetRow(executableRecordingInstance.getDatasetRow())
                            .id(uuid)
                            .payload(payload)
                            .displayName(payload.humanReadable())
                            .status(Status.RUNNING)
                            .build());

                    long startPayload = System.currentTimeMillis();
                    var payloadInstanceExecutionReport = payloadExecutionService.execute(session, payload);

                    executableTestExecutionReport.addPayloadInstanceExecutionReport(payloadInstanceExecutionReport);
                    progressListener.onProgress(PayloadExecutionEvent.builder()
                            .durationInMs(System.currentTimeMillis() - startPayload)
                            .platformId(executableRecordingInstance.getPlatform())
                            .recordingId(executableRecordingInstance.getParentRecordingId())
                            .recordingInstanceId(executableRecordingInstance.getId())
                            .recordingDisplayName(executableRecordingInstance.getName())
                            .datasetRow(executableRecordingInstance.getDatasetRow())
                            .id(uuid)
                            .payload(payloadInstanceExecutionReport.getUpdatedPayload() != null ? payloadInstanceExecutionReport.getUpdatedPayload() : payloadInstanceExecutionReport.getPayload())
                            .displayName(payloadInstanceExecutionReport.getHumanReadable())
                            .status(payloadInstanceExecutionReport.getStatus())
                            .build());
                }
                executableTestExecutionReport.setStatus(executableTestExecutionReport.getPayloadInstanceExecutionReportList().stream().anyMatch(r -> r.getStatus().equals(Status.FAILURE)) ? Status.FAILURE : Status.SUCCESS);
                executableTestExecutionReport.setDurationInMs(System.currentTimeMillis() - startTest);
            } catch (Throwable t) {
                executableTestExecutionReport.setStatus(Status.FAILURE);
                progressListener.onProgress(PayloadExecutionEvent.builder()
                        .recordingId(executableRecordingInstance.getParentRecordingId())
                        .recordingInstanceId(executableRecordingInstance.getId())
                        .recordingDisplayName(executableRecordingInstance.getName())
                        .id(executableRecordingInstance.getId())
                        .displayName(t.getMessage())
                        .status(Status.FAILURE)
                        .build());
            } finally {
                if (session != null) {
                    platform.destroySession(session.getId());
                }
            }
            return executableTestExecutionReport;
        });
    }


}
