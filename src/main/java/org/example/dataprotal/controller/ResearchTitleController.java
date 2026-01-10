package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.researchcard.ResearchTitleRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchTitleResponse;
import org.example.dataprotal.service.ResearchTitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/research-title")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Research Title Controller", description = "Provides research title-related information for admin dashboard")
@PreAuthorize("hasRole('ADMIN') ")
public class ResearchTitleController {
    private final ResearchTitleService researchTitleService;

    @GetMapping
    @Operation(summary = "Get all titles", description = "Returns all titles")
    public ResponseEntity<List<ResearchTitleResponse>> getAll() {
        return ResponseEntity.ok(researchTitleService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get title by ID", description = "Returns title by ID")
    public ResponseEntity<ResearchTitleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(researchTitleService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create title", description = "Creates a new title")
    public ResponseEntity<ResearchTitleResponse> create(@RequestBody ResearchTitleRequest request) {
        return new ResponseEntity<>(researchTitleService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update title", description = "Updates an existing title")
    public ResponseEntity<ResearchTitleResponse> update(@PathVariable Long id, @RequestBody ResearchTitleRequest request) {
        return ResponseEntity.ok(researchTitleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete title", description = "Deletes a title by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        researchTitleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
