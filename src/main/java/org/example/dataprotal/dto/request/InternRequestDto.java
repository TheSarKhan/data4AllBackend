package org.example.dataprotal.dto.request;

import org.example.dataprotal.model.enums.PositionStatus;

public record InternRequestDto(String name, String email, String phone, PositionStatus status) {
}
