package ch.qarts.tattool.webplatform.analysis;

import lombok.*;

@AllArgsConstructor
@Builder
@Data
@ToString
@EqualsAndHashCode
public class BrowserGeometry {
    private Long pageWidth;
    private Long pageHeight;
    private Long windowWidth;
    private Long windowHeight;
}
