package ch.qarts.tattool.webplatform.command.impl.press;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.descriptor.DataProvided;
import ch.qarts.tattool.core.domain.command.descriptor.Mappable;
import ch.qarts.tattool.core.domain.command.descriptor.Prepend;
import ch.qarts.tattool.core.domain.command.descriptor.Renderer;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Data
@NoArgsConstructor
public class PressPayload extends Payload {
    @Mappable
    @DataProvided
    @Renderer("TextSelector")
    private String key;

    @DataProvided
    @Prepend("on element")
    @Renderer("ElementSuperLocatorSelector")
    private ElementSuperLocator elementSuperLocator;

    @Override
    public String humanReadable() {
        return String.format("press %s on element %s", key, elementSuperLocator.getAsString());
    }
}
