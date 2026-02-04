package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.analytic.AnalyticRequest;
import org.example.dataprotal.dto.request.analytic.UpdatedAnalyticRequest;
import org.example.dataprotal.dto.response.analytic.AnalyticResponse;
import org.example.dataprotal.service.TitleAnalyticService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytic")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Analytic Controller", description = "Provides analytic-related information for admin dashboard")
@PreAuthorize("hasRole('ADMIN') ")
public class AnalyticController {
    private final TitleAnalyticService analyticService;

    @GetMapping
    @Operation(summary = "Get all analytic", description = "Returns all analytic")
    public ResponseEntity<List<AnalyticResponse>> getAll() {
        return ResponseEntity.ok(analyticService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get analytic by ID", description = "Returns analytic by ID")
    public ResponseEntity<AnalyticResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(analyticService.getById(id));
    }

    @GetMapping("/subtitle/{subTitleId}")
    @Operation(summary = "Get analytic by subtitle ID", description = "Returns analytic by subtitle ID")
    public ResponseEntity<List<AnalyticResponse>> getBySubTitle(@PathVariable Long subTitleId) {
        return ResponseEntity.ok(analyticService.getBySubTitleId(subTitleId));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create analytic", description = "Creates a new analytic")
    public ResponseEntity<AnalyticResponse> create(@ModelAttribute @Valid AnalyticRequest request) throws IOException {
        return new ResponseEntity<>(analyticService.save(request), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update analytic", description = "Updates an existing analytic")
    public ResponseEntity<AnalyticResponse> update(
            @PathVariable Long id,
            @RequestPart("data") UpdatedAnalyticRequest request,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage
    ) throws IOException {

        if (coverImage != null && !coverImage.isEmpty()) {
            request = new UpdatedAnalyticRequest(
                    request.name(),
                    coverImage,
                    request.subTitleId(),
                    request.isOpened(),
                    request.embedLinks()
            );
        }

        return ResponseEntity.ok(analyticService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete analytic", description = "Deletes an analytic by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        analyticService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAnalytics() throws IOException {
        ByteArrayInputStream in = analyticService.exportToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=analytics.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(in.readAllBytes());
    }
}
