package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.researchcard.ResearchCardRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchCardResponse;
import org.example.dataprotal.service.ResearchCardService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/research-card")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Research Card Controller", description = "Provides research card-related information for admin dashboard")
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

    @PostMapping("/{id}/increment-view")
    @Operation(summary = "Increment research card view", description = "Increments research card view")
    public ResponseEntity<Void> incrementView(@PathVariable Long id) {
        researchCardService.incrementView(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "Toggle research card like", description = "Increments or delete research card like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long id) throws AuthException {
        researchCardService.toggleLike(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName)
            throws MalformedURLException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(researchCardService.getFile(fileName));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create research card", description = "Creates a new research card")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResearchCardResponse> create(@ModelAttribute @Valid ResearchCardRequest request) throws IOException {
        return new ResponseEntity<>(researchCardService.save(request), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update research card", description = "Updates an existing research card")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResearchCardResponse> update(
            @PathVariable Long id,
            @ModelAttribute ResearchCardRequest request) throws IOException {
        return ResponseEntity.ok(researchCardService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete research card", description = "Deletes an research card by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        researchCardService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
