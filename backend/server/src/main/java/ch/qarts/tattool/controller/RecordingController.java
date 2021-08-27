package ch.qarts.tattool.controller;

import ch.qarts.tattool.core.domain.recording.Recording;
import ch.qarts.tattool.core.port.in.RecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
public class RecordingController {

    private final RecordingService recordingService;

    @Autowired
    public RecordingController(RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @PostMapping(value = "/v1/recordings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Recording saveRecording(@RequestBody Recording recording) {
        return this.recordingService.save(recording);
    }

    @GetMapping(value = "/v1/recordings/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Recording getRecording(@PathVariable("id") String id) {
        return recordingService.getRecording(id);
    }

    @GetMapping(value = "/v1/recordings", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Recording> getRecordings(@RequestParam(required = false, value = "tags") List<String> tags) {
        if (tags != null) {
            return recordingService.getRecordingsWithTags(tags);
        } else {
            return recordingService.getAllRecordings();
        }
    }

    @DeleteMapping(value = "/v1/recordings/{id}")
    public void delete(@PathVariable("id") String id) {
        recordingService.deleteRecording(id);
    }


}
