package ch.qarts.tattool.repository.data;

import ch.qarts.tattool.core.domain.run.Run;
import ch.qarts.tattool.core.domain.run.RunStatus;
import ch.qarts.tattool.core.domain.run.Type;
import ch.qarts.tattool.repository.Mapper;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Document("Run")
public class RunDTO extends Mapper<RunDTO, Run> {

    @Id
    private String id;
    private Type type;
    private String title;
    private RunStatus status;

    @DBRef
    private List<ExecutableRecordingInstanceExecutionReportDTO> executableRecordingInstanceExecutionReportList;

    @Override
    public RunDTO toDTO(Run domain) {
        return Objects.nonNull(domain) ? RunDTO.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .status(domain.getStatus())
                .type(domain.getType())
                .executableRecordingInstanceExecutionReportList(domain.getExecutableRecordingInstanceExecutionReportList().stream().map(item -> new ExecutableRecordingInstanceExecutionReportDTO().toDTO(item)).collect(Collectors.toList()))
                .build() : null;
    }

    @Override
    public Run toModel() {
        return Run.builder()
                .id(this.getId())
                .title(this.getTitle())
                .status(this.getStatus())
                .type(this.getType())
                .executableRecordingInstanceExecutionReportList(this.executableRecordingInstanceExecutionReportList.stream().map(ExecutableRecordingInstanceExecutionReportDTO::toModel).collect(Collectors.toList()))
                .build();

    }


}
