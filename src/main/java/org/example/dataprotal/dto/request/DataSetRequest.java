package org.example.dataprotal.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataSetRequest {
    private Long categoryId;
    private Long internId;
    private String dataSetName;
    private String title;
    private String description;
    private boolean isOpened;
}


