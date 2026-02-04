package org.example.dataprotal.dto.response;

import java.time.Instant;

public record LogResponse(
        String action,
        Long userId,
        String userFullName,
        Instant createdAt
) {
}
