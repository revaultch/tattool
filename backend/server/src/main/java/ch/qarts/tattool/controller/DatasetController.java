package ch.qarts.tattool.controller;

import ch.qarts.tattool.core.domain.dataset.Dataset;
import ch.qarts.tattool.core.port.in.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class DatasetController {

    private final DatasetService datasetService;

    @Autowired
    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @PostMapping(value = "/v1/datasets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Dataset saveDataset(@RequestBody Dataset dataset) {
        return this.datasetService.save(dataset);
    }

    @GetMapping(value = "/v1/datasets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Dataset getDataset(@PathVariable("id") String id) {
        return datasetService.getDataset(id);
    }

    @DeleteMapping(value = "/v1/datasets/{id}")
    public void deleteDataset(@PathVariable("id") String id) {
        datasetService.delete(id);
    }

    @GetMapping(value = "/v1/datasets", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dataset> getDatasets() {
        return datasetService.getDatasets();
    }

}
