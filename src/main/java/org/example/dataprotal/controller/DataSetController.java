package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.DataSetQueryDto;
import org.example.dataprotal.dto.request.UpdatedDatasetRequest;
import org.example.dataprotal.dto.response.DataSetResponse;
import org.example.dataprotal.model.dataset.DataSetQuery;
import org.example.dataprotal.model.enums.DataSetStatus;
import org.example.dataprotal.service.DataSetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dataset")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DataSetController {
    private final DataSetService service;

    @GetMapping("/get/{dataSetName}")
    @Operation(description = "Get dataSet with name")
    public ResponseEntity<DataSetResponse> getDataSet(@PathVariable String dataSetName) {
        return ResponseEntity.ok(service.getDataSetByName(dataSetName));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DataSetResponse>> getAllDataSet() {
        return ResponseEntity.ok(service.getAllDataSet());
    }


    @PostMapping("/query")
    @Operation(description = "Send dataSet query")
    public ResponseEntity<DataSetQuery> sendDataSetQuery(@RequestBody DataSetQueryDto dataSetQueryDto) {
        final var createDataSet = service.createDataSetQuery(dataSetQueryDto);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(createDataSet.getId());
        return ResponseEntity.created(location).body(createDataSet);
    }



    @PutMapping("/update/{id}")
    @Operation(description = "Update dataset")
    public ResponseEntity<DataSetResponse> updateDataSet(
            @PathVariable Long id,
            @ModelAttribute UpdatedDatasetRequest request,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) MultipartFile img
    ) {

        return ResponseEntity.ok(
                service.updateDataSet(id, request, file, img)
        );
    }


    @DeleteMapping("/delete/{id}")
    @Operation(description = "Soft delete dataset")
    public ResponseEntity<Void> deleteDataSet(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/status/{id}")
    @Operation(description = "Change dataset status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Long id,
            @RequestParam DataSetStatus status
    ) {
        service.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/opened/{id}")
    @Operation(description = "Change dataset opened status")
    public ResponseEntity<Void> changeOpenedStatus(
            @PathVariable Long id,
            @RequestParam boolean isOpened
    ) {
        service.changeDataSetOpenedStatus(id, isOpened);
        return ResponseEntity.ok().build();
    }

}
