package org.example.dataprotal.repository.analytics;

import org.example.dataprotal.model.analytics.Analytic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalyticRepository extends JpaRepository<Analytic, Long> {

    List<Analytic> findBySubTitleId(Long subTitleId);

    @Query("SELECT DISTINCT a FROM Analytic a LEFT JOIN FETCH a.subTitle LEFT JOIN FETCH a.embedLinks")
    List<Analytic> findAllWithRelations();

    @Query("SELECT DISTINCT a FROM Analytic a LEFT JOIN FETCH a.subTitle LEFT JOIN FETCH a.embedLinks WHERE a.id = :id")
    Optional<Analytic> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT DISTINCT a FROM Analytic a LEFT JOIN FETCH a.subTitle LEFT JOIN FETCH a.embedLinks WHERE a.subTitle.id = :subTitleId")
    List<Analytic> findBySubTitleIdWithRelations(@Param("subTitleId") Long subTitleId);
}