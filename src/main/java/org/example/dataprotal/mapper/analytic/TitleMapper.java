package org.example.dataprotal.mapper.analytic;

import org.example.dataprotal.dto.request.analytic.TitleRequest;
import org.example.dataprotal.dto.response.analytic.SubTitleResponse;
import org.example.dataprotal.dto.response.analytic.TitleResponse;
import org.example.dataprotal.model.analytics.SubTitle;
import org.example.dataprotal.model.analytics.Title;

import java.util.List;

public class TitleMapper {

    public static TitleResponse toResponse(Title title) {
        return new TitleResponse(
                title.getId(),
                title.getName(),
                title.isOpened(),
                toSubTitleResponse(title.getSubTitles())
        );
    }

    private static List<SubTitleResponse> toSubTitleResponse(List<SubTitle> subTitles) {
        return subTitles.stream().map(SubTitleMapper::toResponse).toList();
    }

    public static Title toEntity(TitleRequest titleRequest) {
        return Title.builder()
                .name(titleRequest.name())
                .isOpened(titleRequest.isOpened())
                .build();
    }
}
