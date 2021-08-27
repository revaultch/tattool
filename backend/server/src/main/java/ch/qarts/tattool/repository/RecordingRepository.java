package ch.qarts.tattool.repository;

import ch.qarts.tattool.repository.data.RecordingDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordingRepository extends MongoRepository<RecordingDTO, String> {

}
