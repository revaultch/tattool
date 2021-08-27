package ch.qarts.tattool.core.domain.transpilation;

import ch.qarts.tattool.core.domain.recording.Recording;
import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;

import java.util.List;

public interface TranspilationService {

    List<ExecutableRecordingInstance> transpile(RecordingFilter recordingFilter);

    List<ExecutableRecordingInstance> transpile(Recording recording);

}
