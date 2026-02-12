package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.request.DataSetCategoryRequest;
import org.example.dataprotal.dto.request.UpdatedDatasetCategory;
import org.example.dataprotal.dto.request.UpdatedDatasetRequest;
import org.example.dataprotal.dto.response.DashboardResponse;
import org.example.dataprotal.dto.response.DataSetCategoryResponse;
import org.example.dataprotal.dto.response.DataSetResponse;
import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.enums.DataSetStatus;
import org.example.dataprotal.service.DashboardService;
import org.example.dataprotal.service.DataSetService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Dashboard Controller", description = "Provides information for admin dashboard")
@PreAuthorize("hasRole('ADMIN') ")
public class AdminDashboardController {
    private final DashboardService dashboardService;
    private final DataSetService dataSetService;

    @GetMapping("/dashboard-information")
    @Operation(summary = "Get dashboard data", description = "Returns dashboard information such as total users, total subscriptions, etc.")
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }

    @GetMapping("/get/{dataSetName}")
    @Operation(description = "Get dataSet with name for admin")
    public ResponseEntity<DataSet> getDataSet(@PathVariable String dataSetName) {
        return ResponseEntity.ok(dataSetService.getDataSetByNameForAdmin(dataSetName));
    }

    @PostMapping(value = "/category/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create data set category", description = "Creates a new data set category")
    public ResponseEntity<DataSetCategoryResponse> createCategory(@ModelAttribute DataSetCategoryRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(dataSetService.createCategory(request));
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Returns all categories")
    public List<DataSetCategoryResponse> getAllCategories() {
        return dataSetService.getAllCategories();
    }

    @GetMapping("/get/all/{category}")
    @Operation(description = "Get dataSets with category", summary = "offset is the number of elements that already got.Front must send offset for each time." +
            "At the begining offset will be equal to 0." +
            "I give 5 element limit for now.You can only get 5 element for every fetch.")
    public ResponseEntity<Map<String, Object>> getAllDataSetByCategory(@PathVariable String category,
                                                                       @RequestParam int offset) {
        return ResponseEntity.ok(dataSetService.getDataSetByCategory(category, offset, 5));
    }

    @GetMapping("/get/{datasetId}")
    @Operation(description = "Get dataSet with id")
    public ResponseEntity<DataSetResponse> getDataSetById(@PathVariable Long datasetId) {
        return ResponseEntity.ok(dataSetService.getDataSetById(datasetId));
    }

    @GetMapping("/datasets-by-intern-id/{internId}")
    @Operation(description = "Get dataSets by intern id")
    public ResponseEntity<List<DataSetResponse>> getDataSetsByInternId(@PathVariable Long internId) {
        return ResponseEntity.ok(dataSetService.getDataSetsByInternId(internId));
    }

    @GetMapping("/datasets/read-file/{id}")
    @Operation(summary = "Read data set file", description = "Reads data set file")
    public ResponseEntity<ByteArrayResource> readDataSetFile(@PathVariable Long id) throws Exception {
        byte[] data = dataSetService.downloadDataSetFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"dataset_" + id + ".csv\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(data));
    }

    @PatchMapping("/datasets/{id}/status")
    @Operation(summary = "Change data set status", description = "Changes data set status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id,
                                             @RequestBody DataSetStatus status) {
        dataSetService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/update-dataset/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update dataset")
    public ResponseEntity<DataSetResponse> updateDataSet(
            @PathVariable Long id,
            @RequestPart("data") @Valid UpdatedDatasetRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        DataSetResponse response = dataSetService.updateDataSet(id, request, file, image);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<DataSetResponse>> filter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) DataSetStatus status,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                dataSetService.filter(name, categoryId, status, fromDate, toDate, pageable)
        );
    }

    @DeleteMapping("/datasets/{id}")
    @Operation(summary = "Delete data set", description = "Deletes data set")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dataSetService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/updateCategory/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update category", description = "Updates category")
    public ResponseEntity<DataSetCategoryResponse> updateCategory(@PathVariable Long id, @ModelAttribute UpdatedDatasetCategory request) throws IOException {
        return ResponseEntity.ok(dataSetService.updateCategory(id, request));
    }

    @DeleteMapping("/deleteCategory/{id}")
    @Operation(summary = "Delete data set category", description = "Deletes data set category")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) throws IOException {
        dataSetService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/category/{id}/opened")
    @Operation(summary = "Change opened status of category", description = "Changes opened status of category by ID")
    public ResponseEntity<Void> changeCategoryOpenedStatus(@PathVariable Long id, @RequestParam boolean isOpened) {
        dataSetService.changeCategoryOpenedStatus(id, isOpened);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dataset/{id}/opened")
    @Operation(summary = "Change opened status of dataset", description = "Changes opened status of dataset by ID")
    public ResponseEntity<Void> changeDataSetOpenedStatus(@PathVariable Long id, @RequestParam boolean isOpened) {
        dataSetService.changeDataSetOpenedStatus(id, isOpened);
        return ResponseEntity.ok().build();
    }
}
