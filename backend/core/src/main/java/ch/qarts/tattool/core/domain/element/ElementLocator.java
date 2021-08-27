package ch.qarts.tattool.core.domain.element;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ElementLocator implements Serializable {
    private String type;
    private String query;
}
