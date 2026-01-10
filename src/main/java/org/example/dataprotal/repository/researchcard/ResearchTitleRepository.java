package org.example.dataprotal.repository.researchcard;

import org.example.dataprotal.model.researchcard.ResearchTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchTitleRepository extends JpaRepository<ResearchTitle, Long> {
}
