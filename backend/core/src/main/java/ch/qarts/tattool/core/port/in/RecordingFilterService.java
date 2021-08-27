package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;

import java.util.List;

public interface RecordingFilterService extends ExecutableBuilder {

    List<RecordingFilter> getRecordingFilters();

    RecordingFilter getRecordingFilter(String id);

    RecordingFilter save(RecordingFilter recordingFilter);

    void delete(String id);
}
