package org.example.dataprotal.dto.request.analytic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record AnalyticRequest(@NotBlank String name, @NotNull MultipartFile coverImage,
                              @NotNull Long subTitleId, boolean isOpened, List<EmbedLinkRequest> embedLinks) {
}
