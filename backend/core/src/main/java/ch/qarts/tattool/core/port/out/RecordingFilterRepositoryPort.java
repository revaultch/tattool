package ch.qarts.tattool.core.port.out;

import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;

import java.util.List;

public interface RecordingFilterRepositoryPort {
    List<RecordingFilter> findAll();

    RecordingFilter getOne(String id);

    void deleteById(String id);

    RecordingFilter save(RecordingFilter recordingFilter);
}
