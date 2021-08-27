package ch.qarts.tattool.intg;

import ch.qarts.tattool.controller.RecordingController;
import ch.qarts.tattool.core.domain.dataset.Content;
import ch.qarts.tattool.core.domain.dataset.Dataset;
import ch.qarts.tattool.core.domain.element.Bounds;
import ch.qarts.tattool.core.domain.element.ElementLocator;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.domain.recording.Recording;
import ch.qarts.tattool.core.domain.recording.RecordingStep;
import ch.qarts.tattool.core.port.in.DatasetService;
import ch.qarts.tattool.core.port.in.RecordingService;
import ch.qarts.tattool.repository.RecordingRepository;
import ch.qarts.tattool.webplatform.command.impl.write.WritePayload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecordingIntgTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.6"));

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


    @Autowired
    private RecordingController recordingController;

    @Autowired
    private RecordingService recordingService;

    @Autowired
    private DatasetService datasetService;


    @Autowired
    private RecordingRepository repository;

    private Recording recording1;

    private Dataset dataset1;


    @BeforeEach
    public void setup() {
        this.repository.deleteAll();

        this.dataset1 = Dataset.builder()
                .id("someid")
                .name("name")
                .description("description")
                .content(new Content())
                .build();

        this.recording1 = Recording.builder()
                .name("some recording")
                .platform("some target")
                .description("xxx")
                .tags(Arrays.asList("a", "b", "cdef"))
                .dataset(dataset1)

                .recordingStepList(Collections.singletonList(
                        RecordingStep.builder()
                                .id("12L")
                                .mappings(new HashMap<>())
                                .payload(WritePayload.builder()
                                        .verb("write")
                                        .text("some text")
                                        .elementSuperLocator(ElementSuperLocator.builder()
                                                .id("jjj")
                                                .asString("xxyy")
                                                .screenshot("aaaa")
                                                .tags(Arrays.asList("hello", "world"))
                                                .locators(Arrays.asList(
                                                        ElementLocator.builder()
                                                                .query("xxx")
                                                                .type("aaa")

                                                                .build()
                                                        , ElementLocator.builder()
                                                                .query("xxx2")
                                                                .type("aaa2")

                                                                .build()

                                                ))
                                                .bounds(new Bounds())
                                                .build())
                                        .build())

                                .build()

                ))
                .build();


        datasetService.save(dataset1);

        recordingService.save(recording1);


    }


    @Test
    void basicPersistenceTest() {
        List<Recording> recordingList = recordingService.getAllRecordings();
        Assertions.assertEquals(1, recordingList.size());
        Recording actual = recordingList.get(0);
        recording1.setId(actual.getId());
        Assertions.assertEquals(recording1, recordingService.getAllRecordings().get(0));
    }

}
