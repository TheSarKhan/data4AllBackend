package org.example.dataprotal.repository.researchcard;

import org.example.dataprotal.model.researchcard.ResearchCardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResearchCardLikeRepository extends JpaRepository<ResearchCardLike, Long> {
    Optional<ResearchCardLike> findByResearchCardIdAndUserId(Long researchCardId, Long userId);

    long countByResearchCardId(Long researchCardId);
}
