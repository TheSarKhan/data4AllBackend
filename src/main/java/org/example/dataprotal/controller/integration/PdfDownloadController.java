package org.example.dataprotal.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.response.integration.ModelReportResponse;
import org.example.dataprotal.dto.response.integration.CategoryReportResponse;
import org.example.dataprotal.service.integration.AnalyticService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytic/pdf")
@RequiredArgsConstructor
@Tag(name = "PDF Download", description = "Endpoints for retrieving report metadata and downloading PDF reports")
public class PdfDownloadController {

    private final AnalyticService service;

    @Operation(
            summary = "Get model report metadata",
            description = "Returns information about the report generated for a given model.",
            operationId = "getModelReportInfo",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(
            value = "/sectors/{sectorSlug}/models/{modelSlug}/report",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ModelReportResponse getModelReportInfo(
            @Parameter(description = "Sector slug", example = "energetika")
            @PathVariable String sectorSlug,
            @Parameter(description = "Model slug", example = "enerji-techizati")
            @PathVariable String modelSlug) {

        return service.getModelReportInfo(sectorSlug, modelSlug);
    }

    @Operation(
            summary = "Download model report (PDF)",
            description = "Downloads the generated PDF report for a specific model.",
            operationId = "downloadModelReport",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(
            value = "/sectors/{sectorSlug}/models/{modelSlug}/report/download",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> downloadModelReport(
            @Parameter(description = "Sector slug", example = "energetika")
            @PathVariable String sectorSlug,
            @Parameter(description = "Model slug", example = "enerji-techizati")
            @PathVariable String modelSlug) {

        byte[] pdf = service.downloadModelReport(sectorSlug, modelSlug);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"%s-%s-report.pdf\"".formatted(sectorSlug, modelSlug))
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @Operation(
            summary = "Get category report metadata",
            description = "Returns information about the report generated for a specific category inside a model.",
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
            description = "Downloads the generated PDF report for a specific category inside a model.",
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
