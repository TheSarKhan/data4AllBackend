package org.example.dataprotal.service;

import org.example.dataprotal.dto.request.analytic.TitleRequest;
import org.example.dataprotal.dto.request.analytic.UpdatedAnalyticTitle;
import org.example.dataprotal.dto.response.analytic.TitleResponse;

import java.util.List;

public interface TitleService {

    List<TitleResponse> getAll();

    TitleResponse getById(Long id);

    TitleResponse save(TitleRequest titleRequest);

    TitleResponse update(Long id, UpdatedAnalyticTitle titleRequest);

    void deleteById(Long id);

    void changeOpenedStatus(Long id, boolean isOpened);
}
