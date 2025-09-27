package org.example.dataprotal.service.impl.integration;

import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.response.integration.*;
import org.example.dataprotal.service.integration.AnalyticService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticServiceImpl implements AnalyticService {

    private final WebClient webClient;

    private <T> Mono<T> mapErrors(WebClient.ResponseSpec spec, Class<T> bodyType) {
        return spec
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(new IllegalStateException(
                                        "Upstream error (" + r.statusCode().value() + "): " + body))))
                .bodyToMono(bodyType);
    }

    private <T> Mono<T> mapErrors(WebClient.ResponseSpec spec, ParameterizedTypeReference<T> typeRef) {
        return spec
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(new IllegalStateException(
                                        "Upstream error (" + r.statusCode().value() + "): " + body))))
                .bodyToMono(typeRef);
    }

    @Override
    public List<SectorResponse> getSectors() {
        return mapErrors(
                webClient.get()
                        .uri("/sectors")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve(),
                new ParameterizedTypeReference<List<SectorResponse>>() {
                })
                .block();
    }

    @Override
    public List<ModelResponse> getModels(String sectorSlug) {
        return mapErrors(
                webClient.get()
                        .uri("/sectors/{sector_slug}/models", sectorSlug)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve(),
                new ParameterizedTypeReference<List<ModelResponse>>() {
                })
                .block();
    }

    @Override
    public List<CategoryResponse> getCategories(String sectorSlug, String modelSlug) {
        Map<String, List<CategoryResponse>> wrapper =
                mapErrors(
                        webClient.get()
                                .uri("/sectors/{sector_slug}/models/{model_slug}/categories", sectorSlug, modelSlug)
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve(),
                        new ParameterizedTypeReference<Map<String, List<CategoryResponse>>>() {
                        }
                ).block();

        assert wrapper != null;
        return wrapper.get("categories");
    }

    @Override
    public CategoryReportResponse getCategoryReportInfo(String sectorSlug, String modelSlug, String categorySlug) {
        return mapErrors(
                webClient.get()
                        .uri("/sectors/{sector_slug}/models/{model_slug}/categories/{category_slug}/report",
                                sectorSlug, modelSlug, categorySlug)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve(),
                CategoryReportResponse.class)
                .block();
    }

    @Override
    public byte[] downloadCategoryReport(String sectorSlug, String modelSlug, String categorySlug) {
        return webClient.get()
                .uri("/sectors/{sector_slug}/models/{model_slug}/categories/{category_slug}/report/download",
                        sectorSlug, modelSlug, categorySlug)
                .accept(MediaType.APPLICATION_PDF)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class).defaultIfEmpty("")
                                .flatMap(body -> Mono.error(new IllegalStateException(
                                        "Download failed (" + r.statusCode().value() + "): " + body))))
                .bodyToMono(byte[].class)
                .block();
    }


    @Override
    public AnalyzedDataResponse getData(String sectorSlug,
                                        String modelSlug,
                                        String categorySlug,
                                        Integer startYear,
                                        Integer endYear) {
        return mapErrors(
                webClient.get()
                        .uri(uriBuilder -> {
                            var b = uriBuilder
                                    .path("/sectors/{sector_slug}/models/{model_slug}/data")
                                    .queryParam("category_slug", categorySlug);
                            if (startYear != null) b.queryParam("start_year", startYear);
                            if (endYear != null) b.queryParam("end_year", endYear);
                            return b.build(sectorSlug, modelSlug);
                        })
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve(),
                AnalyzedDataResponse.class)
                .block();
    }

    @Override
    public ModelReportResponse getModelReportInfo(String sectorSlug, String modelSlug) {
        return mapErrors(
                webClient.get()
                        .uri("/sectors/{sector_slug}/models/{model_slug}/report", sectorSlug, modelSlug)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve(),
                ModelReportResponse.class)
                .block();
    }

    @Override
    public byte[] downloadModelReport(String sectorSlug, String modelSlug) {
        return webClient.get()
                .uri("/sectors/{sector_slug}/models/{model_slug}/report/download", sectorSlug, modelSlug)
                .accept(MediaType.APPLICATION_PDF)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(new IllegalStateException(
                                        "Download failed (" + r.statusCode().value() + "): " + body))))
                .bodyToMono(byte[].class)
                .block();
    }
}
