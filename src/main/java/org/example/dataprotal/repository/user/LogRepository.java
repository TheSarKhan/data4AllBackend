package org.example.dataprotal.repository.user;

import org.example.dataprotal.model.user.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findTop10ByOrderByCreatedAtDesc();
}
