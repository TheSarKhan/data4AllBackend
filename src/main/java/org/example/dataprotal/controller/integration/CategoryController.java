package org.example.dataprotal.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.response.integration.CategoryReportResponse;
import org.example.dataprotal.dto.response.integration.CategoryResponse;
import org.example.dataprotal.service.integration.AnalyticService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytic/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Endpoints for listing categories and retrieving category reports")
public class CategoryController {

    private final AnalyticService service;

    @Operation(
            summary = "List categories of a model",
            description = "Returns all available categories for the given sector and model.",
            operationId = "listCategories",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(
            value = "/sectors/{sectorSlug}/models/{modelSlug}/categories",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<CategoryResponse> listCategories(
            @Parameter(description = "Sector slug", example = "energetika")
            @PathVariable String sectorSlug,
            @Parameter(description = "Model slug", example = "enerji-techizati")
            @PathVariable String modelSlug) {

        return service.getCategories(sectorSlug, modelSlug);
    }

    @Operation(
            summary = "Get category report metadata",
            description = "Provides metadata about the generated report for a specific category within a model.",
            operationId = "getCategoryReportInfo",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(
            value = "/sectors/{sectorSlug}/models/{modelSlug}/categories/{categorySlug}/report",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CategoryReportResponse getCategoryReportInfo(
            @Parameter(description = "Sector slug", example = "energetika")
            @PathVariable String sectorSlug,
            @Parameter(description = "Model slug", example = "enerji-techizati")
            @PathVariable String modelSlug,
            @Parameter(description = "Category slug", example = "umumi-enerji-techizati")
            @PathVariable String categorySlug) {

        return service.getCategoryReportInfo(sectorSlug, modelSlug, categorySlug);
    }

    @Operation(
            summary = "Download category report (PDF)",
            description = "Downloads the generated PDF report for a given category.",
            operationId = "downloadCategoryReport",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(
            value = "/sectors/{sectorSlug}/models/{modelSlug}/categories/{categorySlug}/report/download",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> downloadCategoryReport(
            @Parameter(description = "Sector slug", example = "energetika")
            @PathVariable String sectorSlug,
            @Parameter(description = "Model slug", example = "enerji-techizati")
            @PathVariable String modelSlug,
            @Parameter(description = "Category slug", example = "umumi-enerji-techizati")
            @PathVariable String categorySlug) {

        byte[] pdf = service.downloadCategoryReport(sectorSlug, modelSlug, categorySlug);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"%s-%s-%s-report.pdf\""
                                .formatted(sectorSlug, modelSlug, categorySlug))
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
