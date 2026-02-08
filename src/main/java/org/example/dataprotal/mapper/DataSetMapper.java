package org.example.dataprotal.mapper;

import org.example.dataprotal.dto.DataSetQueryDto;
import org.example.dataprotal.dto.request.DataSetRequest;
import org.example.dataprotal.dto.response.DataSetResponse;
import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.dataset.DataSetQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DataSetMapper {
    @Mapping(source = "categoryId", target = "category.id")
    DataSet dtoToEntity(DataSetRequest dataSetDto);

    DataSetQuery queryDtoToEntity(DataSetQueryDto dataSetQueryDto);

    default DataSetResponse toResponseDto(DataSet dataSet) {
        return new DataSetResponse(
                dataSet.getId(),
                dataSet.getAuthor(),
                dataSet.getDataSetName(),
                dataSet.getTitle(),
                dataSet.getDescription(),
                dataSet.getImageUrl(),
                dataSet.getFileUrl(),
                dataSet.getStatus(),
                dataSet.getCategory() != null ? dataSet.getCategory().getName() : null,
                dataSet.getIntern() != null ? dataSet.getIntern().getName() : null,
                dataSet.getCreatedAt(),
                dataSet.isOpened()
        );
    }
}
