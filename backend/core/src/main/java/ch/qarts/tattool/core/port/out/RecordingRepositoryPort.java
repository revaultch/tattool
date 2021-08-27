package ch.qarts.tattool.core.port.out;

import ch.qarts.tattool.core.domain.recording.Recording;

import java.util.List;

public interface RecordingRepositoryPort {
    Recording save(Recording recording);

    Recording getOne(String id);

    List<Recording> findAll();

    List<Recording> withTags(List<String> tags);

    void delete(String id);
}
