package ch.qarts.tattool.adapter;

import ch.qarts.tattool.core.domain.dataset.Dataset;
import ch.qarts.tattool.core.port.out.DatasetRepositoryPort;
import ch.qarts.tattool.repository.DatasetRepository;
import ch.qarts.tattool.repository.data.DatasetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatasetRepositoryPortAdapter implements DatasetRepositoryPort {

    private DatasetRepository repository;

    @Autowired
    public DatasetRepositoryPortAdapter(DatasetRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Dataset> findAll() {
        return this.repository.findAll().stream().map(DatasetDTO::toModel).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Dataset getOne(String id) {
        return this.repository.findById(id).orElseThrow(ItemNotFoundException::new).toModel();
    }

    @Transactional
    @Override
    public Dataset save(Dataset dataset) {
        return this.repository.save(new DatasetDTO().toDTO(dataset)).toModel();
    }
}
