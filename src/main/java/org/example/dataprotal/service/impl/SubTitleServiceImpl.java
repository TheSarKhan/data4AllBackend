package org.example.dataprotal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataprotal.dto.request.analytic.SubTitleRequest;
import org.example.dataprotal.dto.request.analytic.UpdatedAnalyticSubtitle;
import org.example.dataprotal.dto.response.analytic.SubTitleResponse;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.analytic.SubTitleMapper;
import org.example.dataprotal.model.analytics.SubTitle;
import org.example.dataprotal.model.analytics.Title;
import org.example.dataprotal.repository.analytics.SubTitleRepository;
import org.example.dataprotal.repository.analytics.TitleRepository;
import org.example.dataprotal.service.SubTitleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubTitleServiceImpl implements SubTitleService {
    private final SubTitleRepository subTitleRepository;
    private final TitleRepository titleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SubTitleResponse> getAll() {
        log.info("Get all subtitles");
        return subTitleRepository.findAll()
                .stream()
                .map(SubTitleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SubTitleResponse getById(Long id) {
        log.info("Get subtitle by id : {}", id);
        SubTitle subTitle = subTitleRepository.findById(id)
                .orElseThrow(() -> new ResourceCanNotFoundException("Subtitle not found"));
        return SubTitleMapper.toResponse(subTitle);
    }

    @Override
    @Transactional
    public SubTitleResponse save(SubTitleRequest subTitleRequest) {
        SubTitle subTitle = SubTitleMapper.toEntity(subTitleRequest);
        log.info("Save subtitle : {}", subTitle);
        return SubTitleMapper.toResponse(subTitleRepository.save(subTitle));
    }

    @Override
    @Transactional
    public SubTitleResponse update(Long id, UpdatedAnalyticSubtitle subTitleRequest) {
        log.info("Update subtitle by id : {}", id);
        SubTitle subTitle = subTitleRepository.findById(id)
                .orElseThrow(() -> new ResourceCanNotFoundException("Subtitle not found"));
        subTitle.setName(subTitleRequest.name());
        Title title = titleRepository.findById(subTitleRequest.titleId())
                .orElseThrow(() -> new ResourceCanNotFoundException("Title not found"));
        subTitle.setTitle(title);
        return SubTitleMapper.toResponse(subTitleRepository.save(subTitle));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubTitleResponse> getByTitleId(Long titleId) {
        List<SubTitle> subTitles = subTitleRepository.findByTitleId(titleId);
        return subTitles.stream()
                .map(SubTitleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Deleting subtitle with id: {}", id);

        if (!subTitleRepository.existsById(id)) {
            throw new ResourceCanNotFoundException("Subtitle not found with id: " + id);
        }

        subTitleRepository.deleteEmbedLinksBySubtitleId(id);

        subTitleRepository.deleteAnalyticsBySubtitleId(id);

        subTitleRepository.deleteSubTitleByIdNative(id);

        log.info("Successfully deleted subtitle with id: {}", id);
    }

    @Override
    @Transactional
    public void changeOpenedStatus(Long id, boolean isOpened) {
        SubTitle subTitle = subTitleRepository.findById(id)
                .orElseThrow(() -> new ResourceCanNotFoundException("Subtitle not found"));
        subTitle.setOpened(isOpened);
        subTitleRepository.save(subTitle);
        log.info("Change opened status of subtitle with id : {} to {}", id, isOpened);
    }
}