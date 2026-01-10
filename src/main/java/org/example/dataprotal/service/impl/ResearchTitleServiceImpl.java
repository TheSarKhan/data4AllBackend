package org.example.dataprotal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataprotal.dto.request.researchcard.ResearchTitleRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchTitleResponse;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.researchcard.ResearchTitleMapper;
import org.example.dataprotal.model.researchcard.ResearchTitle;
import org.example.dataprotal.repository.researchcard.ResearchTitleRepository;
import org.example.dataprotal.service.ResearchTitleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResearchTitleServiceImpl implements ResearchTitleService {
    private final ResearchTitleRepository researchTitleRepository;

    @Override
    public List<ResearchTitleResponse> getAll() {
        log.info("Get all research titles");
        return researchTitleRepository.findAll().stream().map(ResearchTitleMapper::toResponse).toList();
    }

    @Override
    public ResearchTitleResponse getById(Long id) {
        log.info("Get research title by id : {}", id);
        return ResearchTitleMapper.toResponse(researchTitleRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Research Title not found")));
    }

    @Override
    public ResearchTitleResponse save(ResearchTitleRequest titleRequest) {
        ResearchTitle researchTitle = ResearchTitleMapper.toEntity(titleRequest);
        log.info("Save research title : {}", researchTitle);
        return ResearchTitleMapper.toResponse(researchTitleRepository.save(researchTitle));
    }

    @Override
    public ResearchTitleResponse update(Long id, ResearchTitleRequest titleRequest) {
        log.info("Update research title by id : {}", id);
        ResearchTitle researchTitle = researchTitleRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Research Title not found"));
        researchTitle.setName(titleRequest.name());
        return ResearchTitleMapper.toResponse(researchTitleRepository.save(researchTitle));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Delete research title by id : {}", id);
        researchTitleRepository.deleteById(id);
    }
}
