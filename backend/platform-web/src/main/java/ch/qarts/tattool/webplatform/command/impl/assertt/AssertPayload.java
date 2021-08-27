package ch.qarts.tattool.webplatform.command.impl.assertt;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.descriptor.DataProvided;
import ch.qarts.tattool.core.domain.command.descriptor.Mappable;
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
public class AssertPayload extends Payload {
    @DataProvided
    @Renderer("ElementSuperLocatorSelector")
    private ElementSuperLocator elementSuperLocator;
    @DataProvided
    @Renderer("TextSelector")
    private Assert.AssertFunction assertFunction;
    @Mappable
    @Renderer("TextEditor")
    private String expression;

    @Override
    public String humanReadable() {
        return String.format("assert that element %s %s %s", assertFunction, expression, elementSuperLocator.getAsString());
    }
}
