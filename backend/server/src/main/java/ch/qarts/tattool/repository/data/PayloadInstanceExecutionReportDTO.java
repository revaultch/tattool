package ch.qarts.tattool.repository.data;

import ch.qarts.tattool.core.domain.execution.Status;
import ch.qarts.tattool.core.domain.execution.reporting.PayloadInstanceExecutionReport;
import ch.qarts.tattool.repository.Mapper;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Document("PayloadInstanceExecutionReport")
public class PayloadInstanceExecutionReportDTO extends Mapper<PayloadInstanceExecutionReportDTO, PayloadInstanceExecutionReport> {

    @Id
    private String id;

    private Object payload;

    private Object updatedPayload;

    private Status status;

    private String humanReadable;

    private String screenshotBefore;

    private String screenshotAfter;

    private long durationInMs;

    private List<String> log = new ArrayList<>();

    @Override
    public PayloadInstanceExecutionReportDTO toDTO(PayloadInstanceExecutionReport domain) {
        return PayloadInstanceExecutionReportDTO.builder()
                .id(domain.getId())
                .payload(domain.getPayload())
                .updatedPayload(domain.getUpdatedPayload())
                .status(domain.getStatus())
                .humanReadable(domain.getHumanReadable())
                .screenshotBefore(domain.getScreenshotBefore())
                .screenshotAfter(domain.getScreenshotAfter())
                .durationInMs(domain.getDurationInMs())
                .log(domain.getLog())
                .build();
    }

    @Override
    public PayloadInstanceExecutionReport toModel() {
        return PayloadInstanceExecutionReport.builder()
                .id(this.getId())
                .payload(this.getPayload())
                .updatedPayload(this.getUpdatedPayload())
                .status(this.getStatus())
                .humanReadable(this.getHumanReadable())
                .screenshotBefore(this.getScreenshotBefore())
                .screenshotAfter(this.getScreenshotAfter())
                .durationInMs(this.getDurationInMs())
                .log(this.getLog())
                .build();
    }
}



