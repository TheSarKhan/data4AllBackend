package org.example.dataprotal.mapper.researchcard;

import org.example.dataprotal.dto.request.researchcard.ResearchSubTitleRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchCardResponse;
import org.example.dataprotal.dto.response.researchcard.ResearchSubTitleResponse;
import org.example.dataprotal.model.researchcard.ResearchCard;
import org.example.dataprotal.model.researchcard.ResearchSubTitle;
import org.example.dataprotal.model.researchcard.ResearchTitle;

import java.util.List;

public class ResearchSubTitleMapper {

    public static ResearchSubTitleResponse toResponse(ResearchSubTitle subTitle) {
        return new ResearchSubTitleResponse(
                subTitle.getId(),
                subTitle.getName(),
                toCardResponse(subTitle.getResearchCards())
        );
    }

    private static List<ResearchCardResponse> toCardResponse(List<ResearchCard> researchCards) {
        return researchCards.stream().map(ResearchCardMapper::toResponse).toList();
    }

    public static ResearchSubTitle toEntity(ResearchSubTitleRequest subTitleRequest) {
        return ResearchSubTitle.builder()
                .name(subTitleRequest.name())
                .build();
    }
}
