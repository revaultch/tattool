package ch.qarts.tattool.repository;

import ch.qarts.tattool.repository.data.ExecutableRecordingInstanceExecutionReportDTO;
import ch.qarts.tattool.repository.data.PayloadInstanceExecutionReportDTO;
import ch.qarts.tattool.repository.data.RunDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class ExecutionReportCascadeSaveMongoEventListener extends AbstractMongoEventListener<Object> {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        if ((source instanceof RunDTO)) {
            for (ExecutableRecordingInstanceExecutionReportDTO item : ((RunDTO) source).getExecutableRecordingInstanceExecutionReportList()) {
                mongoOperations.save(item);
            }
        }
        if ((source instanceof ExecutableRecordingInstanceExecutionReportDTO)) {
            for (PayloadInstanceExecutionReportDTO item : ((ExecutableRecordingInstanceExecutionReportDTO) source).getPayloadInstanceExecutionReportList()) {
                mongoOperations.save(item);
            }
        }
    }

}
