package ch.qarts.tattool.core.domain.element;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Bounds implements Serializable {
    private double x;
    private double y;
    private double height;
    private double width;

    public double getSize() {
        return height * width;
    }
}
