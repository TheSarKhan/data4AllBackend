package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {


    private final FileService fileService;

    @GetMapping("/view/{filename}")
    @Operation(description = "Show file")
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) throws IOException {
        Resource file = fileService.getFile(filename);
        String contentType = Files.probeContentType(file.getFile().toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/download/{filename}")
    @Operation(description = "Download file")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        Resource file = fileService.getFile(filename);
        String contentType = Files.probeContentType(file.getFile().toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}


