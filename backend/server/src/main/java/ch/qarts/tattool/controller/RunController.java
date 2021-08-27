package ch.qarts.tattool.controller;

import ch.qarts.tattool.core.domain.run.Run;
import ch.qarts.tattool.core.domain.run.RunStatus;
import ch.qarts.tattool.core.port.in.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
public class RunController {

    private final RunService runService;

    @Autowired
    public RunController(RunService runService) {
        this.runService = runService;
    }

    @GetMapping(value = "/v1/runs/{reference}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Run getRun(@PathVariable("reference") String reference) {
        return runService.getRun(reference);
    }

    @GetMapping(value = "/v1/runs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Run> getRuns(@RequestParam(required = false, value = "status") RunStatus status,
                             @RequestParam(required = false, value = "recordingFilterId") String recordingFilterId,
                             @RequestParam(required = false, value = "recordingId") String recordingId) {
        return runService.getRuns(status, recordingFilterId, recordingId);
    }


}
