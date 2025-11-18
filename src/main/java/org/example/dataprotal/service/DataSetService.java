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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (file != null && !file.isEmpty()) {
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

    public DataSet getDataSetByName(String dataSetName) {
        return repository.findByDataSetName(dataSetName);
    }

    public DataSetQuery createDataSetQuery(DataSetQueryDto dataSetQueryDto) {
        if (!dataSetQueryDto.getIsRobot()) {
            throw new RuntimeException("dont approved data set query");
        }
        DataSetQuery dataSetQuery = dataSetMapper.queryDtoToEntity(dataSetQueryDto);
        return dataSetQueryRepository.save(dataSetQuery);
    }


    public Map<String, Object> getDataSetByCategory(String category, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<DataSet> dataSetsPage = repository.findByCategory(category, pageable);
        return Map.of(
                "content", dataSetsPage.getContent(),
                "hasNext", offset+limit < dataSetsPage.getTotalElements()
                );
    }

    public List<DataSet> getAllDataSet() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
