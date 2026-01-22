package org.example.dataprotal.repository.dataset.specification;

import org.example.dataprotal.model.dataset.DataSet;
import org.example.dataprotal.model.enums.DataSetStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class DataSetSpecification {

    public static Specification<DataSet> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null :
                        cb.like(cb.lower(root.get("title")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<DataSet> hasCategory(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null :
                        cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<DataSet> hasStatus(DataSetStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    public static Specification<DataSet> createdAfter(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null :
                        cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<DataSet> createdBefore(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null :
                        cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }
}
