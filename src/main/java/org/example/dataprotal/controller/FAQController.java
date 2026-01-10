package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.FAQRequest;
import org.example.dataprotal.dto.response.FAQResponse;
import org.example.dataprotal.service.FAQService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faq")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "FAQ Controller", description = "Provides faq question information")
@PreAuthorize("hasRole('ADMIN') ")
public class FAQController {
    private final FAQService faqService;

    @GetMapping
    @Operation(summary = "Get all faqs", description = "Returns all faqs")
    public ResponseEntity<List<FAQResponse>> getAll() {
        return ResponseEntity.ok(faqService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get faq by ID", description = "Returns faq by ID")
    public ResponseEntity<FAQResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create faq", description = "Creates a new faq")
    public ResponseEntity<FAQResponse> create(@RequestBody @Valid FAQRequest request) {
        return new ResponseEntity<>(faqService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update faq", description = "Updates an existing faq")
    public ResponseEntity<FAQResponse> update(@PathVariable Long id, @RequestBody @Valid FAQRequest request) {
        return ResponseEntity.ok(faqService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete faq", description = "Deletes a faq by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faqService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
