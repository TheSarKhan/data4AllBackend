package org.example.dataprotal.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UpdatedDatasetCategory(String name, String description, MultipartFile icon) {
}
