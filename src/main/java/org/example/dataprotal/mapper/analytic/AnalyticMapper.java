package org.example.dataprotal.mapper.analytic;

import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.analytic.AnalyticRequest;
import org.example.dataprotal.dto.request.analytic.UpdateEmbedLinkRequest;
import org.example.dataprotal.dto.request.analytic.UpdatedAnalyticRequest;
import org.example.dataprotal.dto.response.EmbedLinkResponse;
import org.example.dataprotal.dto.response.analytic.AnalyticResponse;
import org.example.dataprotal.model.analytics.Analytic;
import org.example.dataprotal.model.analytics.EmbedLink;

import java.util.Base64;
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
                .isOpened(request.isOpened())
                .build();

        List<EmbedLink> embedLinks = request.embedLinks().stream()
                .map(linkReq -> {
                    String decoded = safeDecode(linkReq.embedLink());

                    return EmbedLink.builder()
                            .embedLink(decoded)
                            .analytic(analytic)
                            .build();
                })
                .toList();

        analytic.setEmbedLinks(embedLinks);
        return analytic;
    }

    public static void updateAnalytic(
            Analytic analytic,
            UpdatedAnalyticRequest request
    ) {
        analytic.setName(request.name());
        analytic.setOpened(request.isOpened());

        Map<Long, EmbedLink> existingLinks = analytic.getEmbedLinks().stream()
                .filter(l -> l.getId() != null)
                .collect(Collectors.toMap(EmbedLink::getId, Function.identity()));

        for (UpdateEmbedLinkRequest req : request.embedLinks()) {
            String decoded = safeDecode(req.embedLink());

            if (req.id() != null && existingLinks.containsKey(req.id())) {
                existingLinks.get(req.id()).setEmbedLink(decoded);
            } else if (req.id() == null || req.id() == 0) {
                EmbedLink newLink = EmbedLink.builder()
                        .embedLink(decoded)
                        .analytic(analytic)
                        .build();
                analytic.getEmbedLinks().add(newLink);
            }
        }

    }

    private static String safeDecode(String value) {
        try {
            return new String(Base64.getDecoder().decode(value));
        } catch (IllegalArgumentException e) {
            return value;
        }
    }
}

