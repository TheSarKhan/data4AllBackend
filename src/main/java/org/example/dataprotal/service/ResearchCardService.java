package org.example.dataprotal.service;

import org.example.dataprotal.dto.request.researchcard.ResearchCardRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchCardResponse;

import java.io.IOException;
import java.util.List;

public interface ResearchCardService {

    List<ResearchCardResponse> getAll();

    ResearchCardResponse getById(Long id);

    ResearchCardResponse save(ResearchCardRequest analyticRequest) throws IOException;

    ResearchCardResponse update(Long id, ResearchCardRequest analyticRequest) throws IOException;

    List<ResearchCardResponse> getBySubTitleId(Long subTitleId);

    void deleteById(Long id);
}
