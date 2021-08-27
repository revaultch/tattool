package ch.qarts.tattool.webplatform.command.impl.select;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.descriptor.DataProvided;
import ch.qarts.tattool.core.domain.command.descriptor.Renderer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Data
@NoArgsConstructor
public class SelectPayload extends Payload {

    @DataProvided
    @Renderer("OptionSelector")
    private SelectedItem selection;

    @Override
    public String humanReadable() {
        return String.format("select %s from %s", selection, selection.getElementSuperLocator().getAsString());
    }
}
