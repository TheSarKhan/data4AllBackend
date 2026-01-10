package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.InternRequestDto;
import org.example.dataprotal.dto.response.InternResponseDto;
import org.example.dataprotal.service.InternService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/intern")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Interns Management Controller", description = "Provides intern-management-related information for admin dashboard")
@PreAuthorize("hasRole('ADMIN') ")
public class InternController {
    private final InternService internService;

    @PostMapping
    @Operation(summary = "Create intern", description = "Creates a new intern")
    public ResponseEntity<InternResponseDto> save(
            @RequestBody InternRequestDto internRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(internService.save(internRequestDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update intern", description = "Updates an existing intern")
    public ResponseEntity<InternResponseDto> update(
            @PathVariable Long id,
            @RequestBody InternRequestDto internRequestDto
    ) {
        return ResponseEntity.ok(internService.update(id, internRequestDto));
    }

    @GetMapping
    @Operation(summary = "Get all interns", description = "Returns all interns")
    public ResponseEntity<List<InternResponseDto>> getAll() {
        return ResponseEntity.ok(internService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get intern by ID", description = "Returns intern by ID")
    public ResponseEntity<InternResponseDto> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(internService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete intern", description = "Deletes an intern by ID")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        internService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
