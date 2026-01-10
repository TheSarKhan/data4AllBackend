package org.example.dataprotal.service;

import org.example.dataprotal.dto.request.researchcard.ResearchSubTitleRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchSubTitleResponse;

import java.util.List;

public interface ResearchSubTitleService {
    List<ResearchSubTitleResponse> getAll();

    ResearchSubTitleResponse getById(Long id);

    ResearchSubTitleResponse save(ResearchSubTitleRequest subTitleRequest);

    ResearchSubTitleResponse update(Long id, ResearchSubTitleRequest subTitleRequest);

    List<ResearchSubTitleResponse> getByTitleId(Long titleId);

    void deleteById(Long id);
}
