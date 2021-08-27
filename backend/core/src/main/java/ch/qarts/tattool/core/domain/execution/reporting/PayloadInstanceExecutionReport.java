package ch.qarts.tattool.core.domain.execution.reporting;

import ch.qarts.tattool.core.domain.execution.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadInstanceExecutionReport {

    private String id;

    private Object payload;

    private Object updatedPayload;

    private Status status;

    private String humanReadable;

    private String screenshotBefore;

    private String screenshotAfter;

    private long durationInMs;

    private List<String> log = new ArrayList<>();

}
