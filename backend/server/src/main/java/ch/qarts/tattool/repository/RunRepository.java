package ch.qarts.tattool.repository;

import ch.qarts.tattool.repository.data.RunDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunRepository extends MongoRepository<RunDTO, String>, RunRepositoryCustom {

}
