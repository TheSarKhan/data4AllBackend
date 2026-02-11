package org.example.dataprotal.dto.request.researchcard;

import org.springframework.web.multipart.MultipartFile;

public record ResearchCardRequest(String topic, String content,

                                  MultipartFile multipartFile, Long subTitleId, boolean isOpened) {
}
