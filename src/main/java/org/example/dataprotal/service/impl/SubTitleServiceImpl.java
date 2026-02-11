package org.example.dataprotal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataprotal.dto.request.analytic.SubTitleRequest;
import org.example.dataprotal.dto.response.analytic.SubTitleResponse;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.analytic.SubTitleMapper;
import org.example.dataprotal.model.analytics.Analytic;
import org.example.dataprotal.model.analytics.SubTitle;
import org.example.dataprotal.model.analytics.Title;
import org.example.dataprotal.repository.analytics.SubTitleRepository;
import org.example.dataprotal.repository.analytics.TitleRepository;
import org.example.dataprotal.service.SubTitleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubTitleServiceImpl implements SubTitleService {
    private final SubTitleRepository subTitleRepository;
    private final TitleRepository titleRepository;

    @Override
    public List<SubTitleResponse> getAll() {
        log.info("Get all subtitles");
        return subTitleRepository.findAll().stream().map(SubTitleMapper::toResponse).toList();
    }

    @Override
    public SubTitleResponse getById(Long id) {
        log.info("Get subtitle by id : {}", id);
        return SubTitleMapper.toResponse(subTitleRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Subtitle not found")));
    }

    @Override
    public SubTitleResponse save(SubTitleRequest subTitleRequest) {
        SubTitle subTitle = SubTitleMapper.toEntity(subTitleRequest);
        log.info("Save subtitle : {}", subTitle);
        return SubTitleMapper.toResponse(subTitleRepository.save(subTitle));
    }

    @Override
    public SubTitleResponse update(Long id, SubTitleRequest subTitleRequest) {
        log.info("Update subtitle by id : {}", id);
        SubTitle subTitle = subTitleRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Subtitle not found"));
        subTitle.setName(subTitleRequest.name());
        Title title = titleRepository.findById(subTitleRequest.titleId()).orElseThrow(() -> new ResourceCanNotFoundException("Title not found"));
        subTitle.setTitle(title);
        subTitle.setOpened(subTitleRequest.isOpened());
        return SubTitleMapper.toResponse(subTitleRepository.save(subTitle));
    }

    @Override
    public List<SubTitleResponse> getByTitleId(Long titleId) {
        List<SubTitle> subTitles = subTitleRepository.findByTitleId(titleId);
        return subTitles.stream().map(SubTitleMapper::toResponse).toList();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        SubTitle subTitle = subTitleRepository.findById(id)
                .orElseThrow(() -> new ResourceCanNotFoundException("Subtitle not found"));

        if (subTitle.getTitle() != null) {
            subTitle.getTitle().getSubTitles().remove(subTitle);
        }
        if (subTitle.getAnalytics() != null && !subTitle.getAnalytics().isEmpty()) {
            List<Analytic> analyticsToRemove = new ArrayList<>(subTitle.getAnalytics());
            for (Analytic analytic : analyticsToRemove) {
                analytic.setSubTitle(null);
                if (analytic.getEmbedLinks() != null) {
                    analytic.getEmbedLinks().clear();
                }
            }
            subTitle.getAnalytics().clear();
        }

        log.info("Delete subtitle by id : {}", id);
        subTitleRepository.delete(subTitle);
        subTitleRepository.flush();
    }

}
