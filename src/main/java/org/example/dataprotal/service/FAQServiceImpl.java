package org.example.dataprotal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataprotal.dto.request.FAQRequest;
import org.example.dataprotal.dto.response.FAQResponse;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.model.chatbot.FAQ;
import org.example.dataprotal.repository.chatbot.FAQRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {
    private final FAQRepository repository;

    @Override
    public FAQResponse getById(Long id) {
        log.info("Get FAQ by id : {}", id);
        return mapToResponse(repository.findById(id).orElseThrow(() ->
                new ResourceCanNotFoundException("Can not found this question:" + id)));
    }

    @Override
    public List<FAQResponse> getAll() {
        log.info("Get all FAQ");
        return repository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public FAQResponse save(FAQRequest request) {
        log.info("Save FAQ : {}", request);
        FAQ faq = FAQ.builder()
                .question(request.question())
                .answer(request.answer())
                .build();
        return mapToResponse(repository.save(faq));
    }

    @Override
    public FAQResponse update(Long id, FAQRequest request) {
        log.info("Update FAQ by id : {}", id);
        FAQ faq = repository.findById(id).orElseThrow(() ->
                new ResourceCanNotFoundException("Can not found this question:" + id));
        faq.setQuestion(request.question());
        faq.setAnswer(request.answer());
        return mapToResponse(repository.save(faq));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Delete FAQ by id : {}", id);
        repository.deleteById(id);
    }

    private FAQResponse mapToResponse(FAQ faq) {
        return new FAQResponse(faq.getId(), faq.getQuestion(), faq.getAnswer());
    }
}
