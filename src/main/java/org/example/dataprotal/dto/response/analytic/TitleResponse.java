package org.example.dataprotal.dto.response.analytic;

import org.example.dataprotal.model.analytics.SubTitle;

import java.util.List;

public record TitleResponse(Long id, String name,
                            List<SubTitleResponse> subTitles) {
}
