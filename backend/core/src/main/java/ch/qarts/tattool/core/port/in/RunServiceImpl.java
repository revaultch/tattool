package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.run.Run;
import ch.qarts.tattool.core.domain.run.RunStatus;
import ch.qarts.tattool.core.port.out.RunRepositoryPort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RunServiceImpl implements RunService {

    private final RunRepositoryPort runRepository;

    @Override
    public Run getRun(String reference) {
        return runRepository.findById(reference);
    }

    @Override
    public List<Run> getRunning() {
        return runRepository.findAll();
    }

    @Override
    public Run save(Run run) {
        return runRepository.save(run);
    }

    @Override
    public List<Run> getRuns(RunStatus status, String recordingId, String testsetId) {
        return this.runRepository.findAll();
    }
}
