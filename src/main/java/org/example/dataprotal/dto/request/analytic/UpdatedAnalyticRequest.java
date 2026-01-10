package org.example.dataprotal.dto.request.analytic;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdatedAnalyticRequest(String name, MultipartFile coverImage,
                                     Long subTitleId, List<UpdateEmbedLinkRequest> embedLinks) {
}
