package ch.qarts.tattool.core.domain.transpilation;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.descriptor.Mappable;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class SomePayload extends Payload {
    @Mappable
    private String text;

    @Mappable
    private String value;

    @Mappable
    private ElementSuperLocator elementSuperLocator;

    @Override
    public String humanReadable() {
        return "some payload";
    }
}