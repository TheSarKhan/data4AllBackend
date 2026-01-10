package org.example.dataprotal.dto.response.researchcard;

import java.time.Instant;

public record ResearchCardResponse(Long id, String topic, String content,

                                   String fileUrl, Long subTitleId, Instant createdAt) {
}
