package ch.qarts.tattool.core.domain.platform;

import ch.qarts.tattool.core.domain.command.descriptor.CommandDescriptor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Platform {

    private String id;

    private String description;

    private List<CommandDescriptor> availableCommands;

}
