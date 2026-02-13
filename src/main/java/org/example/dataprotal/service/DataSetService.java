package org.example.dataprotal.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dataprotal.dto.DataSetQueryDto;
import org.example.dataprotal.dto.request.DataSetCategoryRequest;
import org.example.dataprotal.dto.request.DataSetRequest;
import org.example.dataprotal.dto.request.UpdatedDatasetCategory;
import org.example.dataprotal.dto.request.UpdatedDatasetRequest;
import org.example.dataprotal.dto.response.DataSetCategoryResponse;
import org.example.dataprotal.dto.response.DataSetResponse;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.DataSetMapper;
import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.dataset.DataSetCategory;
import org.example.dataprotal.model.dataset.DataSetQuery;
import org.example.dataprotal.model.dataset.Intern;
import org.example.dataprotal.model.enums.DataSetStatus;
import org.example.dataprotal.model.user.User;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataSetService {
    private final DataSetRepository repository;
    private final DataSetMapper dataSetMapper;
    private final FileService fileService;
    private final DataSetQueryRepository dataSetQueryRepository;
    private final DataSetCategoryRepository categoryRepository;
    private final InternRepository internRepository;
    private final UserService userService;

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
        User currentUser = userService.getCurrentUser();
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
        dataSet.setAuthor(currentUser.getLastName() + " " + currentUser.getFirstName());
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
            UpdatedDatasetRequest dto,
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

        DataSet saved = repository.save(existing);
        return dataSetMapper.toResponseDto(saved);
    }

    public DataSetResponse getDataSetByName(String dataSetName) {
        return dataSetMapper.toResponseDto(repository.findByDataSetNameAndStatus(dataSetName, DataSetStatus.APPROVED));
    }

    public List<DataSetResponse> getDataSetByCategoryId(Long categoryId) {
        return repository.findByCategory_Id(categoryId).stream().map(dataSetMapper::toResponseDto).toList();
    }

    public DataSetResponse getDataSetByNameForAdmin(String dataSetName) {
        return dataSetMapper.toResponseDto(repository.findByDataSetName(dataSetName));
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

    public DataSetCategoryResponse updateCategory(Long id, UpdatedDatasetCategory request) throws IOException {
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

    public List<DataSetResponse> getAllDataSet() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().map(dataSetMapper::toResponseDto).toList();
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

    public void changeCategoryOpenedStatus(Long id, boolean isOpened) {
        DataSetCategory category = categoryRepository.findById(id).orElseThrow(() ->
                new ResourceCanNotFoundException("Category not found"));

        category.setOpened(isOpened);
        categoryRepository.save(category);
        log.info("Change opened status of category with id : {} to {}", id, isOpened);
    }

    public void changeDataSetOpenedStatus(Long id, boolean isOpened) {
        DataSet dataSet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dataset not found"));

        dataSet.setOpened(isOpened);
        repository.save(dataSet);
        log.info("Change opened status of dataset with id : {} to {}", id, isOpened);
    }

    public byte[] exportToExcel(String name,
                                Long categoryId,
                                DataSetStatus status,
                                LocalDateTime from,
                                LocalDateTime to) throws Exception {

        Specification<DataSet> spec = Specification
                .where(DataSetSpecification.hasName(name))
                .and(DataSetSpecification.hasCategory(categoryId))
                .and(DataSetSpecification.hasStatus(status))
                .and(DataSetSpecification.createdAfter(from))
                .and(DataSetSpecification.createdBefore(to));

        List<DataSet> dataSets = repository.findAll(spec);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Datasets");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Title");
        header.createCell(2).setCellValue("Author");
        header.createCell(3).setCellValue("Category");
        header.createCell(4).setCellValue("Status");
        header.createCell(5).setCellValue("Created At");
        header.createCell(6).setCellValue("Opened");

        int rowNum = 1;

        for (DataSet ds : dataSets) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(ds.getId());
            row.createCell(1).setCellValue(ds.getTitle());
            row.createCell(2).setCellValue(ds.getAuthor());
            row.createCell(3).setCellValue(
                    ds.getCategory() != null ? ds.getCategory().getName() : ""
            );
            row.createCell(4).setCellValue(ds.getStatus().name());
            row.createCell(5).setCellValue(
                    ds.getCreatedAt() != null ? ds.getCreatedAt().toString() : ""
            );
            row.createCell(6).setCellValue(ds.isOpened());
        }

        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }


}
