package org.example.dataprotal.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record DataSetCategoryRequest(
        String name, String description, MultipartFile icon, boolean isOpened
) {
}
