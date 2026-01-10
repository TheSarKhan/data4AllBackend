package org.example.dataprotal.repository.analytics;

import org.example.dataprotal.model.analytics.Analytic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticRepository extends JpaRepository<Analytic, Long> {
    List<Analytic> findBySubTitleId(Long subTitleId);
}
