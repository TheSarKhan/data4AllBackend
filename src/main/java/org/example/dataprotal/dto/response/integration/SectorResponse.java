package org.example.dataprotal.dto.response.integration;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectorResponse {

    private String name;
    private String slug;
    private List<String> models;
}
