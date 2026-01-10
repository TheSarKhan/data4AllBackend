package org.example.dataprotal.dto.response.analytic;

import org.example.dataprotal.dto.response.EmbedLinkResponse;

import java.util.List;

public record AnalyticResponse(Long id, String name, String coverImage,
                               Long subTitleId, String subTitleName, List<EmbedLinkResponse> embedLinks) {
}
