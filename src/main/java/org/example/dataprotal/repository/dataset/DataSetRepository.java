package org.example.dataprotal.repository.dataset;

import org.example.dataprotal.model.dataset.DataSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSetRepository extends JpaRepository<DataSet, Long> {
    DataSet findByDataSetName(String dataSetName);
    Page<DataSet> findByCategory(String category, Pageable pageable);
}
