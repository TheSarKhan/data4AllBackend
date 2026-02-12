package org.example.dataprotal.service;

import jakarta.security.auth.message.AuthException;
import org.example.dataprotal.dto.request.researchcard.ResearchCardRequest;
import org.example.dataprotal.dto.request.researchcard.UpdatedResearchCardRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchCardResponse;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface ResearchCardService {

    List<ResearchCardResponse> getAll();

    ResearchCardResponse getById(Long id);

    ResearchCardResponse save(ResearchCardRequest analyticRequest) throws IOException;

    ResearchCardResponse update(Long id, UpdatedResearchCardRequest analyticRequest) throws IOException;

    List<ResearchCardResponse> getBySubTitleId(Long subTitleId);

    void deleteById(Long id);

    void incrementView(Long id);

    void toggleLike(Long cardId) throws AuthException;

    Resource getFile(String storedName) throws MalformedURLException;

    void changeOpenedStatus(Long cardId, boolean isOpened);
}
