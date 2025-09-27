package org.example.dataprotal.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.response.integration.AnalyzedDataResponse;
import org.example.dataprotal.service.integration.AnalyticService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytic/data-analysis")
@RequiredArgsConstructor
@Tag(name = "Data Analysis", description = "Endpoints for retrieving analyzed data from models and categories")
public class DataAnalysisController {

    private final AnalyticService service;

    @Operation(
            summary = "Get analyzed data",
            description = "Fetches analyzed data for a specific sector, model, and category within a given year range.",
            operationId = "getAnalyzedData",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(
            value = "/sectors/{sectorSlug}/models/{modelSlug}/data",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AnalyzedDataResponse getAnalyzedData(
            @Parameter(description = "Sector slug", example = "energetika")
            @PathVariable String sectorSlug,

            @Parameter(description = "Model slug", example = "enerji-techizati")
            @PathVariable String modelSlug,

            @Parameter(description = "Category slug", example = "umumi-enerji-techizati")
            @RequestParam("category_slug") String categorySlug,

            @Parameter(description = "Start year (between 1990 and 2025)", example = "2015")
            @RequestParam("start_year") Integer startYear,

            @Parameter(description = "End year (between 1990 and 2025)", example = "2020")
            @RequestParam("end_year") Integer endYear
    ) {
        return service.getData(sectorSlug, modelSlug, categorySlug, startYear, endYear);
    }
}
