package org.example.dataprotal.dto.response.researchcard;

import java.util.List;

public record ResearchTitleResponse(Long id, String name, boolean isOpened,
                                    List<ResearchSubTitleResponse> subTitles) {
}
