package org.example.dataprotal.mapper.analytic;

import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.analytic.AnalyticRequest;
import org.example.dataprotal.dto.request.analytic.UpdateEmbedLinkRequest;
import org.example.dataprotal.dto.request.analytic.UpdatedAnalyticRequest;
import org.example.dataprotal.dto.response.EmbedLinkResponse;
import org.example.dataprotal.dto.response.analytic.AnalyticResponse;
import org.example.dataprotal.model.analytics.Analytic;
import org.example.dataprotal.model.analytics.EmbedLink;
import org.example.dataprotal.model.analytics.SubTitle;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AnalyticMapper {

    public static AnalyticResponse toResponse(Analytic analytic) {
        return new AnalyticResponse(
                analytic.getId(),
                analytic.getName(),
                analytic.getCoverImage(),
                analytic.getSubTitle().getId(),
                analytic.getSubTitle().getName(),
                analytic.isOpened(),
                toEmbedLinkResponse(analytic.getEmbedLinks())
        );
    }

    private static List<EmbedLinkResponse> toEmbedLinkResponse(List<EmbedLink> embedLinks) {
        return embedLinks.stream().map(embedLink -> new EmbedLinkResponse(
                embedLink.getId(),
                embedLink.getEmbedLink()
        )).toList();
    }

    public static Analytic toEntity(AnalyticRequest request) {

        Analytic analytic = Analytic.builder()
                .name(request.name())
                .subTitle(SubTitle.builder()
                        .id(request.subTitleId())
                        .build())
                .isOpened(request.isOpened())
                .build();

        List<EmbedLink> embedLinks = request.embedLinks().stream()
                .map(linkReq -> EmbedLink.builder()
                        .embedLink(linkReq.embedLink())
                        .analytic(analytic)
                        .build()
                )
                .toList();

        analytic.setEmbedLinks(embedLinks);
        return analytic;
    }

    public static void updateAnalytic(
            Analytic analytic,
            UpdatedAnalyticRequest request
    ) {
        analytic.setName(request.name());
        analytic.setSubTitle(
                SubTitle.builder()
                        .id(request.subTitleId())
                        .build()
        );
        analytic.setOpened(request.isOpened());

        Map<Long, EmbedLink> existingLinks = analytic.getEmbedLinks()
                .stream()
                .filter(link -> link.getId() != null)
                .collect(Collectors.toMap(
                        EmbedLink::getId,
                        Function.identity()
                ));

        for (UpdateEmbedLinkRequest req : request.embedLinks()) {

            if (req.id() != null && existingLinks.containsKey(req.id())) {
                EmbedLink existing = existingLinks.get(req.id());
                existing.setEmbedLink(req.embedLink());

            } else {
                EmbedLink newLink = EmbedLink.builder()
                        .embedLink(req.embedLink())
                        .analytic(analytic)
                        .build();
                analytic.getEmbedLinks().add(newLink);
            }
        }
    }
}

