package org.example.dataprotal.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.response.integration.CategoryResponse;
import org.example.dataprotal.dto.response.integration.ModelResponse;
import org.example.dataprotal.service.integration.AnalyticService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytic/models")
@RequiredArgsConstructor
@Tag(name = "Model", description = "Endpoints for listing models and their categories")
public class ModelController {

    private final AnalyticService service;

    @Operation(
            summary = "List models of a sector",
            description = "Returns all models available for the given sector.",
            operationId = "listModels",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(
            value = "/sectors/{sectorSlug}/models",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ModelResponse> listModels(
            @Parameter(description = "Sector slug", example = "energetika")
            @PathVariable String sectorSlug) {

        return service.getModels(sectorSlug);
    }

    @Operation(
            summary = "List categories of a model",
            description = "Returns all categories available under a specific model within a sector.",
            operationId = "listModelCategories",
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
}
