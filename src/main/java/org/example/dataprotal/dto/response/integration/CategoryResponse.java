package org.example.dataprotal.dto.response.integration;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private String name;
    private String slug;
}
