package ch.qarts.tattool.webplatform.commandfactory;

import ch.qarts.tattool.core.domain.command.descriptor.CommandDescriptor;
import ch.qarts.tattool.webplatform.command.WebPlatformExecutableCommand;
import lombok.SneakyThrows;

import static ch.qarts.tattool.webplatform.commandfactory.CommandDescriptorMapper.getDescriptor;

public class CommandFactory {


    @SneakyThrows
    public static WebPlatformExecutableCommand<?> create(String verb) {
        CommandDescriptor commandDescriptor = getDescriptor(verb);
        return (WebPlatformExecutableCommand<?>) commandDescriptor.getCommandType().getDeclaredConstructor().newInstance();
    }

}
