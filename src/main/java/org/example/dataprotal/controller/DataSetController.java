package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.DataSetQueryDto;
import org.example.dataprotal.dto.request.DataSetRequest;
import org.example.dataprotal.dto.response.DataSetResponse;
import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.dataset.DataSetQuery;
import org.example.dataprotal.service.DataSetService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dataset")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DataSetController {
    private final DataSetService service;

    @GetMapping("/get/{dataSetName}")
    @Operation(description = "Get dataSet with name")
    public ResponseEntity<DataSet> getDataSet(@PathVariable String dataSetName) {
        return ResponseEntity.ok(service.getDataSetByName(dataSetName));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DataSet>> getAllDataSet() {
        return ResponseEntity.ok(service.getAllDataSet());
    }


    @PostMapping("/query")
    @Operation(description = "Send dataSet query")
    public ResponseEntity<DataSetQuery> sendDataSetQuery(@RequestBody DataSetQueryDto dataSetQueryDto) {
        final var createDataSet = service.createDataSetQuery(dataSetQueryDto);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(createDataSet.getId());
        return ResponseEntity.created(location).body(createDataSet);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload new dataset")
    public ResponseEntity<DataSetResponse> createDataSet(
            @RequestPart("data") @Valid DataSetRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        DataSetResponse response = service.createDataSet(request, file, image);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

}
