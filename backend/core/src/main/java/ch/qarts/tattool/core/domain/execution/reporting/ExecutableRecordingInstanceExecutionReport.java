package ch.qarts.tattool.core.domain.execution.reporting;

import ch.qarts.tattool.core.domain.execution.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecutableRecordingInstanceExecutionReport {

    private String id;
    private String platformId;
    private String parentRecordingId;
    private String parentRecordingName;
    private String executableRecordingInstanceId;
    private Long durationInMs;
    private Status status;
    private List<String> datasetRow;
    private List<PayloadInstanceExecutionReport> payloadInstanceExecutionReportList = new ArrayList<>();

    public void addPayloadInstanceExecutionReport(PayloadInstanceExecutionReport payloadInstanceExecutionReport) {
        payloadInstanceExecutionReportList.add(payloadInstanceExecutionReport);
    }

}
