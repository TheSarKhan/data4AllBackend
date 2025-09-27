package org.example.dataprotal.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.response.integration.SectorResponse;
import org.example.dataprotal.dto.response.integration.ModelResponse;
import org.example.dataprotal.service.integration.AnalyticService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytic/sectors")
@RequiredArgsConstructor
@Tag(name = "Sector", description = "Endpoints for sector listing and associated models")
public class SectorController {

    private final AnalyticService service;

    @Operation(
            summary = "List all sectors",
            description = "Retrieves the list of all available sectors with their slugs and model names.",
            operationId = "listSectors",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SectorResponse> listSectors() {
        return service.getSectors();
    }

    @Operation(
            summary = "List models for a sector",
            description = "Retrieves all models for the given sector slug.",
            operationId = "listModelsBySector",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping(
            value = "/{sectorSlug}/models",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ModelResponse> listModels(
            @Parameter(description = "Sector slug", example = "energetika")
            @PathVariable String sectorSlug) {
        return service.getModels(sectorSlug);
    }
}
