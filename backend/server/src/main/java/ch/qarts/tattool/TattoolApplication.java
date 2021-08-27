package ch.qarts.tattool;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.execution.ExecutableTestExecutionService;
import ch.qarts.tattool.core.domain.execution.ExecutableTestExecutionServiceImpl;
import ch.qarts.tattool.core.domain.platform.PlatformProxy;
import ch.qarts.tattool.core.domain.transpilation.TranspilationService;
import ch.qarts.tattool.core.domain.transpilation.TranspilationServiceImpl;
import ch.qarts.tattool.core.port.in.*;
import ch.qarts.tattool.core.port.out.*;
import ch.qarts.tattool.platform.WebPlatformBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
@EnableMongoRepositories
public class TattoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(TattoolApplication.class, args);
    }

    @Bean
    public PlatformProxy getWebPlatform() {
        return new WebPlatformBean();
    }


    @Bean
    public PlatformService platformService(PlatformRegistryPort platformRegistryPort) {
        return new PlatformServiceImpl(platformRegistryPort);
    }

    @Bean
    public DatasetService datasetService(DatasetRepositoryPort datasetRepository) {
        return new DatasetServiceImpl(datasetRepository);
    }

    @Bean
    public PayloadExecutionService commandService() {
        return new PayloadExecutionServiceImpl();
    }

    @Bean
    public RecordingService recordingService(RecordingRepositoryPort recordingRepository, TranspilationService transpilationService, ExecutableTestExecutionService executableTestExecutionService) {
        return new RecordingServiceImpl(recordingRepository, transpilationService, executableTestExecutionService);
    }

    @Bean
    public RunService runService(RunRepositoryPort runRepository) {
        return new RunServiceImpl(runRepository);
    }

    @Bean
    public RecordingFilterService recordingFilterService(RecordingFilterRepositoryPort testsetRepository, TranspilationService transpilationService, ExecutableTestExecutionService executableTestExecutionService) {
        return new RecordingFilterServiceImpl(testsetRepository, transpilationService, executableTestExecutionService);
    }

    @Bean
    public TranspilationService transpilationService(RecordingRepositoryPort recordingRepositoryPort) {
        return new TranspilationServiceImpl(recordingRepositoryPort);
    }

    @Bean
    public ExecutableTestExecutionService executableTestExecutionService(PlatformService platformService, PayloadExecutionService payloadExecutionService) {
        return new ExecutableTestExecutionServiceImpl(platformService, payloadExecutionService);
    }

    @Bean
    public RunExecutorService runExecutorService(RunService runService, RecordingService recordingService, RecordingFilterService recordingFilterService, RunRepositoryPort runRepositoryPort) {
        return new RunExecutorServiceImpl(runService, recordingService, recordingFilterService, runRepositoryPort);
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder(PayloadJsonDeserializer payloadJsonDeserializer) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.deserializerByType(Payload.class, payloadJsonDeserializer);
        return builder;
    }


}
