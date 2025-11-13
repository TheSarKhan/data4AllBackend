package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.dto.DataSetDto;
import org.example.dataprotal.dto.DataSetQueryDto;
import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.dataset.DataSetQuery;
import org.example.dataprotal.service.DataSetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dataset")
@RequiredArgsConstructor
public class DataSetController {
    private final DataSetService service;

    @GetMapping("/get/{dataSetName}")
    @Operation(description = "Get dataSet with name")
    public ResponseEntity<DataSet> getDataSet(@PathVariable String dataSetName) {
        return ResponseEntity.ok(service.getDataSetByName(dataSetName));
    }

    @GetMapping("/get/all/{category}")
    @Operation(description = "Get dataSets with category", summary = "offset is the number of elements that already got.Front must send offset for each time." +
            "At the begining offset will be equal to 0." +
            "I give 5 element limit for now.You can only get 5 element for every fetch.")
    public ResponseEntity<Map<String, Object>> getAllDataSetByCategory(@PathVariable String category,
                                                                       @RequestParam int offset) {
        return ResponseEntity.ok(service.getDataSetByCategory(category, offset, 5));
    }

    @PostMapping("/query")
    @Operation(description = "Send dataSet query")
    public ResponseEntity<DataSetQuery> sendDataSetQuery(@RequestBody DataSetQueryDto dataSetQueryDto) {
        final var createDataSet = service.createDataSetQuery(dataSetQueryDto);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(createDataSet.getId());
        return ResponseEntity.created(location).body(createDataSet);
    }

    @PostMapping
    @Operation(description = "Upload new dataSet")
    public ResponseEntity<DataSet> createDataSet(@RequestPart("request") DataSetDto dataSetDto,
                                                 @RequestPart(required = false) MultipartFile file,
                                                 @RequestPart(required = false) MultipartFile img) {
        final var createDataSet = service.createDataSet(dataSetDto, file, img);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(createDataSet.getId());
        return ResponseEntity.created(location).body(createDataSet);
    }

}
