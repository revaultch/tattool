package ch.qarts.tattool.core.domain.command.descriptor;

import ch.qarts.tattool.core.domain.command.Payload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommandDescriptor {

    private String name;

    private String description;

    private String renderer;

    private List<CommandFieldDescriptor> payloadDescriptor;

    private Class<?> commandType;

    private Class<? extends Payload> payloadType;

}
