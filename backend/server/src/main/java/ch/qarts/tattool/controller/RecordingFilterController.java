package ch.qarts.tattool.controller;

import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;
import ch.qarts.tattool.core.port.in.RecordingFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class RecordingFilterController {

    private final RecordingFilterService recordingFilterService;


    @Autowired
    public RecordingFilterController(RecordingFilterService recordingFilterService) {
        this.recordingFilterService = recordingFilterService;
    }

    @PostMapping(value = "/v1/recordingFilters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RecordingFilter saveRecordingFilter(@RequestBody RecordingFilter recordingFilter) {
        return this.recordingFilterService.save(recordingFilter);
    }

    @GetMapping(value = "/v1/recordingFilters/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RecordingFilter getRecordingFilter(@PathVariable("id") String id) {
        return recordingFilterService.getRecordingFilter(id);
    }

    @DeleteMapping(value = "/v1/recordingFilters/{id}")
    public void deleteRecordingFilter(@PathVariable("id") String id) {
        recordingFilterService.delete(id);
    }

    @GetMapping(value = "/v1/recordingFilters", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RecordingFilter> getRecordingFilters() {
        return recordingFilterService.getRecordingFilters();
    }

    @PostMapping(value = "/v1/recordingFilters/{id}/execution", produces = MediaType.APPLICATION_JSON_VALUE)
    public void execute(@PathVariable("id") String id) {
        recordingFilterService.buildExecutable(id, null, null);
    }


}
