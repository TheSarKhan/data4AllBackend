package org.example.dataprotal.repository.analytics;

import org.example.dataprotal.model.analytics.SubTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SubTitleRepository extends JpaRepository<SubTitle, Long> {
    List<SubTitle> findByTitleId(Long titleId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM embed_links WHERE analytic_id IN (SELECT id FROM analytics WHERE sub_title_id = :subtitleId)", nativeQuery = true)
    int deleteEmbedLinksBySubtitleId(@Param("subtitleId") Long subtitleId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM analytics WHERE sub_title_id = :subtitleId", nativeQuery = true)
    int deleteAnalyticsBySubtitleId(@Param("subtitleId") Long subtitleId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM analytic_sub_titles WHERE id = :id", nativeQuery = true)
    int deleteSubTitleByIdNative(@Param("id") Long id);
}