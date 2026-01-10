package org.example.dataprotal.repository.researchcard;

import org.example.dataprotal.model.researchcard.ResearchCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResearchCardRepository extends JpaRepository<ResearchCard, Long> {
    List<ResearchCard> findBySubTitleId(Long researchSubTitleId);
}
