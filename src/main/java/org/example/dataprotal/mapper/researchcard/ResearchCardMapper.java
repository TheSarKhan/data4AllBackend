package org.example.dataprotal.mapper.researchcard;

import org.example.dataprotal.dto.request.researchcard.ResearchCardRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchCardResponse;
import org.example.dataprotal.model.researchcard.ResearchCard;
import org.example.dataprotal.model.researchcard.ResearchSubTitle;

import java.time.Instant;

public class ResearchCardMapper {

    public static ResearchCardResponse toResponse(ResearchCard researchCard) {
        return new ResearchCardResponse(
                researchCard.getId(),
                researchCard.getTopic(),
                researchCard.getContent(),
                researchCard.getFileUrl(),
                researchCard.getSubTitle().getId(),
                researchCard.getCreatedAt(),
                researchCard.getViewCount(),
                researchCard.getLikeCount()
        );
    }

    public static ResearchCard toEntity(ResearchCardRequest researchCardRequest) {
        return ResearchCard.builder()
                .topic(researchCardRequest.topic())
                .content(researchCardRequest.content())
                .subTitle(ResearchSubTitle.builder().id(researchCardRequest.subTitleId()).build())
                .createdAt(Instant.now())
                .build();
    }

    public static void updateResearchCard(ResearchCard researchCard, ResearchCardRequest researchCardRequest) {
        researchCard.setTopic(researchCardRequest.topic());
        researchCard.setContent(researchCardRequest.content());
        researchCard.setSubTitle(ResearchSubTitle.builder().id(researchCardRequest.subTitleId()).build());
    }
}
