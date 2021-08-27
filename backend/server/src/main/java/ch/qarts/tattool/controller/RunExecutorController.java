package ch.qarts.tattool.controller;

import ch.qarts.tattool.core.domain.execution.ExecutionRequest;
import ch.qarts.tattool.core.domain.execution.live.PayloadExecutionEvent;
import ch.qarts.tattool.core.domain.execution.live.ProgressListener;
import ch.qarts.tattool.core.domain.execution.live.RunTerminatedExecutionEvent;
import ch.qarts.tattool.core.domain.run.Type;
import ch.qarts.tattool.core.port.in.RunExecutorService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@CrossOrigin
public class RunExecutorController {


    private RunExecutorService runExecutorService;

    @Autowired
    public RunExecutorController(RunExecutorService runExecutorService) {
        this.runExecutorService = runExecutorService;
    }


    @PostMapping("/v1/recordings/execution")
    public SseEmitter execute(@RequestBody ExecutionRequest executionRequest) {

        SseEmitter sseEmitter = new SseEmitter((long) (60000 * 5));
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();

        var progressListener = new ProgressListener() {
            @SneakyThrows
            @Override
            public void onProgress(PayloadExecutionEvent payloadExecutionEvent) {
                sseEmitter.send(SseEmitter.event()
                        .data(
                                payloadExecutionEvent
                        )
                        .id(UUID.randomUUID().toString())
                        .name("payload"));
            }

            @SneakyThrows
            @Override
            public void onDone(RunTerminatedExecutionEvent runTerminatedExecutionEvent) {
                sseEmitter.send(SseEmitter.event()
                        .data(
                                runTerminatedExecutionEvent
                        )
                        .id(runTerminatedExecutionEvent.getRunId())
                        .name("done"));
            }
        };

        sseMvcExecutor.execute(() -> {
            try {
                if (executionRequest.getType().equals(Type.RECORDING)) {
                    runExecutorService.runRecording(executionRequest.getId(), executionRequest.getSessionConfiguration(), progressListener);
                } else if (executionRequest.getType().equals(Type.RECORDINGFILTER)) {
                    runExecutorService.runRecordingFilter(executionRequest.getId(), executionRequest.getSessionConfiguration(), progressListener);
                } else {
                    throw new IllegalStateException(String.format("Cannot handle execution request type %s", executionRequest.getType()));
                }
            } catch (Exception ex) {
                sseEmitter.completeWithError(ex);
            }
        });
        return sseEmitter;


    }


}
