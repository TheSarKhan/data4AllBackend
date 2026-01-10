package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.analytic.SubTitleRequest;
import org.example.dataprotal.dto.response.analytic.SubTitleResponse;
import org.example.dataprotal.service.SubTitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytic-sub-title")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Analytic Subtitle Controller", description = "Provides analytic subtitle-related information for admin dashboard")
@PreAuthorize("hasRole('ADMIN') ")
public class SubTitleController {
    private final SubTitleService subTitleService;


    @GetMapping
    @Operation(summary = "Get all subtitles", description = "Returns all subtitles")
    public ResponseEntity<List<SubTitleResponse>> getAll() {
        return ResponseEntity.ok(subTitleService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subtitle by ID", description = "Returns subtitle by ID")
    public ResponseEntity<SubTitleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subTitleService.getById(id));
    }

    @GetMapping("/title/{titleId}")
    @Operation(summary = "Get subtitles by title ID", description = "Returns subtitles by title ID")
    public ResponseEntity<List<SubTitleResponse>> getByTitleId(@PathVariable Long titleId) {
        return ResponseEntity.ok(subTitleService.getByTitleId(titleId));
    }

    @PostMapping
    @Operation(summary = "Create subtitle", description = "Creates a new subtitle")
    public ResponseEntity<SubTitleResponse> create(@RequestBody SubTitleRequest request) {
        return new ResponseEntity<>(subTitleService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subtitle", description = "Updates an existing subtitle")
    public ResponseEntity<SubTitleResponse> update(@PathVariable Long id, @RequestBody SubTitleRequest request) {
        return ResponseEntity.ok(subTitleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subtitle", description = "Deletes a subtitle by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subTitleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
