package org.example.dataprotal.mapper.researchcard;

import org.example.dataprotal.dto.request.researchcard.ResearchTitleRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchSubTitleResponse;
import org.example.dataprotal.dto.response.researchcard.ResearchTitleResponse;
import org.example.dataprotal.model.researchcard.ResearchSubTitle;
import org.example.dataprotal.model.researchcard.ResearchTitle;

import java.util.List;

public class ResearchTitleMapper {

    public static ResearchTitleResponse toResponse(ResearchTitle title) {
        return new ResearchTitleResponse(
                title.getId(),
                title.getName(),
                toSubTitleResponse(title.getSubTitles())
        );
    }

    private static List<ResearchSubTitleResponse> toSubTitleResponse(List<ResearchSubTitle> subTitles) {
        return subTitles.stream().map(ResearchSubTitleMapper::toResponse).toList();
    }

    public static ResearchTitle toEntity(ResearchTitleRequest titleRequest) {
        return ResearchTitle.builder()
                .name(titleRequest.name())
                .build();
    }
}
