package org.example.dataprotal.repository.researchcard;

import org.example.dataprotal.model.researchcard.ResearchSubTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResearchSubTitleRepository extends JpaRepository<ResearchSubTitle, Long> {
    List<ResearchSubTitle> getByTitleId(Long titleId);
}
