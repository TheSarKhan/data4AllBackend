package org.example.dataprotal.service;

import org.example.dataprotal.dto.request.analytic.SubTitleRequest;
import org.example.dataprotal.dto.request.analytic.UpdatedAnalyticSubtitle;
import org.example.dataprotal.dto.response.analytic.SubTitleResponse;

import java.util.List;

public interface SubTitleService {

    List<SubTitleResponse> getAll();

    SubTitleResponse getById(Long id);

    SubTitleResponse save(SubTitleRequest subTitleRequest);

    SubTitleResponse update(Long id, UpdatedAnalyticSubtitle subTitleRequest);

    List<SubTitleResponse> getByTitleId(Long titleId);

    void deleteById(Long id);

    void changeOpenedStatus(Long id, boolean isOpened);
}
