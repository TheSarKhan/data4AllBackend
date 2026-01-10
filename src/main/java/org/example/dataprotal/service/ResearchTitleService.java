package org.example.dataprotal.service;

import org.example.dataprotal.dto.request.researchcard.ResearchTitleRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchTitleResponse;

import java.util.List;

public interface ResearchTitleService {

    List<ResearchTitleResponse> getAll();

    ResearchTitleResponse getById(Long id);

    ResearchTitleResponse save(ResearchTitleRequest titleRequest);

    ResearchTitleResponse update(Long id, ResearchTitleRequest titleRequest);

    void deleteById(Long id);
}
