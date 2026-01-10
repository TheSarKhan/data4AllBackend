package org.example.dataprotal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataprotal.dto.request.researchcard.ResearchSubTitleRequest;
import org.example.dataprotal.dto.response.researchcard.ResearchSubTitleResponse;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.researchcard.ResearchSubTitleMapper;
import org.example.dataprotal.model.researchcard.ResearchSubTitle;
import org.example.dataprotal.model.researchcard.ResearchTitle;
import org.example.dataprotal.repository.researchcard.ResearchSubTitleRepository;
import org.example.dataprotal.repository.researchcard.ResearchTitleRepository;
import org.example.dataprotal.service.ResearchSubTitleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResearchSubTitleServiceImpl implements ResearchSubTitleService {
    private final ResearchTitleRepository researchTitleRepository;
    private final ResearchSubTitleRepository researchSubTitleRepository;

    @Override
    public List<ResearchSubTitleResponse> getAll() {
        log.info("Get all subtitles");
        return researchSubTitleRepository.findAll().stream().map(ResearchSubTitleMapper::toResponse).toList();
    }

    @Override
    public ResearchSubTitleResponse getById(Long id) {
        log.info("Get subtitle by id : {}", id);
        return ResearchSubTitleMapper.toResponse(researchSubTitleRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Subtitle not found")));
    }

    @Override
    public ResearchSubTitleResponse save(ResearchSubTitleRequest subTitleRequest) {
        ResearchSubTitle researchSubTitle = ResearchSubTitleMapper.toEntity(subTitleRequest);
        log.info("Save subtitle : {}", researchSubTitle);
        return ResearchSubTitleMapper.toResponse(researchSubTitleRepository.save(researchSubTitle));
    }

    @Override
    public ResearchSubTitleResponse update(Long id, ResearchSubTitleRequest subTitleRequest) {
        log.info("Update subtitle by id : {}", id);
        ResearchSubTitle researchSubTitle = researchSubTitleRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Subtitle not found"));
        researchSubTitle.setName(subTitleRequest.name());
        ResearchTitle researchTitle = researchTitleRepository.findById(subTitleRequest.titleId()).orElseThrow(() -> new ResourceCanNotFoundException("Title not found"));
        researchSubTitle.setTitle(researchTitle);
        return ResearchSubTitleMapper.toResponse(researchSubTitleRepository.save(researchSubTitle));
    }

    @Override
    public List<ResearchSubTitleResponse> getByTitleId(Long titleId) {
        List<ResearchSubTitle> researchSubTitles = researchSubTitleRepository.getByTitleId(titleId);
        return researchSubTitles.stream().map(ResearchSubTitleMapper::toResponse).toList();
    }

    @Override
    public void deleteById(Long id) {
        log.info("Delete subtitle by id : {}", id);
        researchSubTitleRepository.deleteById(id);
    }
}
