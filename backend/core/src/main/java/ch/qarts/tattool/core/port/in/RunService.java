package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.run.Run;
import ch.qarts.tattool.core.domain.run.RunStatus;

import java.util.List;

public interface RunService {

    Run getRun(String reference);

    List<Run> getRunning();

    Run save(Run run);

    List<Run> getRuns(RunStatus status, String recordingFilterId, String recordingId);

}
