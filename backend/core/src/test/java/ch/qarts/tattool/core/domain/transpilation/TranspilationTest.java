package ch.qarts.tattool.core.domain.transpilation;

import ch.qarts.tattool.core.domain.dataset.Content;
import ch.qarts.tattool.core.domain.dataset.Dataset;
import ch.qarts.tattool.core.domain.element.Bounds;
import ch.qarts.tattool.core.domain.element.ElementLocator;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.domain.recording.Recording;
import ch.qarts.tattool.core.domain.recording.RecordingStep;
import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;
import ch.qarts.tattool.core.port.out.RecordingRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TranspilationTest {

    @Mock
    private RecordingRepositoryPort recordingRepositoryPort;

    private TranspilationService transpilationService;

    private Dataset defaultDatasetDTO = Dataset.builder()
            .content(Content.builder()
                    .columns(Arrays.asList("col1", "col2"))
                    .rows(Arrays.asList(Arrays.asList("val1", "val2"), Arrays.asList("val21", "val22")))
                    .build())
            .build();


    @BeforeEach
    public void setup() {
        this.recordingRepositoryPort = Mockito.mock(RecordingRepositoryPort.class);
        this.transpilationService = new TranspilationServiceImpl(this.recordingRepositoryPort);
    }


    @Test
    public void shouldNotTranspileWhenNoDataset() {
        Recording recording = Recording.builder()
                .dataset(null)
                .recordingStepList(Collections.emptyList())
                .build();
        List<ExecutableRecordingInstance> testList = transpilationService.transpile(recording);
        assertEquals(1, testList.size());
    }


    @Test
    public void shouldTranspileWhenDataset() {

        Recording recording = Recording.builder()
                .dataset(defaultDatasetDTO)
                .recordingStepList(Collections.singletonList(
                        RecordingStep.builder()
                                .id("123L")
                                .payload(
                                        SomePayload.builder()
                                                .verb("some")
                                                .text("default")
                                                .elementSuperLocator(ElementSuperLocator.builder()
                                                        .bounds(Bounds.builder().build())
                                                        .locators(Collections.singletonList(ElementLocator.builder().build()))
                                                        .build())
                                                .build()

                                )
                                .mappings(Map.of("text", "col1"))
                                .build()

                ))
                .build();

        List<ExecutableRecordingInstance> testList = transpilationService.transpile(recording);
        assertEquals(2, testList.size());

        assertEquals(1, testList.get(0).getPayloadList().size());
        assertEquals(1, testList.get(1).getPayloadList().size());

        assertEquals("val1", ((SomePayload) testList.get(0).getPayloadList().get(0)).getText());
        assertEquals("val21", ((SomePayload) testList.get(1).getPayloadList().get(0)).getText());

    }

    @Test
    public void shouldTranspileMultipleCommands() {

        Recording recording = Recording.builder()
                .dataset(defaultDatasetDTO)
                .recordingStepList(Arrays.asList(
                        RecordingStep.builder()
                                .id("123L")
                                .payload(
                                        SomePayload.builder()
                                                .verb("some")
                                                .text("default")
                                                .build()
                                )
                                .mappings(Map.of("text", "col1"))
                                .build(),
                        RecordingStep.builder()
                                .id("124L")
                                .payload(
                                        SomePayload.builder()
                                                .verb("some")
                                                .text("default")
                                                .build()
                                )
                                .mappings(Map.of("text", "col1"))
                                .build()


                ))
                .build();

        List<ExecutableRecordingInstance> testList = transpilationService.transpile(recording);
        assertEquals(2, testList.size());
        assertEquals(2, testList.get(0).getPayloadList().size());
        assertEquals(2, testList.get(1).getPayloadList().size());
        assertEquals("val1", ((SomePayload) testList.get(0).getPayloadList().get(0)).getText());
        assertEquals("val1", ((SomePayload) testList.get(0).getPayloadList().get(1)).getText());
        assertEquals("val21", ((SomePayload) testList.get(1).getPayloadList().get(0)).getText());
        assertEquals("val21", ((SomePayload) testList.get(1).getPayloadList().get(1)).getText());


    }

    @Test
    public void shouldTranspileMultipleColumns() {
        Recording recording = Recording.builder()
                .dataset(defaultDatasetDTO)
                .recordingStepList(Collections.singletonList(

                        RecordingStep.builder()
                                .id("124L")
                                .payload(
                                        SomePayload.builder()
                                                .verb("some")
                                                .text("default")
                                                .build()
                                )
                                .mappings(Map.of("text", "col1", "value", "col2"))
                                .build()


                ))
                .build();

        List<ExecutableRecordingInstance> testList = transpilationService.transpile(recording);
        assertEquals(2, testList.size());
        assertEquals(1, testList.get(0).getPayloadList().size());
        assertEquals(1, testList.get(1).getPayloadList().size());
        assertEquals("val1", ((SomePayload) testList.get(0).getPayloadList().get(0)).getText());
        assertEquals("val2", ((SomePayload) testList.get(0).getPayloadList().get(0)).getValue());
        assertEquals("val21", ((SomePayload) testList.get(1).getPayloadList().get(0)).getText());
        assertEquals("val22", ((SomePayload) testList.get(1).getPayloadList().get(0)).getValue());
    }

    @Test
    public void shouldTranspileRecordingFilter() {
        RecordingFilter recordingFilter = RecordingFilter.builder()
                .tags(Arrays.asList("tag1", "tag2"))
                .build();
        Mockito.when(this.recordingRepositoryPort.withTags(recordingFilter.getTags())).thenReturn(Arrays.asList(
                Recording.builder()
                        .recordingStepList(Collections.emptyList())
                        .build(),
                Recording.builder().recordingStepList(Collections.emptyList())
                        .build()
        ));
        var result = this.transpilationService.transpile(recordingFilter);
        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnPlatformIdWhenNoDataset() {
        Recording recording = Recording.builder().platform("hello").recordingStepList(Collections.emptyList()).build();
        var result = this.transpilationService.transpile(recording);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getPlatform());
    }

    @Test
    public void shouldReturnPlatformIdWhenDataset() {
        Recording recording = Recording.builder().platform("hello")
                .dataset(defaultDatasetDTO)

                .recordingStepList(Collections.singletonList(
                        RecordingStep.builder()
                                .id("123")
                                .mappings(new HashMap<>())
                                .payload(SomePayload.builder()
                                        .verb("some")
                                        .text("default")
                                        .build())
                                .build()

                ))
                .build();
        var result = this.transpilationService.transpile(recording);
        assertEquals(2, result.size());
        assertNotNull(result.get(0).getPlatform());
    }


}
