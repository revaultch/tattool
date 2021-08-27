package ch.qarts.tattool.repository.data;

import ch.qarts.tattool.core.domain.execution.Status;
import ch.qarts.tattool.core.domain.execution.reporting.ExecutableRecordingInstanceExecutionReport;
import ch.qarts.tattool.repository.Mapper;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Document("ExecutableRecordingInstanceExecutionReport")
public class ExecutableRecordingInstanceExecutionReportDTO extends Mapper<ExecutableRecordingInstanceExecutionReportDTO, ExecutableRecordingInstanceExecutionReport> {

    @Id
    private String id;
    private String platformId;
    private String parentRecordingId;
    private String parentRecordingName;
    private String executableRecordingInstanceId;
    private Long durationInMs;
    private Status status;
    private List<String> datasetRow;


    @DBRef
    private List<PayloadInstanceExecutionReportDTO> payloadInstanceExecutionReportList = new ArrayList<>();


    @Override
    public ExecutableRecordingInstanceExecutionReportDTO toDTO(ExecutableRecordingInstanceExecutionReport domain) {
        return ExecutableRecordingInstanceExecutionReportDTO.builder()
                .id(domain.getId())
                .platformId(domain.getPlatformId())
                .parentRecordingId(domain.getParentRecordingId())
                .parentRecordingName(domain.getParentRecordingName())
                .executableRecordingInstanceId(domain.getExecutableRecordingInstanceId())
                .durationInMs(domain.getDurationInMs())
                .status(domain.getStatus())
                .datasetRow(domain.getDatasetRow())
                .payloadInstanceExecutionReportList(domain.getPayloadInstanceExecutionReportList().stream().map(item -> new PayloadInstanceExecutionReportDTO().toDTO(item)).collect(Collectors.toList()))
                .build();
    }

    @Override
    public ExecutableRecordingInstanceExecutionReport toModel() {
        return ExecutableRecordingInstanceExecutionReport.builder()
                .id(this.getId())
                .platformId(this.getPlatformId())
                .parentRecordingId(this.getParentRecordingId())
                .parentRecordingName(this.getParentRecordingName())
                .executableRecordingInstanceId(this.getExecutableRecordingInstanceId())
                .durationInMs(this.getDurationInMs())
                .status(this.getStatus())
                .datasetRow(this.getDatasetRow())
                .payloadInstanceExecutionReportList(this.payloadInstanceExecutionReportList.stream().map(PayloadInstanceExecutionReportDTO::toModel).collect(Collectors.toList()))
                .build();
    }
}
