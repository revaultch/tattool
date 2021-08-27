package ch.qarts.tattool.webplatform.command.impl.select;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SelectedItem implements Serializable {

    private String option;

    private ElementSuperLocator elementSuperLocator;

}
