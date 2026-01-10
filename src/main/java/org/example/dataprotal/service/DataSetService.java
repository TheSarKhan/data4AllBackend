package org.example.dataprotal.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dataprotal.dto.DataSetQueryDto;
import org.example.dataprotal.dto.request.DataSetCategoryRequest;
import org.example.dataprotal.dto.request.DataSetRequest;
import org.example.dataprotal.dto.response.DataSetCategoryResponse;
import org.example.dataprotal.dto.response.DataSetResponse;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.DataSetMapper;
import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.dataset.DataSetCategory;
import org.example.dataprotal.model.dataset.DataSetQuery;
import org.example.dataprotal.model.dataset.Intern;
import org.example.dataprotal.repository.dataset.DataSetCategoryRepository;
import org.example.dataprotal.repository.dataset.DataSetQueryRepository;
import org.example.dataprotal.repository.dataset.DataSetRepository;
import org.example.dataprotal.repository.dataset.InternRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataSetService {
    private final DataSetRepository repository;
    private final DataSetMapper dataSetMapper;
    private final FileService fileService;
    private final DataSetQueryRepository dataSetQueryRepository;
    private final DataSetCategoryRepository categoryRepository;
    private final InternRepository internRepository;

    public DataSetCategoryResponse createCategory(DataSetCategoryRequest request) throws Exception {
        String iconUrl = null;
        if (request.icon() != null && !request.icon().isEmpty()) {
            iconUrl = fileService.uploadFile(request.icon());
        }
        DataSetCategory category = DataSetCategory.builder()
                .name(request.name())
                .description(request.description())
                .iconUrl(iconUrl)
                .build();
        DataSetCategory saved = categoryRepository.save(category);
        return new DataSetCategoryResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getIconUrl()
        );
    }

    @SneakyThrows
    public DataSetResponse createDataSet(
            DataSetRequest dto,
            MultipartFile file,
            MultipartFile img
    ) {
        String fileUrl = null;
        String imageUrl = null;

        if (file != null && !file.isEmpty()) {
            fileUrl = fileService.uploadFile(file);
        }
        if (img != null && !img.isEmpty()) {
            imageUrl = fileService.uploadFile(img);
        }

        DataSetCategory category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Intern intern = internRepository.findById(dto.internId())
                .orElseThrow(() -> new RuntimeException("Intern not found"));

        DataSet dataSet = new DataSet();
        dataSet.setAuthor(dto.author());
        dataSet.setDataSetName(dto.dataSetName());
        dataSet.setTitle(dto.title());
        dataSet.setDescription(dto.description());
        dataSet.setCategory(category);
        dataSet.setIntern(intern);
        dataSet.setFileUrl(fileUrl);
        dataSet.setImageUrl(imageUrl);

        DataSet saved = repository.save(dataSet);

        return dataSetMapper.toResponseDto(saved);
    }

    public DataSet getDataSetByName(String dataSetName) {
        return repository.findByDataSetName(dataSetName);
    }

    public DataSetResponse getDataSetById(Long dataSetId) {
        return dataSetMapper.toResponseDto(repository.findById(dataSetId).orElseThrow(() ->
                new ResourceCanNotFoundException("DataSet not found")));
    }

    public DataSetQuery createDataSetQuery(DataSetQueryDto dataSetQueryDto) {
        if (!dataSetQueryDto.getIsRobot()) {
            throw new RuntimeException("dont approved data set query");
        }
        DataSetQuery dataSetQuery = dataSetMapper.queryDtoToEntity(dataSetQueryDto);
        return dataSetQueryRepository.save(dataSetQuery);
    }


    public Map<String, Object> getDataSetByCategory(String categoryName, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<DataSet> dataSetsPage = repository.findByCategory_Name(categoryName, pageable);
        return Map.of(
                "content", dataSetsPage.getContent(),
                "hasNext", offset + limit < dataSetsPage.getTotalElements()
        );
    }

    public List<DataSetCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(category ->
                new DataSetCategoryResponse(category.getId(), category.getName(), category.getDescription(), category.getIconUrl())).toList();
    }

    public List<DataSet> getAllDataSet() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public byte[] downloadDataSetFile(Long datasetId) throws Exception {
        DataSet dataSet = repository.findById(datasetId)
                .orElseThrow(() -> new RuntimeException("Can not found dataset with id: " + datasetId));
        return fileService.readFile(dataSet.getFileUrl());
    }

    public List<DataSetResponse> getDataSetsByInternId(Long internId) {
        return repository.findByIntern_id(internId).stream()
                .map(dataSetMapper::toResponseDto).toList();
    }
}
