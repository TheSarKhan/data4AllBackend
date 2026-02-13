package org.example.dataprotal.repository.dataset;

import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.enums.DataSetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSetRepository
        extends JpaRepository<DataSet, Long>, JpaSpecificationExecutor<DataSet> {

    DataSet findByDataSetNameAndStatus(String dataSetName, DataSetStatus status);

    DataSet findByDataSetName(String dataSetName);

    Page<DataSet> findByCategory_Name(String categoryName, Pageable pageable);

    List<DataSet> findByIntern_id(Long internId);

    List<DataSet> findByCategory_Id(Long categoryId);
}
