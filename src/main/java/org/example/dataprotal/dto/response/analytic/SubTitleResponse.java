package org.example.dataprotal.dto.response.analytic;

import org.example.dataprotal.model.analytics.Analytic;
import org.example.dataprotal.model.analytics.Title;

import java.util.List;

public record SubTitleResponse(Long id, String name, Title title,
                               List<AnalyticResponse> analytics) {
}
