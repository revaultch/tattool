package ch.qarts.tattool.adapter;

import ch.qarts.tattool.core.domain.run.Run;
import ch.qarts.tattool.core.port.out.RunRepositoryPort;
import ch.qarts.tattool.repository.RunRepository;
import ch.qarts.tattool.repository.data.RunDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RunRepositoryPortAdapter implements RunRepositoryPort {

    private RunRepository repository;

    @Autowired
    public RunRepositoryPortAdapter(RunRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Run> findAll() {
        return this.repository.findAll().stream().map(RunDTO::toModel).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Run findById(String reference) {
        return this.repository.findById(reference).orElseThrow(ItemNotFoundException::new).toModel();
    }

    @Override
    @Transactional
    public Run save(Run run) {
        return this.repository.save(new RunDTO().toDTO(run)).toModel();
    }


}
