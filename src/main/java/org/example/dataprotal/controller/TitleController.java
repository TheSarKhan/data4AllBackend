package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.analytic.TitleRequest;
import org.example.dataprotal.dto.response.analytic.TitleResponse;
import org.example.dataprotal.service.TitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytic-title")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Analytic Title Controller", description = "Provides analytic title-related information for admin dashboard")
@PreAuthorize("hasRole('ADMIN') ")
public class TitleController {
    private final TitleService titleService;

    @GetMapping
    @Operation(summary = "Get all titles", description = "Returns all titles")
    public ResponseEntity<List<TitleResponse>> getAll() {
        return ResponseEntity.ok(titleService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get title by ID", description = "Returns title by ID")
    public ResponseEntity<TitleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(titleService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create title", description = "Creates a new title")
    public ResponseEntity<TitleResponse> create(@RequestBody TitleRequest request) {
        return new ResponseEntity<>(titleService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update title", description = "Updates an existing title")
    public ResponseEntity<TitleResponse> update(@PathVariable Long id, @RequestBody TitleRequest request) {
        return ResponseEntity.ok(titleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete title", description = "Deletes a title by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        titleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
