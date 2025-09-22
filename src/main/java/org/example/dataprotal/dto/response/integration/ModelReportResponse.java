package org.example.dataprotal.dto.response.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelReportResponse {

    private Boolean available;
    private String filename;

    @JsonProperty("display_name")
    private String displayName;
}
