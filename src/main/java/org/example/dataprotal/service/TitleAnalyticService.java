package org.example.dataprotal.service;

import org.example.dataprotal.dto.request.analytic.AnalyticRequest;
import org.example.dataprotal.dto.request.analytic.UpdatedAnalyticRequest;
import org.example.dataprotal.dto.response.analytic.AnalyticResponse;

import java.io.IOException;
import java.util.List;

public interface TitleAnalyticService {

    List<AnalyticResponse> getAll();

    AnalyticResponse getById(Long id);

    AnalyticResponse save(AnalyticRequest analyticRequest) throws IOException;

    AnalyticResponse update(Long id, UpdatedAnalyticRequest analyticRequest) throws IOException;

    List<AnalyticResponse> getBySubTitleId(Long subTitleId);

    void deleteById(Long id);
}
