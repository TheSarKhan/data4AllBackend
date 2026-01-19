package org.example.dataprotal.dto.response.researchcard;

import java.util.List;

public record ResearchSubTitleResponse(Long id, String name, List<ResearchCardResponse> researchCards) {
}
