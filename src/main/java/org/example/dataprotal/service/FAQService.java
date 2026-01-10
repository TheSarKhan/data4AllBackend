package org.example.dataprotal.service;

import org.example.dataprotal.dto.request.FAQRequest;
import org.example.dataprotal.dto.response.FAQResponse;

import java.util.List;

public interface FAQService {
    FAQResponse getById(Long id);

    List<FAQResponse> getAll();

    FAQResponse save(FAQRequest request);

    FAQResponse update(Long id, FAQRequest request);

    void deleteById(Long id);
}
