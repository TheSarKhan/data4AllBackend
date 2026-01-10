package org.example.dataprotal.mapper.analytic;

import org.example.dataprotal.dto.request.analytic.SubTitleRequest;
import org.example.dataprotal.dto.response.analytic.AnalyticResponse;
import org.example.dataprotal.dto.response.analytic.SubTitleResponse;
import org.example.dataprotal.model.analytics.Analytic;
import org.example.dataprotal.model.analytics.SubTitle;
import org.example.dataprotal.model.analytics.Title;

import java.util.List;

public class SubTitleMapper {

    public static SubTitleResponse toResponse(SubTitle subTitle) {
        return new SubTitleResponse(
                subTitle.getId(),
                subTitle.getName(),
                subTitle.getTitle(),
                toAnalyticResponse(subTitle.getAnalytics())
        );
    }

    private static List<AnalyticResponse> toAnalyticResponse(List<Analytic> analytics) {
        return analytics.stream().map(AnalyticMapper::toResponse).toList();
    }

    public static SubTitle toEntity(SubTitleRequest subTitleRequest) {
        return SubTitle.builder()
                .name(subTitleRequest.name())
                .title(Title.builder().id(subTitleRequest.titleId()).build())
                .build();
    }
}
