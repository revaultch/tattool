package ch.qarts.tattool.core.domain.dataset;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Dataset {
    private String id;
    private String name;
    private String description;
    private Content content;
}
