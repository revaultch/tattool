package ch.qarts.tattool.webplatform.commandfactory;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.descriptor.CommandDescriptor;
import ch.qarts.tattool.core.domain.command.descriptor.Metadata;
import ch.qarts.tattool.core.domain.command.descriptor.PayloadDescriptorUtil;
import ch.qarts.tattool.core.domain.command.descriptor.Renderer;
import ch.qarts.tattool.webplatform.command.WebPlatformExecutableCommand;

public class CommandDescriptorMapper {

    public static CommandDescriptor map(WebPlatformExecutableCommand<? extends Payload> command) {
        Metadata[] metadataList = command.getClass().getAnnotationsByType(Metadata.class);
        Renderer[] rendererList = command.getClass().getAnnotationsByType(Renderer.class);
        var commandDescriptor = new CommandDescriptor();
        if (metadataList.length == 1) {
            Metadata metadata = metadataList[0];
            commandDescriptor.setName(metadata.name());
            commandDescriptor.setDescription(metadata.description());
            commandDescriptor.setPayloadDescriptor(PayloadDescriptorUtil.payloadDescriptor(command.getPayloadType()));
            commandDescriptor.setCommandType(command.getClass());
            commandDescriptor.setPayloadType(command.getPayloadType());
        } else {
            throw new IllegalStateException("Invalid number of metadata annotations");
        }
        if (rendererList.length == 1) {
            commandDescriptor.setRenderer(rendererList[0].value());
        }
        return commandDescriptor;
    }


    public static CommandDescriptor getDescriptor(String verb) {
        return CommandListHolder.getAvailableCommands().stream().map(CommandDescriptorMapper::map).filter(c -> c.getName().equals(verb)).findFirst().orElseThrow(() -> new IllegalStateException(String.format("Command not found for verb %s", verb)));
    }


}
