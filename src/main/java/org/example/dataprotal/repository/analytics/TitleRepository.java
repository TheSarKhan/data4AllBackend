package org.example.dataprotal.repository.analytics;

import org.example.dataprotal.model.analytics.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends JpaRepository<Title, Long> {
}
