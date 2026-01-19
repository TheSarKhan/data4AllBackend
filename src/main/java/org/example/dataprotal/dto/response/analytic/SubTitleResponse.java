package org.example.dataprotal.dto.response.analytic;

import java.util.List;

public record SubTitleResponse(Long id, String name,
                               List<AnalyticResponse> analytics) {
}
