package ch.qarts.tattool.client.junit.runner;

import ch.qarts.tattool.core.domain.execution.live.ExecutionEvent;
import reactor.core.publisher.Flux;

public interface ExecutionEventFluxProvider {

    Flux<ExecutionEvent> executionEvents();

}
