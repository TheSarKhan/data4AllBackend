package org.example.dataprotal.dto.request;

public record DataSetRequest(
        String author,
        Long categoryId,
        Long internId,
        String dataSetName,
        String title,
        String description
) {
}

