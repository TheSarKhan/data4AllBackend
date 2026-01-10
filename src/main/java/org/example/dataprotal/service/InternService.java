package org.example.dataprotal.service;

import org.example.dataprotal.dto.request.InternRequestDto;
import org.example.dataprotal.dto.response.InternResponseDto;

import java.util.List;

public interface InternService {
    InternResponseDto getById(Long id);

    List<InternResponseDto> getAll();

    InternResponseDto save(InternRequestDto internRequestDto);

    InternResponseDto update(Long id, InternRequestDto internRequestDto);

    void deleteById(Long id);
}
