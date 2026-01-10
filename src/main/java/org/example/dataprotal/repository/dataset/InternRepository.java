package org.example.dataprotal.repository.dataset;

import org.example.dataprotal.model.dataset.Intern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternRepository extends JpaRepository<Intern, Long> {
}
