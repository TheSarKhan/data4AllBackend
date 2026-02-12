package org.example.dataprotal.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatedDatasetRequest {
    private String author;
    private Long categoryId;
    private Long internId;
    private String dataSetName;
    private String title;
    private String description;
}
