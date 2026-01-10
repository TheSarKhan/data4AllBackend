package org.example.dataprotal.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dataprotal.model.user.Log;
import org.example.dataprotal.repository.user.LogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository repository;

    public void save(String action, Long userId) {
        Log log = Log.builder()
                .log(action)
                .userId(userId)
                .createdAt(Instant.now())
                .build();
        repository.save(log);
    }
}
