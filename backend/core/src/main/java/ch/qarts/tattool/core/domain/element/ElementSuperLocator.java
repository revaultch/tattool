package ch.qarts.tattool.core.domain.element;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
// TODO - bounds does not fit model
public class ElementSuperLocator implements Serializable {
    private String id;
    private String screenshot;
    private String asString;
    private List<ElementLocator> locators = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private Bounds bounds = new Bounds();

    public Optional<ElementLocator> getElementLocatorByType(@NonNull String type) {
        return locators.stream().filter(l -> l.getType().equals(type)).findFirst();
    }

}
