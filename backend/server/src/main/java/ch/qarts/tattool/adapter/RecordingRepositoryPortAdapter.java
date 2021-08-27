package ch.qarts.tattool.adapter;

import ch.qarts.tattool.core.domain.recording.Recording;
import ch.qarts.tattool.core.port.out.RecordingRepositoryPort;
import ch.qarts.tattool.repository.RecordingRepository;
import ch.qarts.tattool.repository.data.RecordingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordingRepositoryPortAdapter implements RecordingRepositoryPort {

    private RecordingRepository repository;

    @Autowired
    public RecordingRepositoryPortAdapter(RecordingRepository repository) {
        this.repository = repository;
    }


    @Transactional
    @Override
    public Recording save(Recording recording) {
        return this.repository.save(new RecordingDTO().toDTO(recording)).toModel();
    }

    @Transactional(readOnly = true)
    @Override
    public Recording getOne(String id) {
        return this.repository.findById(id).orElseThrow(ItemNotFoundException::new).toModel();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Recording> findAll() {
        return this.repository.findAll().stream().map(RecordingDTO::toModel).collect(Collectors.toList());
    }

    private boolean tagMatcher(List<String> recordingTags, List<String> criteriaTags) {
        return criteriaTags.stream().anyMatch(criteriaTag -> recordingTags.stream().anyMatch(recordingTag -> recordingTag.trim().equalsIgnoreCase(criteriaTag.trim())));
    }

    @Override
    public List<Recording> withTags(List<String> tags) {
        return findAll().stream().filter(recording -> tagMatcher(recording.getTags(), tags)).collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        this.repository.deleteById(id);
    }
}
