package org.example.dataprotal.repository.dataset;

import org.example.dataprotal.model.dataset.DataSetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataSetCategoryRepository extends JpaRepository<DataSetCategory, Long> {
    Optional<DataSetCategory> findByName(String name);
}
