package org.example.dataprotal.service.impl;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataprotal.dto.request.researchcard.ResearchCardRequest;
import org.example.dataprotal.dto.request.researchcard.UpdatedResearchCardRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchCardResponse;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.researchcard.ResearchCardMapper;
import org.example.dataprotal.model.researchcard.ResearchCard;
import org.example.dataprotal.model.researchcard.ResearchCardLike;
import org.example.dataprotal.model.user.User;
import org.example.dataprotal.repository.researchcard.ResearchCardLikeRepository;
import org.example.dataprotal.repository.researchcard.ResearchCardRepository;
import org.example.dataprotal.service.FileService;
import org.example.dataprotal.service.ResearchCardService;
import org.example.dataprotal.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResearchCardServiceImpl implements ResearchCardService {
    private final ResearchCardRepository researchCardRepository;
    private final FileService fileService;
    private final ResearchCardLikeRepository researchCardLikeRepository;
    private final UserService userService;

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

        if (researchCardRequest.multipartFile() != null
                && !researchCardRequest.multipartFile().isEmpty()) {

            String fileUrl = fileService.uploadFile(researchCardRequest.multipartFile());
            researchCard.setFileUrl(fileUrl);
        }

        log.info("Save research card : {}", researchCard);
        return ResearchCardMapper.toResponse(researchCardRepository.save(researchCard));
    }

    @Override
    public ResearchCardResponse update(Long id, UpdatedResearchCardRequest researchCardRequest) throws IOException {

        log.info("Update research card by id : {}", id);

        ResearchCard researchCard = researchCardRepository.findById(id)
                .orElseThrow(() -> new ResourceCanNotFoundException("Research card not found"));

        if (researchCardRequest.multipartFile() != null
                && !researchCardRequest.multipartFile().isEmpty()) {

            if (researchCard.getFileUrl() != null) {
                fileService.deleteFile(researchCard.getFileUrl());
            }
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
    @Transactional
    public void deleteById(Long id) {
        ResearchCard researchCard = researchCardRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Research card not found"));
        researchCard.getSubTitle().getResearchCards().remove(researchCard);
        researchCard.setSubTitle(null);

        log.info("Delete research card by id : {}", id);
        researchCardRepository.delete(researchCard);
    }

    @Override
    public void incrementView(Long id) {
        ResearchCard card = researchCardRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Research card not found"));
        card.setViewCount(card.getViewCount() + 1);
        researchCardRepository.save(card);
    }

    @Override
    public void toggleLike(Long cardId) throws AuthException {
        User currentUser = userService.getCurrentUser();
        ResearchCard researchCard = researchCardRepository.findById(cardId).orElseThrow(() ->
                new ResourceCanNotFoundException("Research card not found"));
        Optional<ResearchCardLike> researchCardLike = researchCardLikeRepository.findByResearchCardIdAndUserId(cardId, currentUser.getId());
        if (researchCardLike.isPresent()) {
            researchCardLikeRepository.delete(researchCardLike.get());
        } else {
            ResearchCardLike like = ResearchCardLike.builder()
                    .userId(currentUser.getId())
                    .researchCard(researchCard)
                    .build();
            researchCardLikeRepository.save(like);
        }
        long likeCount = researchCardLikeRepository.countByResearchCardId(cardId);
        researchCard.setLikeCount(likeCount);
        researchCardRepository.save(researchCard);
    }

    public Resource getFile(String storedName) throws MalformedURLException {
        Resource file = fileService.getFile(storedName);
        if (!file.exists() || !file.isReadable()) {
            throw new ResourceCanNotFoundException("File not found");
        }
        return file;
    }

    @Override
    public void changeOpenedStatus(Long cardId, boolean isOpened) {
        ResearchCard researchCard = researchCardRepository.findById(cardId).orElseThrow(() ->
                new ResourceCanNotFoundException("Research card not found"));
        researchCard.setOpened(isOpened);
        researchCardRepository.save(researchCard);
        log.info("Change opened status of research card by id : {}", cardId);
    }
}
