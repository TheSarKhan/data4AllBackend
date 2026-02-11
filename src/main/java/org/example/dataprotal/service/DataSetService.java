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
import org.example.dataprotal.model.enums.DataSetStatus;
import org.example.dataprotal.repository.dataset.DataSetCategoryRepository;
import org.example.dataprotal.repository.dataset.DataSetQueryRepository;
import org.example.dataprotal.repository.dataset.DataSetRepository;
import org.example.dataprotal.repository.dataset.InternRepository;
import org.example.dataprotal.repository.dataset.specification.DataSetSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
                .isOpened(request.isOpened())
                .build();
        DataSetCategory saved = categoryRepository.save(category);
        return new DataSetCategoryResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getIconUrl(),
                saved.isOpened()
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

        DataSetCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        DataSet dataSet = new DataSet();
        dataSet.setAuthor(dto.getAuthor());
        dataSet.setDataSetName(dto.getDataSetName());
        dataSet.setTitle(dto.getTitle());
        dataSet.setDescription(dto.getDescription());
        dataSet.setCategory(category);
        if (dto.getInternId() != null) {
            Intern intern = internRepository.findById(dto.getInternId())
                    .orElseThrow(() -> new RuntimeException("Intern not found"));
            dataSet.setIntern(intern);
        } else {
            dataSet.setIntern(null);
        }
        dataSet.setFileUrl(fileUrl);
        dataSet.setImageUrl(imageUrl);
        dataSet.setOpened(dto.isOpened());
        dataSet.setStatus(DataSetStatus.PENDING);

        DataSet saved = repository.save(dataSet);

        return dataSetMapper.toResponseDto(saved);
    }

    @SneakyThrows
    public DataSetResponse updateDataSet(
            Long id,
            DataSetRequest dto,
            MultipartFile file,
            MultipartFile img
    ) {
        DataSet existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dataset not found"));

        if (file != null && !file.isEmpty()) {
            String fileUrl = fileService.uploadFile(file);
            existing.setFileUrl(fileUrl);
        }
        if (img != null && !img.isEmpty()) {
            String imageUrl = fileService.uploadFile(img);
            existing.setImageUrl(imageUrl);
        }

        if (dto.getCategoryId() != null) {
            DataSetCategory category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existing.setCategory(category);
        }

        if (dto.getInternId() != null) {
            Intern intern = internRepository.findById(dto.getInternId())
                    .orElseThrow(() -> new RuntimeException("Intern not found"));
            existing.setIntern(intern);
        } else {
            existing.setIntern(null);
        }

        existing.setAuthor(dto.getAuthor());
        existing.setDataSetName(dto.getDataSetName());
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setOpened(dto.isOpened());

        DataSet saved = repository.save(existing);
        return dataSetMapper.toResponseDto(saved);
    }

    public DataSet getDataSetByName(String dataSetName) {
        return repository.findByDataSetNameAndStatus(dataSetName, DataSetStatus.APPROVED);
    }

    public DataSet getDataSetByNameForAdmin(String dataSetName) {
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
                new DataSetCategoryResponse(category.getId(), category.getName(), category.getDescription(), category.getIconUrl(),
                        category.isOpened())).toList();
    }

    public DataSetCategoryResponse updateCategory(Long id, DataSetCategoryRequest request) throws IOException {
        DataSetCategory category = categoryRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Category not found"));

        if (request.icon() != null && !request.icon().isEmpty()) {
            fileService.deleteFile(category.getIconUrl());
            String url = fileService.uploadFile(request.icon());
            category.setIconUrl(url);
        }
        if (request.name() != null && !request.name().isEmpty()) {
            category.setName(request.name());
        }
        if (request.description() != null && !request.description().isEmpty()) {
            category.setDescription(request.description());
        }
        category.setOpened(request.isOpened());

        DataSetCategory saved = categoryRepository.save(category);
        return new DataSetCategoryResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getIconUrl(),
                saved.isOpened()
        );
    }
    public void deleteCategory(Long id) throws IOException {
        DataSetCategory category = categoryRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Category not found"));
        fileService.deleteFile(category.getIconUrl());
        categoryRepository.deleteById(id);
    }

    public List<DataSet> getAllDataSet() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public byte[] downloadDataSetFile(Long datasetId) throws Exception {
        DataSet dataSet = repository.findById(datasetId)
                .orElseThrow(() -> new ResourceCanNotFoundException("Can not found dataset with id: " + datasetId));
        return fileService.readFile(dataSet.getFileUrl());
    }

    public List<DataSetResponse> getDataSetsByInternId(Long internId) {
        return repository.findByIntern_id(internId).stream()
                .map(dataSetMapper::toResponseDto).toList();
    }

    public void changeStatus(Long id, DataSetStatus status) {
        DataSet dataSet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dataset not found"));

        dataSet.setStatus(status);
        repository.save(dataSet);
    }

    public Page<DataSetResponse> filter(String name,
                                        Long categoryId,
                                        DataSetStatus status,
                                        LocalDateTime from,
                                        LocalDateTime to,
                                        Pageable pageable) {

        Specification<DataSet> spec = Specification
                .where(DataSetSpecification.hasName(name))
                .and(DataSetSpecification.hasCategory(categoryId))
                .and(DataSetSpecification.hasStatus(status))
                .and(DataSetSpecification.createdAfter(from))
                .and(DataSetSpecification.createdBefore(to));

        return repository.findAll(spec, pageable)
                .map(dataSetMapper::toResponseDto);
    }

    public void softDelete(Long id) {
        DataSet dataSet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dataset not found"));

        dataSet.setStatus(DataSetStatus.DELETED);
        repository.save(dataSet);
    }


}
