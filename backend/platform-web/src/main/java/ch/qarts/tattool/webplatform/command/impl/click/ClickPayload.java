package ch.qarts.tattool.webplatform.command.impl.click;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.descriptor.DataProvided;
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
public class ClickPayload extends Payload {
    @DataProvided
    @Renderer("ElementSuperLocatorSelector")
    private ElementSuperLocator elementSuperLocator;

    @Override
    public String humanReadable() {
        return String.format("click %s", elementSuperLocator.getAsString());
    }
}
