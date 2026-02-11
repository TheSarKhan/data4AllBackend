package org.example.dataprotal.dto.response.analytic;

import java.util.List;

public record TitleResponse(Long id, String name, boolean isOpened,
                            List<SubTitleResponse> subTitles) {
}
