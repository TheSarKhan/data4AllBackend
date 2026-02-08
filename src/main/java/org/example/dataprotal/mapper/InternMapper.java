package org.example.dataprotal.mapper;

import org.example.dataprotal.dto.request.InternRequestDto;
import org.example.dataprotal.dto.response.DataSetResponse;
import org.example.dataprotal.dto.response.InternResponseDto;
import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.dataset.Intern;

import java.util.List;

public class InternMapper {
    public static InternResponseDto toResponseDto(Intern intern) {
        return new InternResponseDto(
                intern.getId(),
                intern.getName(),
                intern.getEmail(),
                intern.getPhone(),
                mapToDataSetResponse(intern.getDatasets()),
                intern.getPosition()
        );
    }

    public static Intern toEntity(InternRequestDto internRequestDto) {
        return Intern.builder()
                .name(internRequestDto.name())
                .email(internRequestDto.email())
                .phone(internRequestDto.phone())
                .position(internRequestDto.status())
                .build();
    }

    private static List<DataSetResponse> mapToDataSetResponse(List<DataSet> dataSets) {
        return dataSets.stream().map(dataSet -> new DataSetResponse(
                dataSet.getId(),
                dataSet.getAuthor(),
                dataSet.getDataSetName(),
                dataSet.getTitle(),
                dataSet.getDescription(),
                dataSet.getImageUrl(),
                dataSet.getFileUrl(),
                dataSet.getStatus(),
                dataSet.getCategory().getName(),
                dataSet.getIntern().getName(),
                dataSet.getCreatedAt(),
                dataSet.isOpened()
        )).toList();
    }
}
