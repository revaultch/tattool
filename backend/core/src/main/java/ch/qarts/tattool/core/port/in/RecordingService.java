package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.recording.Recording;

import java.util.List;

public interface RecordingService extends ExecutableBuilder {

    Recording save(Recording recording);

    Recording getRecording(String id);

    List<Recording> getAllRecordings();

    List<Recording> getRecordingsWithTags(List<String> tags);

    void deleteRecording(String id);

}
