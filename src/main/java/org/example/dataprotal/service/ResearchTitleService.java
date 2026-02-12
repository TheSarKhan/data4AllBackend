package org.example.dataprotal.service;

import org.example.dataprotal.dto.request.researchcard.ResearchTitleRequest;
import org.example.dataprotal.dto.request.researchcard.UpdatedResearchTitle;
import org.example.dataprotal.dto.response.researchcard.ResearchTitleResponse;

import java.util.List;

public interface ResearchTitleService {

    List<ResearchTitleResponse> getAll();

    ResearchTitleResponse getById(Long id);

    ResearchTitleResponse save(ResearchTitleRequest titleRequest);

    ResearchTitleResponse update(Long id, UpdatedResearchTitle titleRequest);

    void deleteById(Long id);

    void changeOpenedStatus(Long id, boolean isOpened);
}
