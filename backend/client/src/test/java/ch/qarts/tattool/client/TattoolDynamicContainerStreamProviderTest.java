package ch.qarts.tattool.client;

import ch.qarts.tattool.client.junit.runner.ExecutionEventFluxProvider;
import ch.qarts.tattool.client.junit.runner.TattoolDynamicContainerStreamProvider;
import ch.qarts.tattool.core.domain.execution.Status;
import ch.qarts.tattool.core.domain.execution.live.PayloadExecutionEvent;
import ch.qarts.tattool.core.domain.execution.live.RunTerminatedExecutionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.opentest4j.AssertionFailedError;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TattoolDynamicContainerStreamProviderTest {

    private final static String SOME_DYNAMICCONTAINER_ID = "SOME_DYNAMICCONTAINER_ID";
    private final static String SOME_DYNAMICTEST_ID = "SOME_DYNAMICTEST_ID";
    private final static String SOME_RECORDING_INSTANCE_ID = "SOME_RECORDING_INSTANCE_ID";

    private TattoolDynamicContainerStreamProvider tattoolDynamicContainerStreamProvider;

    @Mock
    private ExecutionEventFluxProvider executionEventFluxProvider;

    @BeforeEach
    public void setup() {
        this.executionEventFluxProvider = mock(ExecutionEventFluxProvider.class);
        this.tattoolDynamicContainerStreamProvider = new TattoolDynamicContainerStreamProvider(executionEventFluxProvider, (RunTerminatedExecutionEvent runTerminatedExecutionEvent) -> {
        });
    }


    private String buildContainerFullName(String name, String id, String recordingInstanceId) {
        return String.format("%s (%s:%s)", name, id, recordingInstanceId);
    }

    @Test
    public void shouldProperlyDispatchTests() {
        final String DY_ID_1 = String.format("%s_%s", SOME_DYNAMICCONTAINER_ID, "1");
        final String DT_ID_1 = String.format("%s_%s", SOME_DYNAMICTEST_ID, "1");
        final String DY_ID_2 = String.format("%s_%s", SOME_DYNAMICCONTAINER_ID, "2");
        final String DT_ID_2 = String.format("%s_%s", SOME_DYNAMICTEST_ID, "2");

        when(this.executionEventFluxProvider.executionEvents()).thenReturn(
                Flux.just(PayloadExecutionEvent.builder()
                                .recordingId(DY_ID_1)
                                .recordingDisplayName(DY_ID_1)
                                .id(DT_ID_1)
                                .displayName(DT_ID_1)
                                .status(Status.SUCCESS)
                                .recordingInstanceId(SOME_RECORDING_INSTANCE_ID)
                                .build()
                        , PayloadExecutionEvent.builder()
                                .recordingId(DY_ID_2)
                                .recordingDisplayName(DY_ID_2)
                                .id(DT_ID_2)
                                .displayName(DT_ID_2)
                                .recordingInstanceId(SOME_RECORDING_INSTANCE_ID)
                                .status(Status.FAILURE)
                                .build())
        );
        var dynamicContainerList = this.tattoolDynamicContainerStreamProvider.dynamicContainers().collect(Collectors.toList());
        assertEquals(2, dynamicContainerList.size());
        assertEquals(buildContainerFullName(DY_ID_1, DY_ID_1, SOME_RECORDING_INSTANCE_ID), dynamicContainerList.get(0).getDisplayName());
        assertEquals(DT_ID_1, dynamicContainerList.get(0).getChildren().collect(Collectors.toList()).get(0).getDisplayName());
        assertEquals(buildContainerFullName(DY_ID_2, DY_ID_2, SOME_RECORDING_INSTANCE_ID), dynamicContainerList.get(1).getDisplayName());
        assertEquals(DT_ID_2, dynamicContainerList.get(1).getChildren().collect(Collectors.toList()).get(0).getDisplayName());
    }

    @Test
    public void shouldMatchContainerName() {
        var SOME_DYNAMIC_CONTAINER_NAME = "SOME_CONTAINER_NAME";
        when(this.executionEventFluxProvider.executionEvents()).thenReturn(
                Flux.just(PayloadExecutionEvent.builder()
                        .recordingId(SOME_DYNAMICCONTAINER_ID)
                        .id(SOME_DYNAMICTEST_ID)
                        .recordingInstanceId(SOME_RECORDING_INSTANCE_ID)
                        .recordingDisplayName(SOME_DYNAMIC_CONTAINER_NAME)
                        .status(Status.RUNNING)
                        .build()

                )
        );
        var dynamicContainerList = this.tattoolDynamicContainerStreamProvider.dynamicContainers().collect(Collectors.toList());
        assertEquals(1, dynamicContainerList.size());
        assertEquals(buildContainerFullName(SOME_DYNAMIC_CONTAINER_NAME, SOME_DYNAMICCONTAINER_ID, SOME_RECORDING_INSTANCE_ID), dynamicContainerList.get(0).getDisplayName());

    }

    @Test
    public void shouldNotHaveDuplicateContainer() {
        when(this.executionEventFluxProvider.executionEvents()).thenReturn(
                Flux.just(PayloadExecutionEvent.builder()
                                .recordingId(SOME_DYNAMICCONTAINER_ID)
                                .recordingDisplayName(SOME_DYNAMICCONTAINER_ID)
                                .id(SOME_DYNAMICTEST_ID)
                                .displayName(SOME_DYNAMICTEST_ID)
                                .status(Status.RUNNING)
                                .build()
                        , PayloadExecutionEvent.builder()
                                .recordingId(SOME_DYNAMICCONTAINER_ID)
                                .recordingDisplayName(SOME_DYNAMICCONTAINER_ID)
                                .id(SOME_DYNAMICTEST_ID)
                                .displayName(SOME_DYNAMICTEST_ID)
                                .status(Status.RUNNING)
                                .build())
        );
        var dynamicContainerList = this.tattoolDynamicContainerStreamProvider.dynamicContainers().collect(Collectors.toList());
        assertEquals(1, dynamicContainerList.size());
    }

    @Test
    public void shouldNotHaveDuplicateTestOnSUCCESS() {
        when(this.executionEventFluxProvider.executionEvents()).thenReturn(
                Flux.just(PayloadExecutionEvent.builder()
                                .recordingId(SOME_DYNAMICCONTAINER_ID)
                                .recordingDisplayName(SOME_DYNAMICCONTAINER_ID)
                                .id(SOME_DYNAMICTEST_ID)
                                .displayName(SOME_DYNAMICTEST_ID)
                                .status(Status.RUNNING)
                                .build()
                        , PayloadExecutionEvent.builder()
                                .recordingId(SOME_DYNAMICCONTAINER_ID)
                                .recordingDisplayName(SOME_DYNAMICCONTAINER_ID)
                                .id(SOME_DYNAMICTEST_ID)
                                .displayName(SOME_DYNAMICTEST_ID)
                                .status(Status.SUCCESS)
                                .build())
        );
        var dynamicContainerList = this.tattoolDynamicContainerStreamProvider.dynamicContainers().collect(Collectors.toList());
        assertEquals(1, dynamicContainerList.size());
        assertEquals(1, (int) dynamicContainerList.get(0).getChildren().count());
    }

    @Test
    public void shouldNotHaveDuplicateTestOnFAILURE() {
        when(this.executionEventFluxProvider.executionEvents()).thenReturn(
                Flux.just(PayloadExecutionEvent.builder()
                                .recordingId(SOME_DYNAMICCONTAINER_ID)
                                .recordingDisplayName(SOME_DYNAMICCONTAINER_ID)
                                .id(SOME_DYNAMICTEST_ID)
                                .displayName(SOME_DYNAMICTEST_ID)
                                .status(Status.RUNNING)
                                .build()
                        , PayloadExecutionEvent.builder()
                                .recordingId(SOME_DYNAMICCONTAINER_ID)
                                .recordingDisplayName(SOME_DYNAMICCONTAINER_ID)
                                .id(SOME_DYNAMICTEST_ID)
                                .displayName(SOME_DYNAMICTEST_ID)
                                .status(Status.FAILURE)
                                .build())
        );
        var dynamicContainerList = this.tattoolDynamicContainerStreamProvider.dynamicContainers().collect(Collectors.toList());
        assertEquals(1, dynamicContainerList.size());
        assertEquals(1, (int) dynamicContainerList.get(0).getChildren().count());
    }


    @Test
    public void shouldFailOnFAILURE() {
        when(this.executionEventFluxProvider.executionEvents()).thenReturn(
                Flux.just(PayloadExecutionEvent.builder()
                        .recordingId(SOME_DYNAMICCONTAINER_ID)
                        .recordingDisplayName(SOME_DYNAMICCONTAINER_ID)
                        .id(SOME_DYNAMICTEST_ID)
                        .displayName(SOME_DYNAMICTEST_ID)
                        .status(Status.FAILURE)
                        .build())
        );
        var dynamicContainerList = this.tattoolDynamicContainerStreamProvider.dynamicContainers().collect(Collectors.toList());
        assertEquals(1, dynamicContainerList.size());
        var executable = ((DynamicTest) dynamicContainerList.get(0).getChildren().collect(Collectors.toList()).get(0)).getExecutable();
        assertThrows(AssertionFailedError.class, executable);
    }

    @Test
    public void shouldSucceedOnSuccess() {
        when(this.executionEventFluxProvider.executionEvents()).thenReturn(
                Flux.just(PayloadExecutionEvent.builder()
                        .recordingId(SOME_DYNAMICCONTAINER_ID)
                        .recordingDisplayName(SOME_DYNAMICCONTAINER_ID)
                        .id(SOME_DYNAMICTEST_ID)
                        .displayName(SOME_DYNAMICTEST_ID)
                        .status(Status.SUCCESS)
                        .build())
        );
        var dynamicContainerList = this.tattoolDynamicContainerStreamProvider.dynamicContainers().collect(Collectors.toList());
        assertEquals(1, dynamicContainerList.size());
        var executable = ((DynamicTest) dynamicContainerList.get(0).getChildren().collect(Collectors.toList()).get(0)).getExecutable();
        assertDoesNotThrow(executable);
    }


    @Test
    public void shouldStopOnTerminatedEvent() {
        final String DY_ID_1 = String.format("%s_%s", SOME_DYNAMICCONTAINER_ID, "1");
        final String DT_ID_1 = String.format("%s_%s", SOME_DYNAMICTEST_ID, "1");
        final String DY_ID_2 = String.format("%s_%s", SOME_DYNAMICCONTAINER_ID, "2");
        final String DT_ID_2 = String.format("%s_%s", SOME_DYNAMICTEST_ID, "2");

        when(this.executionEventFluxProvider.executionEvents()).thenReturn(
                Flux.just(PayloadExecutionEvent.builder()
                                .recordingId(DY_ID_1)
                                .recordingDisplayName(DY_ID_1)
                                .id(DT_ID_1)
                                .recordingInstanceId(SOME_RECORDING_INSTANCE_ID)
                                .displayName(DT_ID_1)
                                .status(Status.SUCCESS)
                                .build()
                        , RunTerminatedExecutionEvent.builder().build()
                        , PayloadExecutionEvent.builder()
                                .recordingId(DY_ID_2)
                                .recordingDisplayName(DY_ID_2)
                                .id(DT_ID_2)
                                .displayName(DT_ID_2)
                                .status(Status.FAILURE)
                                .build())
        );
        var dynamicContainerList = this.tattoolDynamicContainerStreamProvider.dynamicContainers().collect(Collectors.toList());
        assertEquals(1, dynamicContainerList.size());
        assertEquals(buildContainerFullName(DY_ID_1, DY_ID_1, SOME_RECORDING_INSTANCE_ID), dynamicContainerList.get(0).getDisplayName());
        assertEquals(DT_ID_1, dynamicContainerList.get(0).getChildren().collect(Collectors.toList()).get(0).getDisplayName());
    }

    @Disabled
    @Test
    public void shouldDisplayReportLinkWhenFinished() {
        throw new IllegalStateException("not implemented");
    }


}
