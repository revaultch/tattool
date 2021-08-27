package ch.qarts.tattool;

import ch.qarts.tattool.core.domain.recording.Recording;
import ch.qarts.tattool.core.port.in.DatasetService;
import ch.qarts.tattool.core.port.in.RecordingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
@Profile("dev")
public class DevConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private RecordingService recordingService;

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private ObjectMapper objectMapper;


    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        var path = Paths.get(ClassLoader.getSystemResource("dev/recordings.json").toURI());
        List<Recording> recordingList = objectMapper.readerForListOf(Recording.class).readValue(Files.readString(path));
        for (Recording recording : recordingList) {
            if (recording.getDataset() != null) {
                datasetService.save(recording.getDataset());
            }
            recordingService.save(recording);
        }
    }
}