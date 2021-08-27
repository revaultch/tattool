package ch.qarts.tattool.adapter;

import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;
import ch.qarts.tattool.core.port.out.RecordingFilterRepositoryPort;
import ch.qarts.tattool.repository.RecordingFiterRepository;
import ch.qarts.tattool.repository.data.RecordingFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordingFilterRepositoryPortAdapter implements RecordingFilterRepositoryPort {

    private RecordingFiterRepository repository;

    @Autowired
    public RecordingFilterRepositoryPortAdapter(RecordingFiterRepository jpaRepository) {
        this.repository = jpaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RecordingFilter> findAll() {
        return this.repository.findAll().stream().map(RecordingFilterDTO::toModel).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public RecordingFilter getOne(String id) {
        return this.repository.findById(id).orElseThrow(ItemNotFoundException::new).toModel();
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

    @Transactional
    @Override
    public RecordingFilter save(RecordingFilter recordingFilter) {
        return this.repository.save(new RecordingFilterDTO().toDTO(recordingFilter)).toModel();
    }
}
