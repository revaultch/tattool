package ch.qarts.tattool.client.junit.runner;

import ch.qarts.tattool.core.domain.execution.live.RunTerminatedExecutionEvent;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.stream.Stream;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public abstract class TattoolTestClientRunner {

    private final Tattool annotation = this.getClass().getAnnotation(Tattool.class);

    @TestFactory
    public Stream<DynamicContainer> tattoolClientTests() {
        return new TattoolDynamicContainerStreamProvider(new TattoolExecutionEventFluxProvider(annotation.apiUri(), new SessionConfiguration(), annotation.reference(), annotation.type()), this::handleTermination).dynamicContainers();
    }

    private void handleTermination(RunTerminatedExecutionEvent runTerminatedExecutionEvent) {
        log.info(String.format("Terminated test run - check %s/runs/%s for details", annotation.clientUri(), runTerminatedExecutionEvent.getRunId()));
    }

}
