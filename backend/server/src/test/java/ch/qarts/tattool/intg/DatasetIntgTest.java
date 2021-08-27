package ch.qarts.tattool.intg;

import ch.qarts.tattool.controller.DatasetController;
import ch.qarts.tattool.core.domain.dataset.Content;
import ch.qarts.tattool.core.domain.dataset.Dataset;
import ch.qarts.tattool.core.port.in.DatasetService;
import ch.qarts.tattool.repository.DatasetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Disabled
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DatasetIntgTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.6"));


    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private DatasetController datasetController;

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private DatasetRepository repository;

    private Dataset dataset1;


    @BeforeEach
    public void setup() {
        this.repository.deleteAll();
        this.dataset1 = Dataset.builder()
                .name("somename")
                .description("somedescription")
                .content(new Content())
                .build();

        datasetService.save(dataset1);

    }

    @Test
    void persistenceTest() {
        var datasetList = datasetService.getDatasets();
        Assertions.assertEquals(1, datasetList.size());
        var actual = datasetList.get(0);
        dataset1.setId(actual.getId());
        Assertions.assertEquals(dataset1, datasetService.getDatasets().get(0));
    }


}
