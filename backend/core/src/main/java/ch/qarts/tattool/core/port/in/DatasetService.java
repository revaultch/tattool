package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.dataset.Dataset;

import java.util.List;

public interface DatasetService {

    List<Dataset> getDatasets();

    Dataset getDataset(String id);

    Dataset save(Dataset dataset);

    void delete(String id);
}
