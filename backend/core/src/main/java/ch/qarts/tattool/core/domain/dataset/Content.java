package ch.qarts.tattool.core.domain.dataset;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Content {
    private List<String> columns = new ArrayList<>();
    private List<List<String>> rows = new ArrayList<>();

    public String getValue(String columnName, int rowIndex) {
        int colIndex = IntStream.range(0, columns.size())
                .filter(i -> columns.get(i).equals(columnName))
                .findFirst().orElseThrow(() -> new IllegalStateException(String.format("column %s not found", columnName)));
        return rows.get(rowIndex).get(colIndex);
    }
}
