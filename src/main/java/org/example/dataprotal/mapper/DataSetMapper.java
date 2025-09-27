package org.example.dataprotal.mapper;

import org.example.dataprotal.dto.DataSetDto;
import org.example.dataprotal.dto.DataSetQueryDto;
import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.dataset.DataSetQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataSetMapper {
    DataSet dtoToEntity(DataSetDto dataSetDto);

    DataSetDto entityToDto(DataSet dataSet);

    DataSetQuery queryDtoToEntity(DataSetQueryDto dataSetQueryDto);
}
