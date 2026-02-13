package org.example.dataprotal.mapper.analytic;

import org.example.dataprotal.dto.request.analytic.SubTitleRequest;
import org.example.dataprotal.dto.response.analytic.SubTitleResponse;
import org.example.dataprotal.model.analytics.SubTitle;
import org.example.dataprotal.model.analytics.Title;

public class SubTitleMapper {

    public static SubTitleResponse toResponse(SubTitle subTitle) {
        return new SubTitleResponse(
                subTitle.getId(),
                subTitle.getName(),
                subTitle.isOpened(),
                null
        );
    }

    public static SubTitle toEntity(SubTitleRequest subTitleRequest) {
        return SubTitle.builder()
                .name(subTitleRequest.name())
                .isOpened(subTitleRequest.isOpened())
                .title(Title.builder().id(subTitleRequest.titleId()).build())
                .build();
    }
}