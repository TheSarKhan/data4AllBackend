package org.example.dataprotal.dto.response.researchcard;

import org.example.dataprotal.model.researchcard.ResearchTitle;

import java.util.List;

public record ResearchSubTitleResponse(Long id, String name,
                                       ResearchTitle title, List<ResearchCardResponse> researchCards) {
}
