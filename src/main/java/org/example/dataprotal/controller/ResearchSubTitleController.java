package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.researchcard.ResearchSubTitleRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchSubTitleResponse;
import org.example.dataprotal.service.ResearchSubTitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/research-sub-title")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Research Subtitle Controller", description = "Provides research subtitle-related information for admin dashboard")
@PreAuthorize("hasRole('ADMIN') ")
public class ResearchSubTitleController {
    private final ResearchSubTitleService researchSubTitleService;


    @GetMapping
    @Operation(summary = "Get all subtitles", description = "Returns all subtitles")
    public ResponseEntity<List<ResearchSubTitleResponse>> getAll() {
        return ResponseEntity.ok(researchSubTitleService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subtitle by ID", description = "Returns subtitle by ID")
    public ResponseEntity<ResearchSubTitleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(researchSubTitleService.getById(id));
    }

    @GetMapping("/title/{titleId}")
    @Operation(summary = "Get subtitles by title ID", description = "Returns subtitles by title ID")
    public ResponseEntity<List<ResearchSubTitleResponse>> getByTitleId(@PathVariable Long titleId) {
        return ResponseEntity.ok(researchSubTitleService.getByTitleId(titleId));
    }

    @PostMapping
    @Operation(summary = "Create subtitle", description = "Creates a new subtitle")
    public ResponseEntity<ResearchSubTitleResponse> create(@RequestBody ResearchSubTitleRequest request) {
        return new ResponseEntity<>(researchSubTitleService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subtitle", description = "Updates an existing subtitle")
    public ResponseEntity<ResearchSubTitleResponse> update(@PathVariable Long id, @RequestBody ResearchSubTitleRequest request) {
        return ResponseEntity.ok(researchSubTitleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subtitle", description = "Deletes a subtitle by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        researchSubTitleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
