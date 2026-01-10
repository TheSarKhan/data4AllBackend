package org.example.dataprotal.repository.chatbot;

import org.example.dataprotal.model.chatbot.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FAQRepository extends JpaRepository<FAQ,Long> {
}
