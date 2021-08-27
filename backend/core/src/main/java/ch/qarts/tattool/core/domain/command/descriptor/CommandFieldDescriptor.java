package ch.qarts.tattool.core.domain.command.descriptor;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CommandFieldDescriptor {

    private String name;

    private Map<String, Map<String, String>> annotations;

}
