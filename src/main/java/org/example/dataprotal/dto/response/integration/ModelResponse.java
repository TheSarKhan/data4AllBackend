package org.example.dataprotal.dto.response.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelResponse {

    private String name;
    private String slug;
    private String file;

    @JsonProperty("data_type")
    private String dataType;

    @JsonProperty("date_column")
    private String dateColumn;

    @JsonProperty("date_range")
    private List<Integer> dateRange;

    private List<String> categories;
}
