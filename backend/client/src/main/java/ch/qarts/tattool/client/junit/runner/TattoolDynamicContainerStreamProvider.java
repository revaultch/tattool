package ch.qarts.tattool.client.junit.runner;

import ch.qarts.tattool.core.domain.execution.Status;
import ch.qarts.tattool.core.domain.execution.live.ExecutionEvent;
import ch.qarts.tattool.core.domain.execution.live.PayloadExecutionEvent;
import ch.qarts.tattool.core.domain.execution.live.RunTerminatedExecutionEvent;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicTest;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@AllArgsConstructor
public class TattoolDynamicContainerStreamProvider implements DynamicContainerStreamProvider {

    private final ExecutionEventFluxProvider executionEventFluxProvider;

    private final Consumer<RunTerminatedExecutionEvent> terminationHandler;

    private void logIfTerminated(ExecutionEvent executionEvent) {
        if (executionEvent instanceof RunTerminatedExecutionEvent) {
            terminationHandler.accept((RunTerminatedExecutionEvent) executionEvent);
        }
    }


    @Override
    public Stream<DynamicContainer> dynamicContainers() {
        return executionEventFluxProvider
                .executionEvents()
                .doOnNext(this::logIfTerminated)
                .takeUntil(executionEvent -> executionEvent instanceof RunTerminatedExecutionEvent)
                .filter(executionEvent -> executionEvent instanceof PayloadExecutionEvent)
                .map(executionEvent -> (PayloadExecutionEvent) executionEvent)
                .groupBy(payloadExecutionEvent -> String.format("%s (%s:%s)", payloadExecutionEvent.getRecordingDisplayName(), payloadExecutionEvent.getRecordingId(), payloadExecutionEvent.getRecordingInstanceId()))
                .map(groupedFluxItem ->
                        DynamicContainer.dynamicContainer(groupedFluxItem.key(),
                                groupedFluxItem
                                        .groupBy(payloadExecutionEvent -> Pair.of(payloadExecutionEvent.getDisplayName(), payloadExecutionEvent.getStatus()))
                                        // we want them to be unique (so uniqueness == terminal state)
                                        .filter(item -> Objects.requireNonNull(item.key()).getRight().equals(Status.FAILURE) || Objects.requireNonNull(item.key()).getRight().equals(Status.SUCCESS))
                                        .map(
                                                groupedPayloadEvent ->
                                                        DynamicTest.dynamicTest(Objects.requireNonNull(groupedPayloadEvent.key()).getLeft(), () -> {
                                                            assertNotEquals(Objects.requireNonNull(groupedPayloadEvent.key()).getRight(), Status.FAILURE, "check log for full report link");
                                                        })
                                        ).toStream())

                )
                .toStream();
    }

}
