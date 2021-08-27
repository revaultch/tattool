package ch.qarts.tattool.webplatform.commandfactory;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.webplatform.command.WebPlatformExecutableCommand;
import ch.qarts.tattool.webplatform.command.impl.assertt.Assert;
import ch.qarts.tattool.webplatform.command.impl.click.Click;
import ch.qarts.tattool.webplatform.command.impl.navigateto.NavigateTo;
import ch.qarts.tattool.webplatform.command.impl.press.Press;
import ch.qarts.tattool.webplatform.command.impl.select.Select;
import ch.qarts.tattool.webplatform.command.impl.write.Write;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class CommandListHolder {


    // TODO !!!!
    @SneakyThrows
    public static List<WebPlatformExecutableCommand<?>> getAvailableCommands() {
        var result = new ArrayList<WebPlatformExecutableCommand<? extends Payload>>();
        /*
        for (PojoClass pojoClass : PojoClassFactory.enumerateClassesByExtendingType("ch.qarts.tattool.webtarget.command", WebTargetExecutableCommand.class, null)) {
            result.add((WebTargetExecutableCommand<?>) pojoClass.getClazz().getDeclaredConstructor().newInstance());
        }*/
        result.add(new Click());
        result.add(new NavigateTo());
        result.add(new Select());
        result.add(new Assert());
        result.add(new Write());
        result.add(new Press());
        return result;
    }

}
