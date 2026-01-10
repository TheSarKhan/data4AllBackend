package org.example.dataprotal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataprotal.dto.request.researchcard.ResearchCardRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchCardResponse;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.researchcard.ResearchCardMapper;
import org.example.dataprotal.model.researchcard.ResearchCard;
import org.example.dataprotal.repository.researchcard.ResearchCardRepository;
import org.example.dataprotal.service.FileService;
import org.example.dataprotal.service.ResearchCardService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResearchCardServiceImpl implements ResearchCardService {
    private final ResearchCardRepository researchCardRepository;
    private final FileService fileService;

    @Override
    public List<ResearchCardResponse> getAll() {
        log.info("Get all research cards");
        return researchCardRepository.findAll().stream().map(ResearchCardMapper::toResponse).toList();
    }

    @Override
    public ResearchCardResponse getById(Long id) {
        log.info("Get research card by id : {}", id);
        return ResearchCardMapper.toResponse(researchCardRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Research card not found")));
    }

    @Override
    public ResearchCardResponse save(ResearchCardRequest researchCardRequest) throws IOException {
        ResearchCard researchCard = ResearchCardMapper.toEntity(researchCardRequest);
        String fileUrl = fileService.uploadFile(researchCardRequest.multipartFile());
        researchCard.setFileUrl(fileUrl);
        log.info("Save research card : {}", researchCard);
        return ResearchCardMapper.toResponse(researchCardRepository.save(researchCard));
    }

    @Override
    public ResearchCardResponse update(Long id, ResearchCardRequest researchCardRequest) throws IOException {
        log.info("Update research card by id : {}", id);
        ResearchCard researchCard = researchCardRepository.findById(id)
                .orElseThrow(() -> new ResourceCanNotFoundException("Research card not found"));

        if (researchCardRequest.multipartFile() != null && !researchCardRequest.multipartFile().isEmpty()) {
            String fileUrl = fileService.uploadFile(researchCardRequest.multipartFile());
            researchCard.setFileUrl(fileUrl);
        }
        ResearchCardMapper.updateResearchCard(researchCard, researchCardRequest);

        researchCardRepository.save(researchCard);

        return ResearchCardMapper.toResponse(researchCard);
    }

    @Override
    public List<ResearchCardResponse> getBySubTitleId(Long subTitleId) {

        List<ResearchCard> researchCards = researchCardRepository.findBySubTitleId(subTitleId);
        return researchCards.stream().map(ResearchCardMapper::toResponse).toList();
    }

    @Override
    public void deleteById(Long id) {
        log.info("Delete research card by id : {}", id);
        researchCardRepository.deleteById(id);
    }
}
