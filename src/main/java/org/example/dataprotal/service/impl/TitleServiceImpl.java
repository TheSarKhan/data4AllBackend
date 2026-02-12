package org.example.dataprotal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataprotal.dto.request.analytic.TitleRequest;
import org.example.dataprotal.dto.request.analytic.UpdatedAnalyticTitle;
import org.example.dataprotal.dto.response.analytic.TitleResponse;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.analytic.TitleMapper;
import org.example.dataprotal.model.analytics.Title;
import org.example.dataprotal.repository.analytics.TitleRepository;
import org.example.dataprotal.service.TitleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TitleServiceImpl implements TitleService {
    private final TitleRepository titleRepository;

    @Override
    public List<TitleResponse> getAll() {
        log.info("Get all titles");
        return titleRepository.findAll().stream().map(TitleMapper::toResponse).toList();
    }

    @Override
    public TitleResponse getById(Long id) {
        log.info("Get title by id : {}", id);
        return TitleMapper.toResponse(titleRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Title not found")));
    }

    @Override
    public TitleResponse save(TitleRequest titleRequest) {
        Title title = TitleMapper.toEntity(titleRequest);
        log.info("Save title : {}", title);
        return TitleMapper.toResponse(titleRepository.save(title));
    }

    @Override
    public TitleResponse update(Long id, UpdatedAnalyticTitle titleRequest) {
        log.info("Update title by id : {}", id);
        Title title = titleRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Title not found"));
        title.setName(titleRequest.name());
        return TitleMapper.toResponse(titleRepository.save(title));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Delete title by id : {}", id);
        titleRepository.deleteById(id);
    }

    @Override
    public void changeOpenedStatus(Long id, boolean isOpened) {
        Title title = titleRepository.findById(id).orElseThrow(() -> new ResourceCanNotFoundException("Title not found"));
        title.setOpened(isOpened);
        titleRepository.save(title);
        log.info("Change opened status of title with id : {} to {}", id, isOpened);
    }
}
