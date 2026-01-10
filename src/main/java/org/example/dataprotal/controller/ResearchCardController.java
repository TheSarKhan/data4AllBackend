package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.researchcard.ResearchCardRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchCardResponse;
import org.example.dataprotal.service.ResearchCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/research-card")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Research Card Controller", description = "Provides research card-related information for admin dashboard")
@PreAuthorize("hasRole('ADMIN') ")
public class ResearchCardController {
    private final ResearchCardService researchCardService;

    @GetMapping
    public ResponseEntity<List<ResearchCardResponse>> getAll() {
        return ResponseEntity.ok(researchCardService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get research card by ID", description = "Returns research card by ID")
    public ResponseEntity<ResearchCardResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(researchCardService.getById(id));
    }

    @GetMapping("/subtitle/{subTitleId}")
    @Operation(summary = "Get research card by subtitle ID", description = "Returns research card by subtitle ID")
    public ResponseEntity<List<ResearchCardResponse>> getBySubTitle(@PathVariable Long subTitleId) {
        return ResponseEntity.ok(researchCardService.getBySubTitleId(subTitleId));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create research card", description = "Creates a new research card")
    public ResponseEntity<ResearchCardResponse> create(@ModelAttribute @Valid ResearchCardRequest request) throws IOException {
        return new ResponseEntity<>(researchCardService.save(request), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update research card", description = "Updates an existing research card")
    public ResponseEntity<ResearchCardResponse> update(
            @PathVariable Long id,
            @ModelAttribute ResearchCardRequest request) throws IOException {
        return ResponseEntity.ok(researchCardService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete research card", description = "Deletes an research card by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        researchCardService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
