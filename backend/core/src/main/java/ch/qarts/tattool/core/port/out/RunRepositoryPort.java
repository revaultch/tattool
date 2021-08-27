package ch.qarts.tattool.core.port.out;

import ch.qarts.tattool.core.domain.run.Run;

import java.util.List;

public interface RunRepositoryPort {
    
    List<Run> findAll();

    Run findById(String reference);

    Run save(Run run);

}
