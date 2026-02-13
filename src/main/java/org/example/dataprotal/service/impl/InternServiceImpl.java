package org.example.dataprotal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataprotal.dto.request.InternRequestDto;
import org.example.dataprotal.dto.response.InternResponseDto;
import org.example.dataprotal.exception.ResourceCanNotFoundException;
import org.example.dataprotal.mapper.InternMapper;
import org.example.dataprotal.model.dataset.Intern;
import org.example.dataprotal.repository.dataset.InternRepository;
import org.example.dataprotal.service.InternService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternServiceImpl implements InternService {
    private final InternRepository internRepository;

    @Override
    public InternResponseDto save(InternRequestDto dto) {
        Intern intern = InternMapper.toEntity(dto);
        log.info("Save intern : {}", intern.getId());
        return InternMapper.toResponseDto(internRepository.save(intern));
    }

    @Override
    public InternResponseDto update(Long id, InternRequestDto internRequestDto) {
        log.info("Update intern by id : {}", id);
        Intern intern = internRepository.findById(id)
                .orElseThrow(() -> new ResourceCanNotFoundException("Intern can not found by id : " + id));
        intern.setName(internRequestDto.name());
        intern.setEmail(internRequestDto.email());
        intern.setPhone(internRequestDto.phone());
        intern.setPosition(internRequestDto.status());
        return InternMapper.toResponseDto(internRepository.save(intern));
    }

    @Override
    public List<InternResponseDto> getAll() {
        log.info("Get all interns");
        return internRepository.findAll().stream()
                .map(InternMapper::toResponseDto)
                .toList();
    }

    @Override
    public InternResponseDto getById(Long id) {
        log.info("Get intern by id : {}", id);
        Intern intern = internRepository.findById(id)
                .orElseThrow(() -> new ResourceCanNotFoundException("Intern can not found by id : " + id));
        return InternMapper.toResponseDto(intern);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Delete intern by id : {}", id);
        internRepository.deleteById(id);
    }
}
