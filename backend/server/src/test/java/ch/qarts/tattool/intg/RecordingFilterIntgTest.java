package ch.qarts.tattool.intg;

import ch.qarts.tattool.controller.RecordingFilterController;
import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;
import ch.qarts.tattool.core.port.in.RecordingFilterService;
import ch.qarts.tattool.repository.RecordingFiterRepository;
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
import java.util.List;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecordingFilterIntgTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.6"));

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private RecordingFilterController recordingFilterController;

    @Autowired
    private RecordingFilterService recordingFilterService;

    @Autowired
    private RecordingFiterRepository repository;

    private RecordingFilter recordingFilter1;

    @BeforeEach
    public void setup() {
        this.repository.deleteAll();
        this.recordingFilter1 = RecordingFilter.builder()
                .name("some name")
                .description("some description")
                .tags(Arrays.asList("some", "tags"))
                .build();


        recordingFilterService.save(recordingFilter1);


    }


    @Test
    void persistenceTest() {
        List<RecordingFilter> recordingFilterList = recordingFilterService.getRecordingFilters();
        Assertions.assertEquals(1, recordingFilterList.size());
        RecordingFilter actual = recordingFilterList.get(0);
        recordingFilter1.setId(actual.getId());
        Assertions.assertEquals(recordingFilter1, recordingFilterService.getRecordingFilters().get(0));
    }

}
