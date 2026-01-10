package org.example.dataprotal.dto.response;

import org.example.dataprotal.model.enums.DataSetStatus;

import java.time.LocalDateTime;

public record DataSetResponse(
        Long id,
        String author,
        String dataSetName,
        String title,
        String description,
        String imageUrl,
        String fileUrl,
        DataSetStatus status,
        String categoryName,
        String internName,
        LocalDateTime createdAt
) {}

