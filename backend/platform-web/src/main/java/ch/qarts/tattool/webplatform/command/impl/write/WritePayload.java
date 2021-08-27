package ch.qarts.tattool.webplatform.command.impl.write;

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
public class WritePayload extends Payload {
    @Mappable
    @Renderer("TextEditor")
    private String text;

    @Prepend("into")
    @Renderer("ElementSuperLocatorSelector")
    @DataProvided
    private ElementSuperLocator elementSuperLocator;

    @Override
    public String humanReadable() {
        return String.format("write %s into %s", text, elementSuperLocator.getAsString());
    }
}
