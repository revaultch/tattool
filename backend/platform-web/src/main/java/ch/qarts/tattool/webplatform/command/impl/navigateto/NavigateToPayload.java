package ch.qarts.tattool.webplatform.command.impl.navigateto;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.descriptor.Renderer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Getter
@NoArgsConstructor
public class NavigateToPayload extends Payload {
    @Renderer("TextEditor")
    private String url;

    @Override
    public String humanReadable() {
        return String.format("navigate to %s", url);
    }
}
