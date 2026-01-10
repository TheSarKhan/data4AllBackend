package org.example.dataprotal.repository.analytics;

import org.example.dataprotal.model.analytics.SubTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTitleRepository extends JpaRepository<SubTitle, Long> {
    List<SubTitle> findByTitleId(Long titleId);
}
