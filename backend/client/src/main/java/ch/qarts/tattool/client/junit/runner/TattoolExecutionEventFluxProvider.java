package ch.qarts.tattool.client.junit.runner;

import ch.qarts.tattool.core.domain.execution.ExecutionRequest;
import ch.qarts.tattool.core.domain.execution.live.ExecutionEvent;
import ch.qarts.tattool.core.domain.run.Type;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class TattoolExecutionEventFluxProvider implements ExecutionEventFluxProvider {

    @NonNull
    private final String url;

    private final SessionConfiguration sessionConfiguration;

    @NonNull
    private final String reference;

    @NonNull
    private final Type type;


    @Override
    public Flux<ExecutionEvent> executionEvents() {

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector())
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();

        ParameterizedTypeReference<ServerSentEvent<ExecutionEvent>> sseType
                = new ParameterizedTypeReference<>() {
        };

        ExecutionRequest executionRequest = ExecutionRequest.builder()
                .id(reference)
                .type(type)
                .sessionConfiguration(sessionConfiguration)
                .build();

        return webClient.post()
                .uri(String.format("%s/api/v1/recordings/execution", url))
                .body(BodyInserters.fromValue(executionRequest))
                .retrieve()
                .bodyToFlux(sseType)
                .map(ServerSentEvent::data);
    }
}
