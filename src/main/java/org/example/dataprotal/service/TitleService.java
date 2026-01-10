package org.example.dataprotal.service;

import org.example.dataprotal.dto.request.analytic.TitleRequest;
import org.example.dataprotal.dto.response.analytic.TitleResponse;

import java.util.List;

public interface TitleService {

    List<TitleResponse> getAll();

    TitleResponse getById(Long id);

    TitleResponse save(TitleRequest titleRequest);

    TitleResponse update(Long id, TitleRequest titleRequest);

    void deleteById(Long id);
}
