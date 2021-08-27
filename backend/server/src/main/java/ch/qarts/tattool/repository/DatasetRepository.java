package ch.qarts.tattool.repository;

import ch.qarts.tattool.repository.data.DatasetDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasetRepository extends MongoRepository<DatasetDTO, String> {

}
