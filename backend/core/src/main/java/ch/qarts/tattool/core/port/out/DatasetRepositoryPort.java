package ch.qarts.tattool.core.port.out;

import ch.qarts.tattool.core.domain.dataset.Dataset;

import java.util.List;


public interface DatasetRepositoryPort {
    List<Dataset> findAll();

    void deleteById(String id);

    Dataset getOne(String id);

    Dataset save(Dataset dataset);
}
