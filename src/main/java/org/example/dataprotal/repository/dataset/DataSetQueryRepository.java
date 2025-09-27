package org.example.dataprotal.repository.dataset;

import org.example.dataprotal.model.dataset.DataSetQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSetQueryRepository  extends JpaRepository<DataSetQuery, Long> {
}
