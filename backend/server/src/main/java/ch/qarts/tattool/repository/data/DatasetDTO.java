package ch.qarts.tattool.repository.data;

import ch.qarts.tattool.core.domain.dataset.Content;
import ch.qarts.tattool.core.domain.dataset.Dataset;
import ch.qarts.tattool.repository.Mapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "Dataset")
public class DatasetDTO extends Mapper<DatasetDTO, Dataset> {

    @Id
    private String id;
    private String name;
    private String description;
    private Content content;


    @Override
    public DatasetDTO toDTO(Dataset dataset) {
        return
                dataset != null ?
                        DatasetDTO.builder()
                                .id(dataset.getId())
                                .name(dataset.getName())
                                .description(dataset.getDescription())
                                .content(dataset.getContent())
                                .build() : null;
    }

    @Override
    public Dataset toModel() {
        return
                Dataset.builder()
                        .id(this.getId())
                        .name(this.getName())
                        .description(this.getDescription())
                        .content(this.getContent())
                        .build();
    }


}
