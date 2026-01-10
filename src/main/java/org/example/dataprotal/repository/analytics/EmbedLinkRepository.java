package org.example.dataprotal.repository.analytics;

import org.example.dataprotal.model.analytics.EmbedLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmbedLinkRepository extends JpaRepository<EmbedLink, Long> {
    List<EmbedLink> findByAnalyticId(Long analyticId);
}
