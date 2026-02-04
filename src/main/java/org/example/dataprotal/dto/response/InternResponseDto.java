package org.example.dataprotal.dto.response;

import org.example.dataprotal.model.enums.PositionStatus;

import java.util.List;

public record InternResponseDto(Long id, String name,
                                String email, String phone, List<DataSetResponse> dataSets,
                                PositionStatus status) {
}
