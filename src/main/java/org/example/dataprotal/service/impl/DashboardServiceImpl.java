package org.example.dataprotal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataprotal.dto.response.DashboardResponse;
import org.example.dataprotal.dto.response.LogResponse;
import org.example.dataprotal.repository.user.LogRepository;
import org.example.dataprotal.repository.user.PaymentHistoryRepository;
import org.example.dataprotal.repository.user.UserRepository;
import org.example.dataprotal.service.DashboardService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final UserRepository userRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final LogRepository logRepository;

    @Override
    @Cacheable("dashboard")
    public DashboardResponse getDashboard() {

        return new DashboardResponse(
                userRepository.countActiveUsers(),
                userRepository.thisMonthUsers(),
                paymentHistoryRepository.totalEarnings(),
                logToResponseDto()
        );
    }

    private List<LogResponse> logToResponseDto() {
        return logRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(log -> new LogResponse(
                        log.getLog(),
                        log.getUserId(),
                        log.getCreatedAt()
                ))
                .toList();
    }

}
