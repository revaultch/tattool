package ch.qarts.tattool.repository;

import ch.qarts.tattool.repository.data.RecordingFilterDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordingFiterRepository extends MongoRepository<RecordingFilterDTO, String> {

}
