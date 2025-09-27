package org.example.dataprotal.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dataprotal.dto.DataSetDto;
import org.example.dataprotal.dto.DataSetQueryDto;
import org.example.dataprotal.mapper.DataSetMapper;
import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.dataset.DataSetQuery;
import org.example.dataprotal.repository.dataset.DataSetQueryRepository;
import org.example.dataprotal.repository.dataset.DataSetRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DataSetService {
    private final DataSetRepository repository;
    private final DataSetMapper dataSetMapper;
    private final FileService fileService;
    private final DataSetQueryRepository dataSetQueryRepository;

    @SneakyThrows
    public DataSet createDataSet(DataSetDto dataSetDto, MultipartFile file, MultipartFile img) {
        String dataSetFile = null, dataSetImg = null;
        if (file != null && !file.isEmpty() ) {
            dataSetFile = fileService.uploadFile(file);
        }
        if (img != null && !img.isEmpty()) {
            dataSetImg = fileService.uploadFile(img);
        }
        DataSet dataSet = dataSetMapper.dtoToEntity(dataSetDto);
        dataSet.setFileUrl(dataSetFile);
        dataSet.setImageUrl(dataSetImg);
        return repository.save(dataSet);
    }

    public DataSetDto getDataSetByName(String dataSetName) {
        DataSet dataSet = repository.findByDataSetName(dataSetName);
        return dataSetMapper.entityToDto(dataSet);
    }

    public DataSetQuery createDataSetQuery(DataSetQueryDto dataSetQueryDto) {
        if (!dataSetQueryDto.getIsRobot()) {
            throw new RuntimeException("dont approved data set query");
        }
        DataSetQuery dataSetQuery = dataSetMapper.queryDtoToEntity(dataSetQueryDto);
        return dataSetQueryRepository.save(dataSetQuery);
    }
}
