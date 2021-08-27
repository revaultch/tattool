package ch.qarts.tattool.core.domain.execution;

import ch.qarts.tattool.core.domain.run.Type;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionRequest {
    @NonNull
    private String id;
    @NonNull
    private Type type;
    private SessionConfiguration sessionConfiguration;
}
