package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.dataset.Dataset;
import ch.qarts.tattool.core.port.out.DatasetRepositoryPort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DatasetServiceImpl implements DatasetService {

    private final DatasetRepositoryPort datasetRepository;

    public List<Dataset> getDatasets() {
        return datasetRepository.findAll();
    }

    public Dataset getDataset(String id) {
        return datasetRepository.getOne(id);
    }

    public Dataset save(Dataset dataset) {
        return this.datasetRepository.save(dataset);
    }

    public void delete(String id) {
        datasetRepository.deleteById(id);
    }


}
