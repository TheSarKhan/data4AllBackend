package org.example.dataprotal.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FAQRequest(@NotBlank String question,
                         @NotBlank String answer) {
}
